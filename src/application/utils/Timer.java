package application.utils;

import org.joda.time.Instant;
import org.joda.time.Interval;

public class Timer {
    private Instant startTime;

    public Timer() {
        this.startTime = new Instant();
    }

    public long getTimeSince() {
        Interval interval = new Interval(startTime, new Instant());
        return interval.toDuration().getMillis();
    }
}
