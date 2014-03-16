package org.safehaus.chop.webapp.service.calc.overview;

import org.safehaus.chop.webapp.service.metric.Metric;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OverviewAvg {

    public static List<Metric> get(Map<String, Map<Integer, Metric>> values) {

        ArrayList<Metric> metrics = new ArrayList<Metric>();

        for (Map<Integer, Metric> runs : values.values()) {
            metrics.add(getAvg(runs));
        }

        return metrics;
    }

    private static Metric getAvg(Map<Integer, Metric> runs) {

        Metric avg = new Metric();

        for (Metric metric : runs.values()) {
            avg.merge(metric);
        }

        return avg;
    }


}