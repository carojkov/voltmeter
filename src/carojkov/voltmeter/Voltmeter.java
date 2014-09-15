package carojkov.voltmeter;

/**
 * Created by carojkov on 2014-09-11.
 */
public interface Voltmeter
{
  public void init();

  /**
   * Resets the voltage
   */
  public void reset();

  /**
   * Measures voltage
   *
   * @return
   */
  public float[] measure();

  public void close();
}
