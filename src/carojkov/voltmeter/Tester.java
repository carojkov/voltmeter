package carojkov.voltmeter;

import java.util.Calendar;

public interface Tester
{
  public void start(Calendar date, int cycle);

  public void stop(Calendar date, int cycle, int check);

  public void test(Calendar date, int cycle, int check);
}
