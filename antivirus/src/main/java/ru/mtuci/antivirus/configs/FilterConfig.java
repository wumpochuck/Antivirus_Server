// // That filter was needed for testing client & service connection to server
//package ru.mtuci.antivirus.configs;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import ru.mtuci.antivirus.filters.RequestLoggingFilter;
//
//@Configuration
//public class FilterConfig {
//
//    @Bean
//    public FilterRegistrationBean<RequestLoggingFilter> loggingFilter() {
//        FilterRegistrationBean<RequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new RequestLoggingFilter());
//        registrationBean.addUrlPatterns("/auth/register");
//        return registrationBean;
//    }
//}