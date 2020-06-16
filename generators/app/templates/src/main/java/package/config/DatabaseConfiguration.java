package <%= packageName %>.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// todo configure jpa and elasticsearch
@Configuration
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"<%= packageName %>.repository","<%= packageName %>.internal"})
@EnableElasticsearchRepositories(basePackages = {"<%= packageName %>.repository.search", "<%= packageName %>.internal"})
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);
}
