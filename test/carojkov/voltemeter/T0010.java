package carojkov.voltemeter;

import carojkov.voltmeter.StartDateCalculator;
import carojkov.voltmeter.StartTime;
import carojkov.voltmeter.TestDriver;
import carojkov.voltmeter.TestStand;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.LinkedList;

/**
 * Test Scheduler class (2014-9-14 3:53:34:345 PM)
 */
public class T0010 implements TestStand
{
  private final static int year = 2014;
  private final static int month = 9;
  private final static int day = 14;
  private final static int am_pm = Calendar.PM;
  private final static int hour = 3;
  private final static int minute = 53;
  private final static int second = 34;
  private final static int millisecond = 345;

  private Calendar _calendar;

  private LinkedList<String> _expected;

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
  public void reset(Calendar date,
                    int cycle,
                    int check,
                    boolean isFirst,
                    boolean isLast)
  {
    String str = "reset:"
                 + date.getTime()
                 + ':'
                 + cycle
                 + ':'
                 + check
                 + ':'
                 + isFirst
                 + ':'
                 + isLast;
    Assert.assertEquals(_expected.removeFirst(), str);
  }

  @Override
  public void test(Calendar date, int cycle, int check)
  {
    String str = "test:"
                 + date.getTime()
                 + ':'
                 + cycle
                 + ':'
                 + check;
    Assert.assertEquals(_expected.removeFirst(), str);
  }

  @Test
  public void test_10_2_3() throws InterruptedException
  {
    StartDateCalculator startDateCalculator = new StartDateCalculator();
    Calendar startTime = startDateCalculator.getStartTime(_calendar,
                                                          StartTime.NOW);

    _expected = new LinkedList<>();
    _expected.add("reset:Tue Oct 14 15:53:40 PDT 2014:0:0:true:false");
    _expected.add("test:Tue Oct 14 15:53:42 PDT 2014:0:0");
    _expected.add("test:Tue Oct 14 15:53:44 PDT 2014:0:1");
    _expected.add("test:Tue Oct 14 15:53:46 PDT 2014:0:2");
    _expected.add("test:Tue Oct 14 15:53:48 PDT 2014:0:3");
    _expected.add("reset:Tue Oct 14 15:53:50 PDT 2014:1:4:false:false");
    _expected.add("test:Tue Oct 14 15:53:52 PDT 2014:1:0");
    _expected.add("test:Tue Oct 14 15:53:54 PDT 2014:1:1");
    _expected.add("test:Tue Oct 14 15:53:56 PDT 2014:1:2");
    _expected.add("test:Tue Oct 14 15:53:58 PDT 2014:1:3");
    _expected.add("reset:Tue Oct 14 15:54:00 PDT 2014:2:4:false:false");
    _expected.add("test:Tue Oct 14 15:54:02 PDT 2014:2:0");
    _expected.add("test:Tue Oct 14 15:54:04 PDT 2014:2:1");
    _expected.add("test:Tue Oct 14 15:54:06 PDT 2014:2:2");
    _expected.add("test:Tue Oct 14 15:54:08 PDT 2014:2:3");
    _expected.add("reset:Tue Oct 14 15:54:10 PDT 2014:3:4:false:true");

    TestDriver s = new TestDriver(this, startTime, 10, 2, 3);
    while (!s.isComplete()) {
      Thread.sleep(1000);
    }

    Assert.assertTrue(_expected.isEmpty());
  }
}
