package com.microservice.authservice.productserver.Config;

import com.microservice.authservice.productserver.AuthFilter.AuthRoleFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final AuthRoleFilter authRoleFilter;

    @Autowired
    public FilterConfig(AuthRoleFilter authRoleFilter) {
        this.authRoleFilter = authRoleFilter;
    }

    @Bean
    public FilterRegistrationBean<AuthRoleFilter> loggingFilter(){
        FilterRegistrationBean<AuthRoleFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authRoleFilter);
//        registrationBean.addUrlPatterns("/api/**");
        return registrationBean;
    }
}