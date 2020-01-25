package org.tilacyn.clock;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class EventStatisticsTest {

    @Test
    public void normalTest() {
        EventStatistics eventStatistics = new EventStatisticsImpl(new NormalClock());
        eventStatistics.incEvent("lol");
        assertEquals((double) 1 / 60,  eventStatistics.getEventStatisticByName("lol"), 1e-5);
    }

    @Test
    public void simpleTest() {
        SetableClock clock = new SetableClock();
        EventStatistics eventStatistics = new EventStatisticsImpl(clock);
        add100Events(eventStatistics, clock, "event");

        assertEquals((double) 100 / 60,  eventStatistics.getEventStatisticByName("event"), 1e-5);

        add100Events(eventStatistics, clock, "event");
        add100Events(eventStatistics, clock, "event");

        assertEquals(5,  eventStatistics.getEventStatisticByName("event"), 1e-5);
    }

    @Test
    public void outdatedTest() {
        SetableClock clock = new SetableClock();
        EventStatistics eventStatistics = new EventStatisticsImpl(clock);
        add100Events(eventStatistics, clock, "event");
        clock.plusSeconds(4000);

        add100Events(eventStatistics, clock, "event");

        assertEquals((double) 100 / 60,  eventStatistics.getEventStatisticByName("event"), 1e-5);
    }

    @Test
    public void multipleEventsTest() {
        SetableClock clock = new SetableClock();
        EventStatistics eventStatistics = new EventStatisticsImpl(clock);

        add100Events(eventStatistics, clock, "1");
        add100Events(eventStatistics, clock, "2");
        add100Events(eventStatistics, clock, "3");

        assertEquals((double) 100 / 60,  eventStatistics.getEventStatisticByName("1"), 1e-5);
        assertEquals((double) 100 / 60,  eventStatistics.getEventStatisticByName("2"), 1e-5);
        assertEquals((double) 100 / 60,  eventStatistics.getEventStatisticByName("3"), 1e-5);

        Map<String, Double> statistics = eventStatistics.getAllEventStatistics();

        statistics.values().forEach(d -> assertEquals((double) 100 / 60, d, 1e-5));
    }

    private void add100Events(EventStatistics eventStatistics, SetableClock clock, String name) {
        for (int i = 0; i < 100; i++) {
            eventStatistics.incEvent(name);
            clock.plusSeconds(1);
        }
    }

}