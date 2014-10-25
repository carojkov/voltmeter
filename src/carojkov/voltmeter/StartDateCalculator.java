package carojkov.voltmeter;

import java.util.Calendar;

public class StartDateCalculator
{
  public static Calendar getStartTime(Calendar calendar, StartTime startTime)
  {
    calendar = (Calendar) calendar.clone();

    switch (startTime) {
    case NOW: {
      int seconds = calendar.get(Calendar.SECOND);

      if (!isSecondOn10(calendar))
        seconds = seconds / 10 * 10 + 10;

      calendar.set(Calendar.SECOND, seconds);
      calendar.set(Calendar.MILLISECOND, 0);

      break;
    }
    case MINUTE: {
      if (!isMinuteOn_1(calendar))
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1);

      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      break;
    }
    case MINUTE_ON_5: {
      if (!isMinuteOn_5(calendar)) {
        int min = calendar.get(Calendar.MINUTE);
        min = min / 5 * 5 + 5;
        calendar.set(Calendar.MINUTE, min);
      }

      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      break;
    }
    case MINUTE_ON_10: {
      if (!isMinuteOn_10(calendar)) {
        int min = calendar.get(Calendar.MINUTE);
        min = min / 10 * 10 + 10;
        calendar.set(Calendar.MINUTE, min);
      }

      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      break;
    }
    case DAY: {
      if (!isMidnight(calendar))
        calendar.roll(Calendar.DAY_OF_MONTH, 1);

      calendar.set(Calendar.MILLISECOND, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.HOUR, 0);
      calendar.set(Calendar.AM_PM, Calendar.AM);

      break;
    }
    default: {
      throw new IllegalStateException();
    }
    }

    return calendar;
  }

  private static boolean isSecondOn10(Calendar c)
  {
    if (c.get(Calendar.MILLISECOND) == 0 && c.get(Calendar.SECOND) % 10 == 0)
      return true;
    else
      return false;
  }

  private static boolean isMinuteOn_1(Calendar c)
  {
    if (c.get(Calendar.MILLISECOND) == 0 && c.get(Calendar.SECOND) == 0)
      return true;
    else
      return false;
  }

  private static boolean isMinuteOn_5(Calendar c)
  {
    if (c.get(Calendar.MILLISECOND) == 0
        && c.get(Calendar.SECOND) == 0
        && c.get(Calendar.MINUTE) % 5 == 0)
      return true;
    else
      return false;
  }

  private static boolean isMinuteOn_10(Calendar c)
  {
    if (c.get(Calendar.MILLISECOND) == 0
        && c.get(Calendar.SECOND) == 0
        && c.get(Calendar.MINUTE) % 10 == 0)
      return true;
    else
      return false;
  }

  private static boolean isMidnight(Calendar c)
  {
    if (c.get(Calendar.MILLISECOND) == 0
        && c.get(Calendar.SECOND) == 0
        && c.get(Calendar.MINUTE) == 0
        && c.get(Calendar.HOUR) == 0)
      return true;
    else
      return false;
  }
}
