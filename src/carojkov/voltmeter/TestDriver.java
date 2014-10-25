package carojkov.voltmeter;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TestDriver implements Runnable
{
  private final long sec = 1;
  private final long min = sec * 60;
  private final long hour = min * 60;
  private final long day = hour * 24;

  private final long _cycleDuration;
  private final long _checkInterval;
  private final int _cycles;

  private Calendar _cycleEnd;
  private Calendar _nextTrigger;
  private int _cycle = -1;
  private int _check = 0;

  private final TestStand _testStand;

  private final Timer _timer;

  /**
   * @param startTime
   * @param cycleDuration in seconds
   * @param checkInterval in seconds
   * @param cycles
   */
  public TestDriver(TestStand testStand,
                    Calendar startTime,
                    long cycleDuration,
                    long checkInterval,
                    int cycles)
  {
    _testStand = testStand;
    _cycleDuration = cycleDuration;
    _checkInterval = checkInterval;
    _cycles = cycles;

    Calendar cycleStart = (Calendar) startTime.clone();
    _nextTrigger = cycleStart;
    _cycleEnd = _nextTrigger;

    _timer = new Timer(this.getClass().getName(), true);
    _timer.schedule(createTimerTask(), cycleStart.getTime());
  }

  public boolean isComplete()
  {
    return _cycle == _cycles;
  }

  @Override
  public void run()
  {
    if (isCycleEnd()) {
      boolean isFirst = _cycle == -1;
      boolean isLast = (_cycle + 1 == _cycles);
      _testStand.reset(_nextTrigger, _cycle, _check, isFirst, isLast);
      _cycleEnd = getDatePastInterval(_cycleEnd, _cycleDuration);
      _cycle++;
      _check = 0;
    }
    else {
      _testStand.test(_nextTrigger, _cycle, _check);
      _check++;
    }

    if (_cycle == _cycles)
      return;

    Calendar nextDate = getDatePastInterval(_nextTrigger, _checkInterval);

    _nextTrigger = nextDate;

    _timer.schedule(createTimerTask(), _nextTrigger.getTime());
  }

  private boolean isCycleEnd()
  {
    return _nextTrigger.equals(_cycleEnd);
  }

  private TimerTask createTimerTask()
  {
    return new TimerTask()
    {
      @Override
      public void run()
      {
        TestDriver.this.run();
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
