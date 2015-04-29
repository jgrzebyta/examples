package org.yournamehere.security;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.StringUtils;
import org.yournamehere.SecurityConfiguration;

/**
 *
 * @author Jacek Grzebyta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = SecurityConfiguration.class)
public class SimpleSpringTest {
    
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private DataSource dbSource;
    private Logger log = LoggerFactory.getLogger(getClass());
    
    @Test
    public void configurationTest() throws Exception {
        log.info("configuration test");
        
        Assert.assertNotNull(jdbcTemplate);
        Assert.assertNotNull(dbSource);
        
        String dataUrl = ((DriverManagerDataSource) dbSource).getUrl();
        Assert.assertTrue(StringUtils.hasText(dataUrl));
        log.info("URL: {}", dataUrl);
    }
}
