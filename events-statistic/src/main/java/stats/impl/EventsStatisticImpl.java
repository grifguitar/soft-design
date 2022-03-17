package stats.impl;

import clock.Clock;
import stats.EventsStatistic;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class EventsStatisticImpl implements EventsStatistic {
    private static final double ZERO = 0.0;
    private static final double HOUR_MIN = 60.0;
    private static final Duration HOUR = Duration.ofHours(1);

    private final Clock clock;
    private final Map<String, Queue<Instant>> memory;

    public EventsStatisticImpl(Clock clock) {
        this.clock = clock;
        this.memory = new HashMap<>();
    }

    @Override
    public void incEvent(String name) {
        if (!memory.containsKey(name)) {
            memory.put(name, new ArrayDeque<>());
        }
        memory.get(name).add(clock.now());
    }

    @Override
    public double getEventStatisticByName(String name) {
        if (!memory.containsKey(name)) {
            return ZERO;
        }
        Queue<Instant> q = memory.get(name);
        while (!q.isEmpty()) {
            Instant head = q.element();
            if (head.isBefore(clock.getPast(HOUR))) {
                q.remove();
            } else {
                break;
            }
        }
        if (q.isEmpty()) {
            memory.remove(name);
            return ZERO;
        }
        return memory.get(name).size() / HOUR_MIN;
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        Map<String, Double> res = new HashMap<>();
        memory.forEach((name, q) -> res.put(name, getEventStatisticByName(name)));
        return res;
    }

    @Override
    public void printStatistic() {
        getAllEventStatistic().forEach((name, val) -> System.out.println(name + " = " + val));
    }
}
