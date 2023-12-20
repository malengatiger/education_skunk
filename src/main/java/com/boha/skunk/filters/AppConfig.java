package com.boha.skunk.filters;


import com.boha.skunk.util.E;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    private static final String xx = E.FERN+E.FERN+E.FERN + "AppConfig: ";
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    private final RateLimitInterceptor interceptor;

    public AppConfig(RateLimitInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LOGGER.info(xx + " addInterceptors");
        registry.addInterceptor(interceptor)
                .addPathPatterns("/geo/v1/**");
    }
}

