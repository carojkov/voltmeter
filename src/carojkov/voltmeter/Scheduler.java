package carojkov.voltmeter;

import java.util.Calendar;

public class Scheduler
{
  public Calendar getStartTime(Calendar calendar, StartTime startTime)
  {
    calendar = (Calendar) calendar.clone();

    switch (startTime) {
    case NOW: {
      int seconds = calendar.get(Calendar.SECOND);
      seconds = seconds / 10 * 10 + 10;
      calendar.set(Calendar.SECOND, seconds);
      calendar.set(Calendar.MILLISECOND, 0);

      break;
    }
    case MINUTE: {
      calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 1);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      break;
    }
    case MINUTE_ON_5: {
      int min = calendar.get(Calendar.MINUTE);
      min = min / 5 * 5 + 5;

      calendar.set(Calendar.MINUTE, min);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      break;
    }
    case MINUTE_ON_10: {
      int min = calendar.get(Calendar.MINUTE);
      min = min / 10 * 10 + 10;

      calendar.set(Calendar.MINUTE, min);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      break;
    }
    case DAY: {
      calendar.set(Calendar.MILLISECOND, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.HOUR, 0);
      calendar.set(Calendar.AM_PM, Calendar.AM);

      calendar.roll(Calendar.DAY_OF_MONTH, 1);

      break;
    }
    default: {
      throw new IllegalStateException();
    }
    }

    return calendar;
  }

}
