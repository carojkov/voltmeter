package carojkov.voltmeter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.Calendar;

public class Voltmeter implements TestStand
{
  private RandomAccessFile _meter;
  private float _voltFactor = 0.0556f;
  private boolean _isVerbose = false;

  private DecimalFormat _format = new DecimalFormat("0000");

  public Voltmeter(String meterFile, boolean isVerbose) throws IOException
  {
    _meter = new RandomAccessFile(meterFile, "rwd");
    _isVerbose = isVerbose;

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
                    int check,
                    boolean isFirst,
                    boolean isLast)
  {
    if (isFirst) {
      reset();
      configure();
    }

    if (!isFirst || isLast) {
      test(date, cycle, check);
    }

    System.out.println();
    resetVoltage();

    if (!isLast)
      test(date, cycle, check);
  }

  @Override
  public void test(Calendar date, int cycle, int check)
  {
    float[] values = measure();
    String str = String.format("%s\t%d\t%f\t%f",
                               date.getTime(),
                               check,
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
    log(feedback);
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
    log(feedback);
  }

  private void switchOff()
  {
    send("PO,B,0,0\r");
    String feedback = read();
    assert "OK".equals(feedback);
    log(feedback);
  }

  private void log(String message)
  {
    if (_isVerbose)
      System.out.println(message);
  }

  private void configure()
  {
    send("C,3,0,0,2\r");
    String feedback = read();
    log(feedback);
  }

  private void send(String command)
  {
    try {
      _meter.write(command.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
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
      e.printStackTrace();
    }

    return null;
  }

  public void run()
  {
    try {
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private float[] readVoltage()
  {
    float[] result = new float[]{-1, -1};

    try {
      send("A\r");
      String value = read();// A,0024,0008

      log("raw-value: " + value);

      String[] valueParts = value.split(",");

      result[0] = _format.parse(valueParts[1]).intValue() * _voltFactor;
      result[1] = _format.parse(valueParts[2]).intValue() * _voltFactor;
    } catch (Exception e) {
      e.printStackTrace();
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
