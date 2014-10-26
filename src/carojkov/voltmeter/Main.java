package carojkov.voltmeter;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Logger;

public class Main
{
  private Args _args;

  private TestDriver _driver;
  private Logger _logger = Logger.getLogger(Main.class.getName());

  public Main(Args args)
  {
    _args = args;

    StartTime t = _args.getArgEnum(StartTime.class, "-start");

    Calendar start = StartDateCalculator.getStartTime(Calendar.getInstance(),
                                                      t);

    String device = _args.getArg("-in");
    long cycle = args.getArgSeconds("-cycle");
    long check = args.getArgSeconds("-check");
    int cycles = args.getArgInt("-cycles");

    Voltmeter voltmeter = null;

    try {
      voltmeter = new Voltmeter(device);
    } catch (IOException e) {
      e.printStackTrace();

      return;
    }

    logInfo("start-date = " + start.getTime());
    logInfo("cycle = " + cycle);
    logInfo("check = " + check);
    logInfo("cycles = " + cycles);

    _driver = new TestDriver(voltmeter, start, cycle, check, cycles);
  }

  private void logInfo(String message)
  {
    _logger.info(message);
  }

  public void run()
  {
    while (!_driver.isComplete()) {
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
