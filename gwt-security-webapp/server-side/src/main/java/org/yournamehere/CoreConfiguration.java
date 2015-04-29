package org.yournamehere;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Default configuration class
 *
 * @author Jacek Grzebyta
 */
@Configuration
@PropertySource("classpath:db.properties")
@ComponentScan({"org.yournamehere.security.db"})
public class CoreConfiguration {
    
    @Autowired
    private Environment env;
    
    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource(env.getProperty("db.url"), env.getProperty("db.username"), env.getProperty("db.password"));
        ds.setDriverClassName(env.getProperty("db.driver"));
        return ds;
    }
    
    
    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }
}
