package carojkov.voltmeter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alex on 2014-09-15.
 */
public class DateUtil
{

  public static String format(Date date)
  {
    DateFormat format = SimpleDateFormat.getDateTimeInstance(DateFormat.LONG,
                                                             DateFormat.LONG);
    return format.format(date);
  }
}
