package org.safehaus.chop.api;


/**
 * Parameters used by REST resources.
 */
public interface RestParams {
    String CONTENT = "content";
    String FILENAME = "file";
    String RUNNER_HOSTNAME = "runnerHostname";
    String RUNNER_PORT = "runnerPort";
    String RUNNER_IPV4_ADDRESS = "runnerIpv4Address";
    String MODULE_GROUPID = "moduleGroupId";
    String MODULE_ARTIFACTID = "moduleArtifactId";
    String MODULE_VERSION = "moduleVersion";
    String COMMIT_ID = "commitId";
    String USERNAME = "user";
    String PASSWORD = "pwd";
    String TEST_CLASS = "testClass";
    String RUN_NUMBER = "runNumber";
    String RUN_ID = "runId";
}