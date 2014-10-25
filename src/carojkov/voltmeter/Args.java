package carojkov.voltmeter;

import java.util.Objects;

public class Args
{
  private String[] _args;

  private Args(String[] args)
  {
    Objects.requireNonNull(args);
    _args = args;
  }

  public long getArgSeconds(String key)
  {
    String value = getArg(key);

    long l = 0;
    boolean isPatternComplete = false;
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);

      if (isPatternComplete)
        throw new IllegalArgumentException();

      switch (c) {
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9': {
        l = l * 10 + (c - '0');
        break;
      }
      case 'd': {
        l = l * 24 * 60 * 60;
        isPatternComplete = true;
        break;
      }
      case 'h': {
        l = l * 60 * 60;
        isPatternComplete = true;
        break;
      }
      case 'm': {
        l = l * 60;
        isPatternComplete = true;
        break;
      }
      case 's': {
        isPatternComplete = true;
        break;
      }
      default: {
        throw new IllegalArgumentException(String.format(
          "can't convert %s to seconds"));
      }
      }
    }

    return l;
  }

  public int getArgInt(String key)
  {
    String value = getArg(key);

    return Integer.parseInt(value);
  }

  public <T extends Enum<T>> T getArgEnum(Class<T> e, String key)
  {
    String value = getArg(key);

    return Enum.valueOf(e, value);
  }

  public String getArg(String key)
  {
    for (int i = 0; i < _args.length; i++) {
      String arg = _args[i];
      if (arg.equals(key) && ((i + 1) < _args.length))
        return _args[++i];
    }

    return null;
  }

  public static Args parse(String[] args)
  {
    return new Args(args);
  }
}
