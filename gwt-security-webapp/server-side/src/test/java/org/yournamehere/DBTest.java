package org.yournamehere;

import java.util.ArrayList;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.derby.tools.dblook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 *
 * @author Jacek Grzebyta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = SecurityConfiguration.class)
public class DBTest {
    
    @Resource
    private DriverManagerDataSource dataSource;
    
    private Logger log = LoggerFactory.getLogger(getClass());
    
    @Test
    public void displayDDL() throws Exception {
        String url = dataSource.getUrl();
        log.info("database URL: \"{}\"", url);
        
        // add user and password
        url = url + ";user=db;password=db";
        
        ArrayList<String> argsList = new ArrayList<>(2);
        argsList.add("-d");
        argsList.add(url);
        
        dblook.main(argsList.toArray(new String[]{}));
    }
}
