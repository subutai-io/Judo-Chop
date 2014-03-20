package org.safehaus.chop.webapp.view.chart.view;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.safehaus.chop.webapp.service.DataService;
import org.safehaus.chop.webapp.service.InjectorFactory;
import org.safehaus.chop.webapp.service.calc.Params;
import org.safehaus.chop.webapp.view.chart.ChartViewContext;
import org.safehaus.chop.webapp.view.chart.overview.OverviewChart;
import org.safehaus.chop.webapp.view.util.JavaScriptUtil;
import org.safehaus.chop.webapp.view.util.UIUtil;

import java.util.Set;

public abstract class ChartView extends AbsoluteLayout implements JavaScriptFunction {

    private DataService dataService = InjectorFactory.getInstance(DataService.class);

    protected ChartViewContext chartViewContext;
//    private ChartView prevView;
    protected ChartView nextChartView;
    //private ChartBuilder chartBuilder;

    protected ComboBox testNamesCombo;
    protected ComboBox metricCombo;
    protected ComboBox percentileCombo;
    protected ComboBox failureCombo;

    protected Params params;

    protected ChartView(ChartViewContext viewContext, ChartView prevView, ChartView nextChartView, String chartId) {

        this.chartViewContext = viewContext;
//        this.prevView = prevView;
        this.nextChartView = nextChartView;

        setSizeFull();
        addControls(chartId);
    }

    protected void addControls(String chartId) {
        addTestNamesCombo();
        addMetricCombo();
        addPercentileCombo();
        addFailureCombo();
        addSubmitButton();
        addChartLayout(chartId);
    }

    private void addChartLayout(String id) {
        AbsoluteLayout chartLayout = UIUtil.getLayout(id, "700px", "400px");
        addComponent(chartLayout, "left: 10px; top: 150px;");
    }

    protected void addSubmitButton() {

        Button button = UIUtil.getButton("Submit", "100px");
        addComponent(button, "left: 600px; top: 80px;");

        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                submitButtonClicked();
            }
        });
    }

    protected void submitButtonClicked() {
        System.out.println("submit");
    }

    protected void addNextChartButton() {

        Button button = UIUtil.getButton("next chart", "150px");
        button.setStyleName(Reindeer.BUTTON_LINK);
        addComponent(button, "left: 800px; top: 30px;");

        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                nextChartButtonClicked();
            }
        });
    }

    private void nextChartButtonClicked() {
        chartViewContext.show( nextChartView, getParams() );
    }

    private void addTestNamesCombo() {
        testNamesCombo = UIUtil.getCombo("Test Names:");
        addComponent(testNamesCombo, "left: 10px; top: 30px;");
    }

    private void addMetricCombo() {
        String metrics[] = {"Avg Time", "Min Time", "Max Time", "Actual Time"};

        metricCombo = UIUtil.getCombo("Metric:", metrics);
        addComponent(metricCombo, "left: 10px; top: 80px;");
    }

    protected void addPercentileCombo() {
        String values[] = {"100", "90", "80", "70", "60", "50", "40", "30", "20", "10"};

        percentileCombo = UIUtil.getCombo("Percentile:", values);
        addComponent(percentileCombo, "left: 200px; top: 80px;");
    }

    protected void addFailureCombo() {
        String values[] = {"ALL", "FAILED", "SUCCESS"};

        failureCombo = UIUtil.getCombo("Interation Points to Plot:", values);
        addComponent(failureCombo, "left: 400px; top: 80px;");
    }

    protected void populateTestNames(String moduleId) {
        Set<String> testNames = dataService.getTestNames(moduleId);
        UIUtil.populateCombo(testNamesCombo, testNames.toArray(new String[0]));
    }

    protected Params getParams() {
        return new Params(
                params.getModuleId(),
                (String) testNamesCombo.getValue(),
                params.getCommitId(),
                params.getRunNumber(),
                (String) metricCombo.getValue(),
                Integer.parseInt( (String) percentileCombo.getValue() ),
                (String) failureCombo.getValue()
        );
    }

    private OverviewChart overviewChart = new OverviewChart();

    public void show(Params params_) {

//        this.moduleId = params_.getModuleId();
        this.params = params_;
        System.out.println(this.params);

        populateTestNames(params_.getModuleId());

        Params params = getParams();
        System.out.println(params);
        System.out.println("-");

        String chart = overviewChart.get(params);
        JavaScriptUtil.execute(chart);
        JavaScriptUtil.addCallback("overviewChartCallback", this);
    }

//    public void showPrev() {}
//    public void showNext() {}
//    public void show() {}


}
