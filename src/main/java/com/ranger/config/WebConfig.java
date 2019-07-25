package com.ranger.config;

import com.ranger.config.http.UserRequestFilter;
import com.ranger.utils.RedisStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private RedisStringUtil redisUtil;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.addUrlPatterns("/*");
        UserRequestFilter userRequestFilter = new UserRequestFilter(redisUtil);
        filterRegistrationBean.setFilter(userRequestFilter);
        return filterRegistrationBean;
    }
}
