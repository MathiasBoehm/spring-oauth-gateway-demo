package de.struktuhr.gateway.config;

import de.struktuhr.gateway.security.CustomMethodSecurityExpressionHandler;
import de.struktuhr.gateway.security.CustomPermissionEvaluator;
import de.struktuhr.gateway.security.UserInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Bean
    public UserInfoService userInfoService() {
        return new UserInfoService();
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        final UserInfoService userInfoService = userInfoService();

        final CustomMethodSecurityExpressionHandler expressionHandler = new CustomMethodSecurityExpressionHandler(userInfoService);
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());

        return expressionHandler;
    }
}
