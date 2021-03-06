package carojkov.voltmeter;

import java.util.Calendar;

public interface TestStand
{
  public void reset(Calendar date,
                    int cycle,
                    boolean isFirst,
                    boolean isLast);

  public void test(Calendar date, int cycle);
}
