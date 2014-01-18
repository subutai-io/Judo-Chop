package org.chop.web

import org.apache.commons.lang.StringUtils
import org.chop.service.data.CommitCalc

import org.chop.service.data.FileScanner
import org.chop.service.data.Storage
import org.chop.service.metric.*

class IndexController {

    def index() {

        FileScanner.setup(session.getServletContext())

        List<String> commitDirs = FileScanner.updateStorage()
        Set<String> classNames = Storage.getClassNames()

        String className = getSelectedClassName(classNames)
        MetricType metricType = getSelectedMetricType()

        CommitCalc commitCalc = new CommitCalc(className, metricType)
        Map<String, List<Metric>> commits = commitCalc.get()

        int i = 0
        String str = ""

        commits.each { commitId, list ->

            if (str != "") {
                str += ","
            }

            str += Format.formatCommit(i, commitId, list)
            i++
        }

        str += "," + Format.formatValues( getMainValues(commits) )

        render(view: "/index", model: [commitDirs: commitDirs, classNames: classNames, series: str])
    }

    private MetricType getSelectedMetricType() {

        MetricType metricType = MetricType.AVG

        if ("minTime" == params.metric) {
            metricType = MetricType.MIN
        } else if ("maxTime" == params.metric) {
            metricType = MetricType.MAX
        } else if ("actualTime" == params.metric) {
            metricType = MetricType.ACTUAL
        }

        return metricType
    }

    private String getSelectedClassName(Set<String> classNames) {
        return StringUtils.isEmpty(params.className) ? classNames.first() : params.className
    }

    private List<Metric> getMainValues(Map<String, List<Metric>> commits) {

        List<Metric> values = []

        commits.each { commitId, list ->
            AggregatedMetric aggr = new AggregatedMetric()

            list.each { value ->
                aggr.add(value)
            }

            values.add(aggr)
        }

        return values
    }
}