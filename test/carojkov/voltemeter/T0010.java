package carojkov.voltemeter;

import carojkov.voltmeter.StartDateCalculator;
import carojkov.voltmeter.StartTime;
import carojkov.voltmeter.Tester;
import carojkov.voltmeter.TesterDriver;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

/**
 * Test Scheduler class (2014-9-14 3:53:34:345 PM)
 */
public class T0010 implements Tester
{
  private final static int year = 2014;
  private final static int month = 9;
  private final static int day = 14;
  private final static int am_pm = Calendar.PM;
  private final static int hour = 3;
  private final static int minute = 53;
  private final static int second = 34;
  private final static int millisecond = 345;

  private final static int period = 1000 * 60;

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

  @Override
  public void start(Calendar date, int cycle)
  {
    System.out.println("T0010.start [" + cycle + "] " + date.getTime());
  }

  @Override
  public void stop(Calendar date, int cycle, int check)
  {
    System.out.println("T0010.stop["
                       + cycle
                       + ':'
                       + check
                       + "] "
                       + date.getTime());
  }

  @Override
  public void test(Calendar date, int cycle, int check)
  {
    System.out.println("T0010.test["
                       + cycle
                       + ':'
                       + check
                       + "] "
                       + date.getTime());
  }

  @Test
  public void test() throws InterruptedException
  {
    StartDateCalculator startDateCalculator = new StartDateCalculator();

    Calendar startTime = startDateCalculator.getStartTime(_calendar,
                                                          StartTime.NOW);

    TesterDriver s = new TesterDriver(this, startTime, 10, 2, 3);
    while (!s.isComplete()) {
      Thread.sleep(1000);
    }
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
