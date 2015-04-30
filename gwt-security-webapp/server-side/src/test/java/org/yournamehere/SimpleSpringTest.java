package org.yournamehere;

import com.google.gwt.thirdparty.guava.common.base.Predicate;
import com.google.gwt.thirdparty.guava.common.collect.Iterables;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.StringUtils;

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
    @Resource
    private ApplicationContext ctx;
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
    
    @Test
    public void listBeans() throws Exception {
        log.info("list all beans in SpringContext");
        
        Assert.assertNotNull(ctx);
        
        int countBeans = ctx.getBeanDefinitionCount();
        Assert.assertTrue(countBeans > 1);
        log.info("context has '{}' beans", ctx.getBeanDefinitionCount());
        
        log.info("list beans:");
        List<String> beans = Arrays.asList(ctx.getBeanDefinitionNames());
        
        for (String b: beans) {
            log.info("\t{}", b);
        }
        
        // are serviceBeans created
        boolean containsServiceBean = Iterables.any(beans, new Predicate<String>(){

            @Override
            public boolean apply(String t) {
                return t.contains("ServiceBean");
            }
        });
        
        Assert.assertTrue("There is no service beans", containsServiceBean);
        
        // is dao created
        Assert.assertTrue("dao is not created", Iterables.any(beans, new Predicate<String>(){

            @Override
            public boolean apply(String t) {
                return t.toLowerCase().contains("dao");
            }
        }));
    }
}
