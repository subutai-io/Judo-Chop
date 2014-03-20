package org.safehaus.chop.webapp.elasticsearch;


import org.elasticsearch.client.Client;
import org.junit.rules.TestRule;


public interface IElasticSearchClient extends TestRule {

    public Client getClient();

}
