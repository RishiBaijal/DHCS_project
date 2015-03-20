package in.ac.iiitd.dhcs.focus.Service;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;

final class b
    implements Executor
{

    private b()
    {
    }

    b(byte byte0)
    {
        this();
    }
    public final void execute(Runnable runnable)
    {
        (new Handler(Looper.getMainLooper())).post(runnable);
    }
}
