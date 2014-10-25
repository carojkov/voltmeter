package carojkov.voltmeter;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Schedule implements Runnable
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

  private Timer _timer = new Timer();

  /**
   * @param startTime
   * @param cycleDuration in seconds
   * @param checkInterval in seconds
   * @param cycles
   */
  public Schedule(Calendar startTime,
                  long cycleDuration,
                  long checkInterval,
                  int cycles)
  {
    _cycleDuration = cycleDuration;
    _checkInterval = checkInterval;
    _cycles = cycles;

    _cycleStart = (Calendar) startTime.clone();
    _cycleEnd = getDatePastInterval(_cycleStart, cycleDuration);
    _date = (Calendar) _cycleStart.clone();

    _timer.schedule(createTimerTask(), _date.getTime());
  }

  @Override
  public void run()
  {
    if (isCycleStart()) {
      reset(_date);
      check(_date);

    }
    else if (isCycleEnd()) {
      check(_date);
      reset(_date);

      _cycleStart = _date;
      _cycleEnd = getDatePastInterval(_cycleStart, _cycleDuration);
      _cycle++;
    }
    else {
      check(_date);
    }

    if (_cycle == _cycles)
      return;

    Calendar nextDate = getDatePastInterval(_date, _checkInterval);

    _date = nextDate;
    Date ddd = nextDate.getTime();
    _timer.schedule(createTimerTask(), ddd);
  }

  private void reset(Calendar date)
  {
    System.out.println("Schedule.reset " + date.getTime());
  }

  private void check(Calendar date)
  {
    System.out.println("Schedule.check " + date.getTime());
    _check++;
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
        Schedule.this.run();
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
