package carojkov.voltemeter;

import carojkov.voltmeter.Args;
import carojkov.voltmeter.StartTime;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Args class
 */
public class T0020
{
  @Test
  public void testEnum()
  {
    Args args = Args.parse(new String[]{"xxx", "now"});

    StartTime e = args.getArgEnum(StartTime.class, "xxx");

    Assert.assertEquals(StartTime.NOW, e);
  }

  @Test
  public void testEnumCapital()
  {
    Args args = Args.parse(new String[]{"xxx", "NOW"});

    StartTime e = args.getArgEnum(StartTime.class, "xxx");

    Assert.assertEquals(StartTime.NOW, e);
  }


  @Test
  public void test1Second()
  {
    Args args = Args.parse(new String[]{"xxx", "1"});
    Assert.assertEquals(1, args.getArgSeconds("xxx"));
  }

  @Test
  public void test10Seconds()
  {
    Args args = Args.parse(new String[]{"xxx", "10"});
    Assert.assertEquals(10, args.getArgSeconds("xxx"));
  }

  @Test
  public void test10s()
  {
    Args args = Args.parse(new String[]{"xxx", "10s"});
    Assert.assertEquals(10, args.getArgSeconds("xxx"));
  }

  @Test
  public void test1m()
  {
    Args args = Args.parse(new String[]{"xxx", "1m"});
    Assert.assertEquals(1 * 60, args.getArgSeconds("xxx"));
  }

  @Test
  public void test10m()
  {
    Args args = Args.parse(new String[]{"xxx", "10m"});
    Assert.assertEquals(10 * 60, args.getArgSeconds("xxx"));
  }

  @Test
  public void test1h()
  {
    Args args = Args.parse(new String[]{"xxx", "1h"});
    Assert.assertEquals(1 * 60 * 60, args.getArgSeconds("xxx"));
  }

  @Test
  public void test10h()
  {
    Args args = Args.parse(new String[]{"xxx", "10h"});
    Assert.assertEquals(10 * 60 * 60, args.getArgSeconds("xxx"));
  }

  @Test
  public void test1d()
  {
    Args args = Args.parse(new String[]{"xxx", "1d"});
    Assert.assertEquals(1 * 24 * 60 * 60, args.getArgSeconds("xxx"));
  }

  @Test
  public void test10d()
  {
    Args args = Args.parse(new String[]{"xxx", "10d"});
    Assert.assertEquals(10 * 24 * 60 * 60, args.getArgSeconds("xxx"));
  }

}
