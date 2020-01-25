package org.tilacyn.clock;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EventStatisticsImpl implements EventStatistics {

    private HashMap<String, EventHistory> eventHistories = new HashMap<>();
    private Clock clock;

    public EventStatisticsImpl(Clock clock) {
        this.clock = clock;
    }

    public void incEvent(String name) {
        if (!eventHistories.containsKey(name)) {
            EventHistory eventHistory = new EventHistory(name, clock);
            eventHistory.logEvent(clock.now());
            eventHistories.put(name, eventHistory);
        } else {
            eventHistories.get(name).logEvent(clock.now());
        }
    }

    public void printStatistic() {

    }

    @Override
    public double getEventStatisticByName(String name) {
        if (!eventHistories.containsKey(name)) {
            return 0;
        }
        return eventHistories.get(name).rpm();

    }

    @Override
    public Map<String, Double> getAllEventStatistics() {
        return eventHistories.entrySet().stream().collect(Collectors.toMap(Map.Entry<String, EventHistory>::getKey,
                entry -> entry.getValue().rpm()));
    }

}
