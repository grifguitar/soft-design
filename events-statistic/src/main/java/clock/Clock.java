package clock;

import java.time.Duration;
import java.time.Instant;

public interface Clock {
    Instant now();

    default Instant getPast(Duration duration) {
        return now().minus(duration);
    }
}
