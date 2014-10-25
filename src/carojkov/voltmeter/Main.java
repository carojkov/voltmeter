package carojkov.voltmeter;

import java.io.IOException;
import java.util.Calendar;

public class Main
{
  private Args _args;

  TestDriver _driver;

  public Main(Args args)
  {
    _args = args;

    StartTime t = _args.getArgEnum(StartTime.class, "-start");

    Calendar start = StartDateCalculator.getStartTime(Calendar.getInstance(),
                                                      t);

    String voltmeterFile = _args.getArg("-in");

    Voltmeter voltmeter = null;

    try {
      voltmeter = new Voltmeter(voltmeterFile);
    } catch (IOException e) {
      e.printStackTrace();

      return;
    }

    long cycle = args.getArgSeconds("-cycle");
    long check = args.getArgSeconds("-check");
    int cycles = args.getArgInt("-cycles");

    _driver = new TestDriver(voltmeter, start, cycle, check, cycles);
  }

  public void run()
  {
    while (_driver.isComplete()) {
      try {
        Thread.sleep(5 * 1000);
      } catch (InterruptedException e) {
      }
    }
  }

  public static void main(String[] argsv)
  {
    Args args = Args.parse(argsv);

    Main main = new Main(args);

    main.run();
  }
}
