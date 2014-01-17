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
package org.safehaus.chop.runner.rest;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.safehaus.chop.api.Runner;
import org.safehaus.chop.runner.IController;
import org.safehaus.chop.api.BaseResult;
import org.safehaus.chop.api.Result;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.google.inject.Singleton;


/** ... */
@Singleton
@Produces( MediaType.APPLICATION_JSON )
@Path( "/status" )
public class StatusResource {
    private final IController controller;
    private Runner runner;


    @Inject
    public StatusResource( IController controller, Runner runner ) {
        this.controller = controller;
        this.runner = runner;
    }


    @GET
    public Result status() throws JsonProcessingException {
        return new BaseResult( runner.getUrl(), true, null, controller.getState(), controller.getProject() );
    }
}
