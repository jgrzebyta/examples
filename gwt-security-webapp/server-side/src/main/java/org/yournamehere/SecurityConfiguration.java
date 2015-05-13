package org.yournamehere;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.yournamehere.server.utils.CustomAuthenticationProvider;
import org.yournamehere.server.utils.SimpleDAO;

/**
 * Define security resources & scan service beans for Spring-security
 * annotations.
 *
 * @author Jacek Grzebyta
 */
@Configuration
@Import(CoreConfiguration.class)
@ComponentScan({"org.yournamehere.security.server"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Resource
    private SimpleDAO dao;

    private Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    @Override
    public MethodInterceptor methodSecurityInterceptor() throws Exception {
        MethodInterceptor msi = super.methodSecurityInterceptor();
        // by defaul it is 
        //org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor
        log.info("method security interceptor: {}", msi.getClass().getCanonicalName());
        return msi;
    }

    @Bean
    @Override
    protected AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> voters = new ArrayList<>();
        try {
            voters.add(getRoleHierarchyVoter());
            voters.add(getAuthenticatedVoter());
            voters.add(preInvocationVoter());
            log.debug("loaded {} voters", voters.size());
        } catch (Exception e) {
            log.error("some exceptions ", e);
        }

        return new UnanimousBased(voters);
        
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("authenticatyion manager builder: {}", auth.getClass().getCanonicalName());

        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        AuthenticationManager toReturn = super.authenticationManager(); // org.springframework.security.authentication.ProviderManager

        log.info("authentication manager: {}", toReturn);
        return toReturn;
    }

    @Bean
    protected AuthenticationProvider authenticationProvider() throws Exception {
        CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authenticationProvider;
    }

    /* AccessDecisionVoters*/
    /**
     * Implementation of {@link RoleVoter}.
     * @return 
     * @throws Exception 
     */
    @Bean
    public AccessDecisionVoter<Object> getRoleHierarchyVoter() throws Exception {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_USER > ROLE_ANONYMOUS");
        return new RoleHierarchyVoter(hierarchy);
    }

    @Bean
    public AccessDecisionVoter<Object> getAuthenticatedVoter() throws Exception {
        return new AuthenticatedVoter();
    }
    
    @Bean
    public PreInvocationAuthorizationAdviceVoter preInvocationVoter() throws Exception {
        return new PreInvocationAuthorizationAdviceVoter(new ExpressionBasedPreInvocationAdvice());
    }
}
