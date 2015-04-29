package org.yournamehere;

import javax.annotation.Resource;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.yournamehere.security.db.SimpleDAO;

/**
 * Default configuration class
 *
 * @author Jacek Grzebyta
 */
@Configuration
@Import(CoreConfiguration.class)
@ComponentScan({"org.yournamehere.server"})
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {
    
    @Resource
    private SimpleDAO dao;
    
    private Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_USER > ROLE_ANONYMOUS");
        return hierarchy;
    }
    
    @Bean
    public RoleHierarchyVoter getRoleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    @Bean
    @Override
    public MethodInterceptor methodSecurityInterceptor() throws Exception {
        return super.methodSecurityInterceptor();
    }

    @Bean
    @Override
    protected AccessDecisionManager accessDecisionManager() {
        return super.accessDecisionManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
        dap.setUserDetailsService(userDetailsService());
        auth.authenticationProvider(dap);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        AuthenticationManager toReturn = super.authenticationManager();
        log.info("authentication manager: {}", toReturn);
        return toReturn;
    }
    
    /**
     * Create anonymous implementation of {@link UserDetailsService}.
     * @return 
     */
    @Bean
    protected UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return dao.getUserDetails(username);
            }
        };
    }
}
