package in.ac.iiitd.dhcs.focus.Service;

public final class ayy
{

    private ayy()
    {
    }

    public static void a(Throwable throwable)
    {
        if (throwable != null)
        {
            d(throwable);
        }
    }

    public static void b(Throwable throwable)
    {
        if (throwable != null)
        {
            d(throwable);
        }
    }

    public static void c(Throwable throwable)
    {
        d(throwable);
    }

    private static String d(Throwable throwable)
    {
        if (throwable == null)
        {
            return "null";
        } else
        {
            return (new StringBuilder()).append(throwable.getClass().getName()).append(": ").append(String.valueOf(throwable.getMessage())).toString();
        }
    }

}
