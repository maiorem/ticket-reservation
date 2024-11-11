package com.hhplus.io.common.config;

import com.hhplus.io.common.support.api.filter.LoggingFilter;
import com.hhplus.io.common.support.api.filter.TokenAuthenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean tokenAutheFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(new TokenAuthenFilter());
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean loggingFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(new LoggingFilter());
        registrationBean.setOrder(2);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
