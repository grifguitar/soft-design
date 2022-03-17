package clock.impl;

import clock.Clock;

import java.time.Duration;
import java.time.Instant;

public class SetableClock implements Clock {
    private Instant now;

    public SetableClock(Instant now) {
        this.now = now;
    }

    public void plus(Duration duration) {
        now = now.plus(duration);
    }

    @Override
    public Instant now() {
        return now;
    }
}
