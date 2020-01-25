package org.tilacyn.clock;

import java.util.Map;

public interface EventStatistics {
    void incEvent(String name);
    void printStatistic();
    double getEventStatisticByName(String name);
    Map<String, Double> getAllEventStatistics();
}
