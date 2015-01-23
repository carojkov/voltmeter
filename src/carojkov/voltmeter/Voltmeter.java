package carojkov.voltmeter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Voltmeter implements TestStand
{
  private Logger _logger = Logger.getLogger(Voltmeter.class.getName());

  private RandomAccessFile _meter;
  private float _voltFactor = 0.0556f;

  private DecimalFormat _format = new DecimalFormat("0000");

  public Voltmeter(String device) throws IOException
  {
    _meter = new RandomAccessFile(device, "rwd");

    Runtime.getRuntime().addShutdownHook(new Thread()
    {
      public void run()
      {
        try {
          close();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  @Override
  public void reset(Calendar date,
                    int cycle,
                    boolean isFirst,
                    boolean isLast)
  {
    logFine(String.format("reset %s %d %s %s",
                          date.getTime(),
                          cycle,
                          isFirst,
                          isLast));

    if (isFirst) {
      reset();
      configure();
    } else {
      test(date, cycle);
    }

    resetVoltage();
  }

  @Override
  public void test(Calendar date, int cycle)
  {
    logFine(String.format("test %s %d",
                          date.getTime(),
                          cycle));

    float[] values = measure();
    String str = String.format("%s\t%f\t%f",
                               date.getTime(),
                               values[0],
                               values[1]);

    System.out.println(str);
  }

  public float[] measure()
  {
    try {
      switchOn();
      return readVoltage();
    } finally {
      switchOff();
    }
  }

  private void reset()
  {
    send("R\r");
    String feedback = read();
    logInfo(feedback);
  }

  private void resetVoltage()
  {
    send("PO,B,1,1\r");
    String feedback = read();
    assert "OK".equals(feedback);
    try {
      Thread.sleep(1000 * 2);
    } catch (InterruptedException e) {

    }

    send("PO,B,1,0\r");
    feedback = read();
    assert "OK".equals(feedback);
  }

  private void switchOn()
  {
    send("PO,B,0,1\r");
    String feedback = read();
    assert "OK".equals(feedback);
    logInfo(feedback);
  }

  private void switchOff()
  {
    send("PO,B,0,0\r");
    String feedback = read();
    assert "OK".equals(feedback);
    logInfo(feedback);
  }

  private void logInfo(String message)
  {
    _logger.info(message);
  }

  private void logFine(String message)
  {
    _logger.fine(message);
  }

  private void configure()
  {
    send("C,3,0,0,2\r");
    String feedback = read();
    logInfo(feedback);
  }

  private void send(String command)
  {
    try {
      _meter.write(command.getBytes());
    } catch (IOException e) {
      _logger.log(Level.WARNING, e.getMessage(), e);
    }
  }

  private String read()
  {
    try {
      byte[] buffer = new byte[256];
      int i;

      i = _meter.read(buffer);

      return new String(buffer, 0, i);
    } catch (IOException e) {
      _logger.log(Level.WARNING, e.getMessage(), e);
    }

    return null;
  }

  private float[] readVoltage()
  {
    float[] result = new float[]{-1, -1};

    try {
      send("A\r");
      String value = read();// A,0024,0008

      logInfo("raw-value: " + value);

      String[] valueParts = value.split(",");

      result[0] = _format.parse(valueParts[1]).intValue() * _voltFactor;
      result[1] = _format.parse(valueParts[2]).intValue() * _voltFactor;
    } catch (Exception e) {
      _logger.log(Level.WARNING, e.getMessage(), e);
    }

    return result;
  }

  private void close() throws Exception
  {
    RandomAccessFile file = _meter;
    _meter = null;
    if (file != null)
      file.close();
  }
}
