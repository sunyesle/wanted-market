package com.sunyesle.wanted_market.global.config;

import com.sunyesle.wanted_market.global.log.ReqResLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {

    @Bean
    public FilterRegistrationBean<ReqResLoggingFilter> reqResLoggingFilterBean() {
        FilterRegistrationBean<ReqResLoggingFilter> filterReg = new FilterRegistrationBean<>();
        filterReg.setFilter(new ReqResLoggingFilter());
        filterReg.addUrlPatterns("/api/v1/*");
        return filterReg;
    }
}
