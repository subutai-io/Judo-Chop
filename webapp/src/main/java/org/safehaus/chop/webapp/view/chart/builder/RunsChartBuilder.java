package org.safehaus.chop.webapp.view.chart.builder;

import org.safehaus.chop.api.Run;
import org.safehaus.chop.webapp.dao.RunDao;
import org.safehaus.chop.webapp.service.InjectorFactory;
import org.safehaus.chop.webapp.service.calc.Params;
import org.safehaus.chop.webapp.service.calc.runs.RunsCollector;
import org.safehaus.chop.webapp.view.chart.runs.RunsFormat;
import org.safehaus.chop.webapp.view.util.FileUtil;

import java.util.List;

public class RunsChartBuilder extends ChartBuilder {

    private RunDao runDao = InjectorFactory.getInstance(RunDao.class);

    public String getChart(Params params) {

        List<Run> runs = runDao.getList(params.getCommitId(), params.getTestName());
        RunsCollector collector = new RunsCollector(runs, params.getMetricType(), params.getPercentile(), params.getFailureValue());

        System.out.println(collector);

        RunsFormat format = new RunsFormat(collector);

        String s = FileUtil.getContent("js/runs-chart.js");
        s = s.replace( "$series", format.getSeries() );

        return s;
    }

}