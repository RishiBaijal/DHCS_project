package in.ac.iiitd.dhcs.focus.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

final class a
{

    static final int a;
    static final int b;
    private static final a c = new a();
    private static final int e;
    private final Executor d = new b((byte)0);

    private a()
    {
    }

    public static ExecutorService a()
    {
        ThreadPoolExecutor threadpoolexecutor = new ThreadPoolExecutor(a, b, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue(128));
        if (android.os.Build.VERSION.SDK_INT >= 9)
        {
            threadpoolexecutor.allowCoreThreadTimeOut(true);
        }
        return threadpoolexecutor;
    }

    public static Executor b()
    {
        return c.d;
    }

    static
    {
        int i = Runtime.getRuntime().availableProcessors();
        e = i;
        a = i + 1;
        b = 1 + 2 * e;
    }
    
    
}
