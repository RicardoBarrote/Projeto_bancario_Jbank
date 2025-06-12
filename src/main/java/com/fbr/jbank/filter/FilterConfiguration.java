package com.fbr.jbank.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    private final IpFilter ipFilter;

    public FilterConfiguration(IpFilter ipFilter) {
        this.ipFilter = ipFilter;
    }

    @Bean
    public FilterRegistrationBean<IpFilter> ipFilterFilterRegistrationBean() {
        var registrationBean = new FilterRegistrationBean<IpFilter>();

        registrationBean.setFilter(ipFilter);
        registrationBean.setOrder(0);

        return registrationBean;
    }
}
