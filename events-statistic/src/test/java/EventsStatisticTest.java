import clock.impl.SetableClock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import stats.EventsStatistic;
import stats.impl.EventsStatisticImpl;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class EventsStatisticTest extends Assert {
    private static final double EPS = 1e-6;
    private static final double HOUR_MIN = 60.0;
    private static final double ZERO = 0.0;
    private static final double ONE = 1.0 / HOUR_MIN;
    private static final double TWO = 2.0 / HOUR_MIN;
    private static final Duration HOUR = Duration.ofHours(1);
    private static final Duration MINUTE = Duration.ofMinutes(1);
    private static final Duration TICK = Duration.ofMillis(1);

    private SetableClock clock;
    private EventsStatistic eventsStatistic;

    @Before
    public void setUp() {
        clock = new SetableClock(Instant.now());
        eventsStatistic = new EventsStatisticImpl(clock);
    }

    @After
    public void tearDown() {
        clock = null;
        eventsStatistic = null;
    }

    @Test
    public void testOneIncEvent() {
        eventsStatistic.incEvent("G");
        double ans = eventsStatistic.getEventStatisticByName("G");
        assertEquals(ONE, ans, EPS);
    }

    @Test
    public void testTwoDifferentIncEvent() {
        eventsStatistic.incEvent("G");
        eventsStatistic.incEvent("H");
        eventsStatistic.incEvent("G");
        double ans1 = eventsStatistic.getEventStatisticByName("G");
        assertEquals(TWO, ans1, EPS);
        double ans2 = eventsStatistic.getEventStatisticByName("H");
        assertEquals(ONE, ans2, EPS);
    }

    @Test
    public void testDeleteAfterIncEvent() {
        eventsStatistic.incEvent("G");
        clock.plus(TICK);
        eventsStatistic.incEvent("G");
        double ans1 = eventsStatistic.getEventStatisticByName("G");
        assertEquals(TWO, ans1, EPS);
        clock.plus(HOUR);
        double ans2 = eventsStatistic.getEventStatisticByName("G");
        assertEquals(ONE, ans2, EPS);
        clock.plus(TICK);
        double ans3 = eventsStatistic.getEventStatisticByName("G");
        assertEquals(ZERO, ans3, EPS);
    }

    @Test
    public void testIncEventAfterDelete() {
        eventsStatistic.incEvent("G");
        clock.plus(TICK);
        clock.plus(HOUR);
        double ans1 = eventsStatistic.getEventStatisticByName("G");
        assertEquals(ZERO, ans1, EPS);
        eventsStatistic.incEvent("G");
        clock.plus(HOUR);
        double ans2 = eventsStatistic.getEventStatisticByName("G");
        assertEquals(ONE, ans2, EPS);
        clock.plus(TICK);
        double ans3 = eventsStatistic.getEventStatisticByName("G");
        assertEquals(ZERO, ans3, EPS);
    }

    @Test
    public void testGetNotExist() {
        double ans1 = eventsStatistic.getEventStatisticByName("G");
        double ans2 = eventsStatistic.getEventStatisticByName("H");
        assertEquals(ZERO, ans1, EPS);
        assertEquals(ZERO, ans2, EPS);
    }

    @Test
    public void testSyncGroupIncEventAndGetAll() {
        for (int i = 0; i < 1000; i++) {
            eventsStatistic.incEvent("F");
        }
        for (int i = 0; i < 50; i++) {
            eventsStatistic.incEvent("G");
        }
        for (int i = 0; i < 2500; i++) {
            eventsStatistic.incEvent("H");
        }
        Map<String, Double> ans = eventsStatistic.getAllEventStatistic();
        assertEquals(2500 / HOUR_MIN, ans.get("H"), EPS);
        assertEquals(50 / HOUR_MIN, ans.get("G"), EPS);
        assertEquals(1000 / HOUR_MIN, ans.get("F"), EPS);
    }

    @Test
    public void testDelayGroupIncEventAndGetAll() {
        for (int i = 0; i < 1000; i++) {
            eventsStatistic.incEvent("F");
            clock.plus(MINUTE);
        }
        for (int i = 0; i < 2; i++) {
            eventsStatistic.incEvent("G");
            clock.plus(MINUTE);
        }
        for (int i = 0; i < 1; i++) {
            eventsStatistic.incEvent("H");
            clock.plus(TICK);
        }
        Map<String, Double> ans = eventsStatistic.getAllEventStatistic();
        assertEquals(1 / HOUR_MIN, ans.get("H"), EPS);
        assertEquals(2 / HOUR_MIN, ans.get("G"), EPS);
        assertEquals(57 / HOUR_MIN, ans.get("F"), EPS);
    }

    @Test
    public void testPrintStatistic() {
        for (int i = 0; i < 1000; i++) {
            eventsStatistic.incEvent("F");
            clock.plus(MINUTE);
        }
        for (int i = 0; i < 2; i++) {
            eventsStatistic.incEvent("G");
            clock.plus(MINUTE);
        }
        for (int i = 0; i < 1; i++) {
            eventsStatistic.incEvent("H");
            clock.plus(TICK);
        }
        eventsStatistic.printStatistic();
    }
}
