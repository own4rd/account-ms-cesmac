package com.example.account_ms.config;

import com.example.account_ms.filter.ApiRateLimitFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReteLimitConfig {
    @Bean
    public FilterRegistrationBean<ApiRateLimitFilter> rateLimitFilter() {
        FilterRegistrationBean<ApiRateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiRateLimitFilter());
        registrationBean.addUrlPatterns("/*"); // Apply to all routes
        return registrationBean;
    }
}
