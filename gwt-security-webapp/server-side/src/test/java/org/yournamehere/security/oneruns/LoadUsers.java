package org.yournamehere.security.oneruns;

import javax.annotation.Resource;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.yournamehere.SecurityConfiguration;
import org.yournamehere.server.utils.GenerateHash;
import org.yournamehere.server.utils.SimpleDAO;
import org.yournamehere.shared.schema.LocalUser;

/**
 *
 * @author Jacek Grzebyta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = SecurityConfiguration.class)
public class LoadUsers {
    
    private Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private SimpleDAO dao;
    
    /**
     * One-run method for loading users into database.
     * 
     * @throws Exception 
     */
    @Test
    @Ignore
    public void loadUsers() throws Exception {
        log.info("load users");
        String user= "user";
        String password = "password";
        
        BCryptPasswordEncoder hashEncoder = new BCryptPasswordEncoder();
        
        for (int i=1; i<=5; i++) {
            String pswd = password + Integer.toString(i);
            
            String passwordHash = hashEncoder.encode(pswd);
            
            LocalUser u = LocalUser.getInstance(
                    GenerateHash.getRandomHash(user,password), 
                    user + Integer.toString(i), 
                    passwordHash);
            
            dao.addUser(u);
        }
    }
}
