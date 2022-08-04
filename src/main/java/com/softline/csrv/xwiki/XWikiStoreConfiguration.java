package com.softline.csrv.xwiki;

import io.jmix.autoconfigure.data.JmixLiquibaseCreator;
import io.jmix.core.JmixModules;
import io.jmix.core.Resources;
import io.jmix.data.impl.JmixEntityManagerFactoryBean;
import io.jmix.data.impl.JmixTransactionManager;
import io.jmix.data.impl.liquibase.LiquibaseChangeLogProcessor;
import io.jmix.data.persistence.DbmsSpecifics;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class XWikiStoreConfiguration {

    @Bean
    @ConfigurationProperties("xwiki.datasource")
    DataSourceProperties xwikiDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "xwiki.datasource.hikari")
    DataSource xwikiDataSource(@Qualifier("xwikiDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean
    LocalContainerEntityManagerFactoryBean xwikiEntityManagerFactory(
            @Qualifier("xwikiDataSource") DataSource dataSource,
            JpaVendorAdapter jpaVendorAdapter,
            DbmsSpecifics dbmsSpecifics,
            JmixModules jmixModules,
            Resources resources) {
        return new JmixEntityManagerFactoryBean("xwiki", dataSource, jpaVendorAdapter, dbmsSpecifics, jmixModules, resources);
    }

    @Bean
    JpaTransactionManager xwikiTransactionManager(@Qualifier("xwikiEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JmixTransactionManager("xwiki", entityManagerFactory);
    }

    @Bean
    public SpringLiquibase xwikiLiquibase(LiquibaseChangeLogProcessor processor, @Qualifier("xwikiDataSource") DataSource dataSource) {
        return JmixLiquibaseCreator.create(dataSource, new LiquibaseProperties(), processor, "xwiki");
    }
}
