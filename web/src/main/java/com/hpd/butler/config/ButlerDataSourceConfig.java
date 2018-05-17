package com.hpd.butler.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by zy on 7/27/2017.
 */

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef="entityManagerFactoryButler",
        transactionManagerRef="transactionManagerButler",
        basePackages= {"com.hpd.butler.domain"}) //设置Repository所在位置
public class ButlerDataSourceConfig {

    @Autowired @Qualifier("butlerDataSource")
    private DataSource butlerDataSource;

    @Primary
    @Bean(name = "entityManagerButler")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryButler(builder).getObject().createEntityManager();
    }

    @Primary
    @Bean(name = "entityManagerFactoryButler")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryButler (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(butlerDataSource)
                .properties(getVendorProperties(butlerDataSource))
                .packages("com.hpd.butler.domain") //设置实体类所在位置
                .build();
    }

    @Autowired
    private JpaProperties jpaProperties;

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

    @Primary
    @Bean(name = "transactionManagerButler")
    public PlatformTransactionManager transactionManagerButler(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryButler(builder).getObject());
    }

}
