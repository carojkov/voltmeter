package carojkov.voltmeter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends TimerTask
{
  private static final long timerTick = 100;

  private long _lastReset = -1;
  private long _lastMeasure = -1;
  private long _resetPeriod;
  private long _measurePeriod;
  //
  private Timer _timer;

  /**
   * @param resetPeriod   - number of milliseconds between resets
   * @param measurePeriod - number of milliseconds between measurements
   */
  public Main(long resetPeriod, long measurePeriod)
  {
    _resetPeriod = resetPeriod;
    _measurePeriod = measurePeriod;

    //_timer = new Timer();
  }

  public void start(boolean isAtMidnight)
  {
    final long now = System.currentTimeMillis();
    final long delay;
  }



  @Override
  public void run()
  {
    final long now = System.currentTimeMillis();

    boolean isMeasured = false;

    if (_lastReset == -1) {
    }
    else if (_lastMeasure == -1 || (now >= (_lastMeasure + _measurePeriod))) {
      _lastMeasure = now;

      measure(now);

      isMeasured = true;
    }

    if (_lastReset == -1 || now >= (_lastReset + _resetPeriod)) {
      _lastReset = now;

      reset(now);

      if (!isMeasured)
        measure(now);
    }
  }

  private void measure(long time)
  {
    System.out.println("Main.measure " + new Date(time));
  }

  private void reset(long time)
  {
    System.out.println("Main.reset " + new Date(time));
  }

  public static void main(String[] args)
  {
    long resetPeriod = 1000 * 60 * 60 * 24;
    long measurePeriod = 1000 * 1;

//    Main main = new Main(resetPeriod, measurePeriod);

    Calendar c = Calendar.getInstance();

    c.set(Calendar.HOUR, 11);
    c.set(Calendar.MINUTE, 59);


  }

}
