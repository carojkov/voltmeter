package alex.voltmeter;

import java.io.*;
import java.util.*;
import java.text.*;

public class Voltmeter extends TimerTask
{
  private RandomAccessFile _meter;
  private Timer _timer;
  private float _voltFactor = 0.0556f;
  private int _probeCounter = 0;
  private int _probesTotal;
  private int _pause;
  private boolean _isVerbose = false;

  private DecimalFormat _format = new DecimalFormat("0000");

  public Voltmeter(String[] args) throws IOException
  {
    _meter = new RandomAccessFile(args[0], "rwd");

    _probesTotal = Integer.parseInt(args[1]);

    _pause = 1000 * Integer.parseInt(args[2]);

    _timer = new Timer();

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

  private void reset() throws IOException
  {
    send("R\r");
    String feedback = read();
    log(feedback);
  }

  private void resetVoltage() throws InterruptedException, IOException
  {
    send("PO,B,1,1\r");
    String feedback = read();
    assert "OK".equals(feedback);

    Thread.sleep(1000 * 2);

    send("PO,B,1,0\r");
    feedback = read();
    assert "OK".equals(feedback);
  }

  private void switchOn() throws IOException
  {
    send("PO,B,0,1\r");
    String feedback = read();
    assert "OK".equals(feedback);

    if (_isVerbose)
      log(feedback);
  }

  private void switchOff() throws IOException
  {
    send("PO,B,0,0\r");
    String feedback = read();
    assert "OK".equals(feedback);
    if (_isVerbose)
      log(feedback);
  }

  private void log(String message)
  {
    System.out.println(message);
  }

  private void configure() throws IOException
  {
    send("C,3,0,0,2\r");
    String feedback = read();
    if (_isVerbose)
      log(feedback);
  }

  private void send(String command) throws IOException
  {
    _meter.write(command.getBytes());
  }

  private String read() throws IOException
  {
    byte[] buffer = new byte[256];
    int i;

    i = _meter.read(buffer);

    return new String(buffer, 0, i);
  }

  public void run()
  {
    try {
      switchOn();
      readVoltage();
      switchOff();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    if (++_probeCounter < _probesTotal) {
    }
    else {
      _timer.cancel();
    }
  }

  private void schedule()
  {
    _timer.schedule(this, new java.util.Date(), _pause);
  }

  private void readVoltage() throws IOException, ParseException
  {
    send("A\r");

    String feedback = read();// A,0024,0008
    String[] voltage = feedback.split(",");

    if (voltage.length != 3) {
      log("error: [" + feedback + "]");
      return;
    }

    float v0 = _format.parse(voltage[1]).intValue() * _voltFactor;
    float v1 = _format.parse(voltage[2]).intValue() * _voltFactor;

    log(new Date() + "\t (" + _probeCounter + ")" + ":\t" + v0 + '\t' + v1);
  }

  private void close() throws Exception
  {
    RandomAccessFile file = _meter;
    _meter = null;
    if (file != null)
      file.close();
  }

  public static void main(String[] args) throws Exception
  {
    Voltmeter t = new Voltmeter(args);
    t.reset();
    t.configure();
    t.resetVoltage();
    t.switchOn();
    t.schedule();
    //	t.close();
  }
}
