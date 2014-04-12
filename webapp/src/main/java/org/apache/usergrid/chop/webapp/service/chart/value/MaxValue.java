package org.apache.usergrid.chop.webapp.service.chart.value;

import org.apache.usergrid.chop.api.Run;

class MaxValue extends Value {

    @Override
    protected void calcValue(Run run) {
        value = Math.max( value, run.getMaxTime() );
    }

}