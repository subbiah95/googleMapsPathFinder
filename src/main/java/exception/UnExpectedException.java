package exception;

import java.text.MessageFormat;

public class UnExpectedException extends Exception
{
    public UnExpectedException(String base, Object... args)
    {
        super(MessageFormat.format(base, args));
    }
}