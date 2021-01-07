package com.iyang.spring.boot.jpa.analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;

/**
 *
 * @author Yang
 *
 */

//@EnableJpaRepositories(queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
@SpringBootApplication
public class SpringBootJpaAnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaAnalysisApplication.class, args);
    }

}
