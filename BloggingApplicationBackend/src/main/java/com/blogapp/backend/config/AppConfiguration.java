package com.blogapp.backend.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "ASC";

    private AppConfiguration() {

    }

}
