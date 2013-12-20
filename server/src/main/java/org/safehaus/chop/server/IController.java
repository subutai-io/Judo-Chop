package org.safehaus.chop.server;


import org.safehaus.chop.api.StatsSnapshot;
import org.safehaus.chop.api.ISummary;
import org.safehaus.chop.api.Project;
import org.safehaus.chop.api.State;


/**
 * Created with IntelliJ IDEA. User: akarasulu Date: 12/19/13 Time: 12:15 AM To change this template use File | Settings
 * | File Templates.
 */
public interface IController {
    void reset();

    StatsSnapshot getCallStatsSnapshot();

    State getState();

    ISummary getSummary();

    boolean isRunning();

    boolean needsReset();

    long getStartTime();

    long getStopTime();

    void start();

    void stop();

    Project getProject();
}
