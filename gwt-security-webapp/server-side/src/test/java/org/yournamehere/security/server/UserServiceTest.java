package org.yournamehere.security.server;

import javax.annotation.Resource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.StringUtils;
import org.yournamehere.SecurityConfiguration;
import org.yournamehere.server.utils.AnonymousTokenFactory;
import org.yournamehere.server.utils.SimpleDAO;
import org.yournamehere.shared.exceptions.WrongCredentialsException;

/**
 *
 * @author Jacek Grzebyta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = SecurityConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest {

    private Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private UserService srv;
    @Resource
    private SimpleDAO dao;

    private String token;

    @Resource
    private ApplicationContext ctx;
    
    @Before
    public void loadAnonymousUser() throws Exception {
        log.info("load anonymous user");
        SecurityContextHolder.getContext().setAuthentication(AnonymousTokenFactory.makeAnonymous());
    }
    
    @Test
    public void contextTest() throws Exception {
        log.info("context test");
        
        ProviderManager pm = (ProviderManager) ctx.getBean("authenticationManager", AuthenticationManager.class);
        log.info("list od providers: {}", pm.getProviders());
    }
    

    @Test
    public void loginTest() throws Exception {
        log.info("login test");
        
        String passwd = "password1";
        
        log.info("user details: '{}' '{}'", "user1", passwd);
        token = srv.login("user1", passwd, "Test-Agent");
        
        
        log.info("token value: {}", token);
        
        Assert.assertTrue(StringUtils.hasText(token));
    }
    
    @Test(expected = WrongCredentialsException.class)
    public void badCredentials() throws Exception {
        log.info("bad credentials test");
        
        token = srv.login("user1", "bad password", "Test-Agent");
        log.info("token: {}", token); // this line should not be reachable
    }
    
    @Test
    public void reloginUser() throws Exception {
        log.info("re-login user");
        
        // proper user login
        token  = srv.login("user3", "password3", "User-Agent");
        Assert.assertTrue(StringUtils.hasText(token));
        
        // Load anonymous user
        SecurityContextHolder.getContext().setAuthentication(AnonymousTokenFactory.makeAnonymous());
        String newToken = srv.login(token, "User-Agent");
        
        
        Assert.assertSame(token, newToken);
        Assert.assertTrue(SecurityContextHolder.getContext().getAuthentication() != null);
    }
    
    @After
    public void removeToken() throws Exception {
        log.info("delete token");

        if (StringUtils.hasText(token)) {
            dao.removeToken(token);
        }
    }
}
