package org.safehaus.chop.webapp.dao;

import org.junit.Test;
import org.safehaus.chop.api.Run;
import org.safehaus.chop.api.RunResult;
import org.safehaus.chop.webapp.elasticsearch.ESSuiteTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class RunResultDaoTest {

    private static Logger LOG = LoggerFactory.getLogger( RunResultDaoTest.class );


    @Test
    public void getAll() {

        LOG.info( "\n===RunResultDaoTest.getAll===\n" );

        List<RunResult> list = ESSuiteTest.runResultDao.getAll();

        for ( RunResult runResult : list ) {
            LOG.info( runResult.toString() );
        }

        assertEquals( 3, list.size() );
    }

    @Test
    public void getMap() {

        LOG.info( "\n===RunResultDaoTest.getMap===\n" );

        Map<String, Run> runs = ESSuiteTest.runDao.getMap( ESSuiteTest.COMMIT_ID_2, 2, ESSuiteTest.TEST_NAME );
        Map<Run, List<RunResult>> runResults = ESSuiteTest.runResultDao.getMap( runs );

        for ( Run run : runResults.keySet() ) {
            LOG.info( run.toString() );

            for (RunResult runResult : runResults.get( run ) ) {
                LOG.info( "   {}", runResult.toString() );
            }
        }

        assertEquals( 1, runResults.size() );
    }

}
