/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.safehaus.perftest;


import org.safehaus.perftest.api.Perftest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** A performance test that does nothing. */
public class NoopPerftest implements Perftest {
    private static final Logger LOG = LoggerFactory.getLogger( NoopPerftest.class );


    @Override
    public long getCallCount() {
        return 1000;
    }


    @Override
    public int getThreadCount() {
        return 10;
    }


    @Override
    public long getDelayBetweenCalls() {
        return 0;
    }


    @Override
    public void call() {
        LOG.info( "This performance test tests nothing" );
    }
}