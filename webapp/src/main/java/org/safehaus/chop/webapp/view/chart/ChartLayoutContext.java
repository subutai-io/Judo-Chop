package org.safehaus.chop.webapp.view.chart;

import org.safehaus.chop.webapp.service.calc.Params;
import org.safehaus.chop.webapp.view.chart.layout.ChartLayout;

public interface ChartLayoutContext {

    public void show(ChartLayout chartView, Params params);

}