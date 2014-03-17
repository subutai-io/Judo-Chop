package org.safehaus.chop.webapp.view.chart.iterations;

import org.safehaus.chop.api.Run;
import org.safehaus.chop.api.RunResult;
import org.safehaus.chop.webapp.dao.RunDao;
import org.safehaus.chop.webapp.dao.RunResultDao;
import org.safehaus.chop.webapp.service.InjectorFactory;
import org.safehaus.chop.webapp.service.calc.iterations.IterationsCollector;
import org.safehaus.chop.webapp.view.chart.iterations.IterationsFormat;
import org.safehaus.chop.webapp.view.util.FileUtil;

import java.util.List;
import java.util.Map;

public class IterationsChart {

    private RunDao runDao = InjectorFactory.getInstance(RunDao.class);
    private RunResultDao runResultDao = InjectorFactory.getInstance(RunResultDao.class);

    public String get() throws Exception {

        String commitId = "7072b85746a980bc5dd9923ccdc9e0ed8e4eb19e";
//        String commitId = "cc471b502aca2791c3a068f93d15b79ff6b7b827";
        String testName = "org.apache.usergrid.persistence.collection.serialization.impl.MvccEntitySerializationStrategyImplTest";

        Map<String, Run> runs = runDao.getMap(commitId, 2, testName);
        Map<Run, List<RunResult>> runResults = runResultDao.getMap(runs);

        IterationsCollector collector = new IterationsCollector(runResults);
        IterationsFormat format = new IterationsFormat(collector);

        String s = FileUtil.getContent("js/iterations-chart.js");
        s = s.replace( "$series", format.getSeries() );

        return s;
    }

}
