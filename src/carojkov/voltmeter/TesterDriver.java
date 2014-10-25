package carojkov.voltmeter;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TesterDriver implements Runnable
{
  private final long sec = 1;
  private final long min = sec * 60;
  private final long hour = min * 60;
  private final long day = hour * 24;

  private final long _cycleDuration;
  private final long _checkInterval;
  private final int _cycles;

  private Calendar _cycleStart;
  private Calendar _cycleEnd;
  private Calendar _date;
  private int _cycle = 0;
  private int _check = 0;

  private final Tester _tester;

  private final Timer _timer;

  /**
   * @param startTime
   * @param cycleDuration in seconds
   * @param checkInterval in seconds
   * @param cycles
   */
  public TesterDriver(Tester tester,
                      Calendar startTime,
                      long cycleDuration,
                      long checkInterval,
                      int cycles)
  {
    _tester = tester;
    _cycleDuration = cycleDuration;
    _checkInterval = checkInterval;
    _cycles = cycles;

    _cycleStart = (Calendar) startTime.clone();
    _cycleEnd = getDatePastInterval(_cycleStart, cycleDuration);
    _date = (Calendar) _cycleStart.clone();

    _timer = new Timer(this.getClass().getName(), true);
    _timer.schedule(createTimerTask(), _date.getTime());
  }

  public boolean isComplete()
  {
    return _cycle == _cycles;
  }

  @Override
  public void run()
  {
    if (isCycleStart()) {
      testerStart(_date, _cycle);
      _check = 0;
    }
    else if (isCycleEnd()) {
      testerEnd(_date, _cycle, _check);
      _cycleStart = _date;
      _cycleEnd = getDatePastInterval(_cycleStart, _cycleDuration);
      _cycle++;
    }
    else {
      testerTest(_date, _cycle, _check);
    }

    if (_cycle == _cycles)
      return;

    Calendar nextDate = getDatePastInterval(_date, _checkInterval);

    _date = nextDate;
    Date ddd = nextDate.getTime();
    _timer.schedule(createTimerTask(), ddd);
  }

  public void testerStart(Calendar date, int cycle)
  {
    _tester.start(date, cycle);
  }

  public void testerEnd(Calendar date, int cycle, int check)
  {
    _tester.stop(date, cycle, check);
  }

  public void testerTest(Calendar date, int cycle, int check)
  {
    _tester.test(date, cycle, check);
  }

  private boolean isCycleStart()
  {
    return _date.equals(_cycleStart);
  }

  private boolean isCycleEnd()
  {
    return _date.equals(_cycleEnd);
  }

  private TimerTask createTimerTask()
  {
    return new TimerTask()
    {
      @Override
      public void run()
      {
        TesterDriver.this.run();
      }
    };
  }

  private Calendar getDatePastInterval(Calendar startDate, long l)
  {
    Calendar endDate = (Calendar) startDate.clone();

    final int days = (int) (l / day);
    final int hours = (int) ((l - days * day) / hour);
    final int mins = (int) ((l - days * day - hours * hour) / min);
    final int secs = (int) ((l - days * day - hours * hour - mins * min) / sec);

    endDate.add(Calendar.DATE, days);
    endDate.add(Calendar.HOUR_OF_DAY, hours);
    endDate.add(Calendar.MINUTE, mins);
    endDate.add(Calendar.SECOND, secs);

    return endDate;
  }
}
