package com.yumi.butler.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Druid的DataResource配置类
 *
 * @author teddyzhou
 *
 *         凡是被Spring管理的类，实现接口 EnvironmentAware 重写方法 setEnvironment 可以在工程启动时，
 *         获取到系统环境变量和application配置文件中的变量。 还有一种方式是采用注解的方式获取 @value("${变量的key值}")
 *         ：获取application配置文件中的变量。 这里采用第一种要方便些
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig implements EnvironmentAware {
    @Value("${butler.datasource.url}")
    private String butlerDbUrl;
    @Value("${butler.datasource.username}")
    private String butlerDbUser;
    @Value("${butler.datasource.password}")
    private String butlerDbPwd;
    @Value("${butler.datasource.driver-class-name}")
    private String butlerDbDriver;

    @Value("${platform.datasource.url}")
    private String platformDbUrl;
    @Value("${platform.datasource.username}")
    private String platformDbUser;
    @Value("${platform.datasource.password}")
    private String platformDbPwd;
    @Value("${platform.datasource.driver-class-name}")
    private String platformDbDriver;

    private RelaxedPropertyResolver propertyResolver;

    public void setEnvironment(Environment env) {
        this.propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
    }

    @Bean(name = "butlerDataSource")
    @Qualifier("butlerDataSource")
    @Primary
    @ConfigurationProperties(prefix="butler.datasource")
    public DataSource butlerDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(butlerDbUrl);
        datasource.setDriverClassName(butlerDbDriver);
        datasource.setUsername(butlerDbUser);
        datasource.setPassword(butlerDbPwd);

        return setDruidProperty(datasource);
    }

    @Bean(name = "platformDataSource")
    @Qualifier("platformDataSource")
//    @Primary  主数据源配置
    @ConfigurationProperties(prefix="platform.datasource")
    public DataSource platformDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(platformDbUrl);
        datasource.setDriverClassName(platformDbDriver);
        datasource.setUsername(platformDbUser);
        datasource.setPassword(platformDbPwd);

        return setDruidProperty(datasource);
    }

    public DataSource setDruidProperty(DruidDataSource datasource){
        datasource.setMaxActive(Integer.valueOf(propertyResolver.getProperty("maxActive")));
        datasource.setInitialSize(Integer.valueOf(propertyResolver.getProperty("initialSize")));
        datasource.setMaxWait(Long.valueOf(propertyResolver.getProperty("maxWait")));
        datasource.setMinIdle(Integer.valueOf(propertyResolver.getProperty("minIdle")));
        datasource.setTimeBetweenEvictionRunsMillis(Long.valueOf(propertyResolver.getProperty("timeBetweenEvictionRunsMillis")));
        datasource.setMinEvictableIdleTimeMillis(Long.valueOf(propertyResolver.getProperty("minEvictableIdleTimeMillis")));
        datasource.setValidationQuery(propertyResolver.getProperty("validationQuery"));
        datasource.setTestWhileIdle(Boolean.valueOf(propertyResolver.getProperty("testWhileIdle")));
        datasource.setTestOnBorrow(Boolean.valueOf(propertyResolver.getProperty("testOnBorrow")));
        datasource.setTestOnReturn(Boolean.valueOf(propertyResolver.getProperty("testOnReturn")));
        datasource.setPoolPreparedStatements(Boolean.valueOf(propertyResolver.getProperty("poolPreparedStatements")));
        datasource.setMaxOpenPreparedStatements(Integer.valueOf(propertyResolver.getProperty("maxOpenPreparedStatements")));
        try {
            datasource.setFilters(propertyResolver.getProperty("filters"));
        } catch (SQLException e) {
            //logger.error("druid configuration initialization filter", e);
        }

        return  datasource;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        //http://localhost:9081/orderservice/druid/datasource.html
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("loginUsername", "admin");// 用户名
        initParameters.put("loginPassword", "admin");// 密码
        initParameters.put("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能
//		initParameters.put("allow", ""); // IP白名单 (没有配置或者为空，则允许所有访问)
        // initParameters.put("deny", "192.168.20.38");// IP黑名单
        // (存在共同时，deny优先于allow)
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }
}
