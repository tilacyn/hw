package org.tilacyn.clock;

import java.time.Instant;
import java.util.SortedSet;
import java.util.TreeSet;

public class EventHistory {
    private static long HOUR = 3600;

    private String name;
    private SortedSet<Instant> timeStamps;
    private Clock clock;

    public EventHistory(String name, Clock clock) {
        this.name = name;
        this.clock = clock;
        timeStamps = new TreeSet<>();
    }

    public void logEvent(Instant timestamp) {
        timeStamps.add(timestamp);
    }

    public double rpm() {
        update();
        return (double) timeStamps.size() / 60;
    }

    public void update() {
        timeStamps = timeStamps.tailSet(clock.now().minusSeconds(HOUR));
    }

}
