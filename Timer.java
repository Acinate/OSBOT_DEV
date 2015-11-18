/**
 * Created by Admin on 10/24/2015.
 */

public class Timer
{

    private long period;
    private long start;

    public Timer(long period)
    {
        this.period = period;
        start = System.currentTimeMillis();
    }

    public long getElapsed()
    {
        return System.currentTimeMillis() - start;
    }

    public long getRemaining()
    {
        return period - getElapsed();
    }

    public boolean isRunning()
    {
        return getElapsed() <= period;
    }

    public void reset()
    {
        start = System.currentTimeMillis();
    }

    public void stop()
    {
        period = 0;
    }

    public static String format(long milliSeconds)
    {
        long secs = milliSeconds / 1000L;
        return String.format("%02d:%02d:%02d", new Object[] {
                Long.valueOf(secs / 3600L), Long.valueOf((secs % 3600L) / 60L), Long.valueOf(secs % 60L)
        });
    }
}

