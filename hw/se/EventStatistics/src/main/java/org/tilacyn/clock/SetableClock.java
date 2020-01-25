package org.tilacyn.clock;

import java.time.Instant;

public class SetableClock implements Clock {
    private Instant now = Instant.now();


    public void setNow(Instant now) {
        this.now = now;
    }

    public void plusSeconds(long seconds) {
        this.now = this.now.plusSeconds(seconds);
    }

    @Override
    public Instant now() {
        return now;
    }
}
