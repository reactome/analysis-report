package org.reactome.server.tools.analysis.exporter.playground.util;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.SessionFactory;
import org.reactome.server.graph.config.Neo4jConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = {"org.reactome.server.graph"})
@EnableTransactionManagement
@EnableNeo4jRepositories(basePackages = {"org.reactome.server.graph.repository"})
@EnableSpringConfigured
public class GraphCoreConfig extends Neo4jConfig {
    private SessionFactory sessionFactory;
    private Logger logger = LoggerFactory.getLogger(GraphCoreConfig.class);

    @Bean
    public Configuration getConfiguration() {
        Configuration config = new Configuration();
//        System.out.println(System.getProperty("neo4j.host"));
//        System.out.println(System.getProperty("neo4j.port"));
//        System.out.println(System.getProperty("neo4j.user"));
//        System.out.println(System.getProperty("neo4j.password"));
        config.driverConfiguration()
//                .setDriverClassName(System.getProperty("driver"))
//                .setURI(System.getProperty("URI"))
                .setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")//in the context should have the neo4j properties.
                .setURI("http://".concat(System.getProperty("neo4j.host")).concat(":").concat(System.getProperty("neo4j.port")))
                .setCredentials(System.getProperty("neo4j.user"), System.getProperty("neo4j.password"));
        return config;
    }

    @Override
    @Bean
    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            logger.info("Creating a Neo4j SessionFactory");
            sessionFactory = new SessionFactory(getConfiguration(), "org.reactome.server.graph.domain");
        }
        return sessionFactory;
    }
}
