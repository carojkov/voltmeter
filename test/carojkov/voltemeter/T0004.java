package carojkov.voltemeter;

import carojkov.voltmeter.Scheduler;
import carojkov.voltmeter.StartTime;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

/**
 * Test Scheduler class (2014-9-14 3:50:00:00 PM)
 */
public class T0004
{
  private final static int year = 2014;
  private final static int month = 9;
  private final static int day = 14;
  private final static int am_pm = Calendar.PM;
  private final static int hour = 3;
  private final static int minute = 50;
  private final static int second = 00;
  private final static int millisecond = 00;

  Calendar _calendar;

  @Before
  public void init()
  {
    _calendar = Calendar.getInstance();
    _calendar.set(Calendar.YEAR, year);
    _calendar.set(Calendar.MONTH, month);
    _calendar.set(Calendar.DAY_OF_MONTH, day);
    _calendar.set(Calendar.AM_PM, am_pm);
    _calendar.set(Calendar.HOUR, hour);
    _calendar.set(Calendar.MINUTE, minute);
    _calendar.set(Calendar.SECOND, second);
    _calendar.set(Calendar.MILLISECOND, millisecond);
  }

  @Test
  public void testNow()
  {
    Scheduler scheduler = new Scheduler();

    Calendar value = scheduler.getStartTime(_calendar, StartTime.NOW);

    check(value, year, month, day, am_pm, hour, minute, second, millisecond);
  }

  @Test
  public void testMinute()
  {
    Scheduler scheduler = new Scheduler();

    Calendar value = scheduler.getStartTime(_calendar, StartTime.MINUTE);

    check(value, year, month, day, am_pm, hour, minute, second, millisecond);
  }

  @Test
  public void testMinuteOn5()
  {
    Scheduler scheduler = new Scheduler();

    Calendar value = scheduler.getStartTime(_calendar, StartTime.MINUTE_ON_5);

    check(value, year, month, day, am_pm, hour, minute, second, millisecond);
  }

  @Test
  public void testMinuteOn10()
  {
    Scheduler scheduler = new Scheduler();

    Calendar value = scheduler.getStartTime(_calendar,
                                            StartTime.MINUTE_ON_10);
    check(value, year, month, day, am_pm, hour, minute, second, millisecond);
  }

  @Test
  public void testDay()//midnight
  {
    Scheduler scheduler = new Scheduler();

    Calendar value = scheduler.getStartTime(_calendar, StartTime.DAY);

    check(value, year, month, day + 1, Calendar.AM, 0, 0, 0, 0);
  }

  private void check(Calendar c,
                     int year,
                     int month,
                     int day,
                     int am_pm,
                     int hour,
                     int minute,
                     int second,
                     int millisecond)
  {
    Assert.assertEquals(year, c.get(Calendar.YEAR));
    Assert.assertEquals(month, c.get(Calendar.MONTH));
    Assert.assertEquals(day, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(am_pm, c.get(Calendar.AM_PM));
    Assert.assertEquals(hour, c.get(Calendar.HOUR));
    Assert.assertEquals(minute, c.get(Calendar.MINUTE));
    Assert.assertEquals(second, c.get(Calendar.SECOND));
    Assert.assertEquals(millisecond, c.get(Calendar.MILLISECOND));
  }
}
