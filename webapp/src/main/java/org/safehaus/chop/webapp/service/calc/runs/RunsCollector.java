package org.safehaus.chop.webapp.service.calc.runs;

import org.safehaus.chop.api.Run;
import org.safehaus.chop.webapp.service.metric.Metric;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RunsCollector {

    // <runNumber, metric>
    private final Map<Integer, Metric> runs = new TreeMap<Integer, Metric>();

    public RunsCollector(List<Run> list) {
        for (Run run : list) {
            collect(run);
        }
    }

    private void collect(Run run) {

        Metric metric = runs.get( run.getRunNumber() );

        if (metric == null) {
            metric = new Metric();
            runs.put(run.getRunNumber(), metric);
        }

        metric.merge(run);
    }

    public Map<Integer, Metric> getRuns() {
        Map<Integer, Metric> filteredRuns = RunsPercentile.filter(runs, 100);
        return FailureFilter.filter(filteredRuns, null);
    }

    @Override
    public String toString() {
        return runs.toString();
    }
}