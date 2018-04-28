package com.yumi.butler.config;

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
        entityManagerFactoryRef="entityManagerFactoryplatform",
        transactionManagerRef="transactionManagerplatform",
        basePackages= { "com.yumi.butler.domain.platform" }) //设置Repository所在位置
public class PlatformDataSourceConfig {

    @Autowired @Qualifier("platformDataSource")
    private DataSource platformDataSource;

    @Bean(name = "entityManagerplatform")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryplatform(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactoryplatform")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryplatform (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(platformDataSource)
                .properties(getVendorProperties(platformDataSource))
                .packages("com.iyumi.platformservice.domain.platform") //设置实体类所在位置
                .build();
    }

    @Autowired
    private JpaProperties jpaProperties;

    private Map<String, String> getVendorProperties(DataSource dataSource) {
        return jpaProperties.getHibernateProperties(dataSource);
    }

//    @Primary
    @Bean(name = "transactionManagerplatform")
    public PlatformTransactionManager transactionManagerplatform(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryplatform(builder).getObject());
    }

}
