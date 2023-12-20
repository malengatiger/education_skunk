package com.boha.skunk.filters;


import com.boha.skunk.util.E;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


@Component
@Profile("prod")
public class RequestThrottleFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestThrottleFilter.class);

    //or whatever you want it to be
    @Value("${requestsPerSecond}")
    private int maxRequestsPerSecond;
    private final Cache<Object, Object> requestCountsPerIpAddress;

    public RequestThrottleFilter(){
        super();
        requestCountsPerIpAddress = Caffeine.newBuilder().
                expireAfterWrite(1, TimeUnit.SECONDS).build();
        LOGGER.info(E.PANDA+E.PANDA+ "constructor for RequestThrottleFilter");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info(E.PANDA+E.PANDA+E.PANDA+ "init for RequestThrottleFilter: " + filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String url = httpServletRequest.getRequestURL().toString();
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String clientIpAddress = getClientIP((HttpServletRequest) servletRequest);
        if(isMaximumRequestsPerSecondExceeded(clientIpAddress)){
            LOGGER.info(E.RED_DOT+E.RED_DOT+E.RED_DOT+E.RED_DOT+E.RED_DOT+E.RED_DOT+ " User request quota exceeded for IpAddress: " + clientIpAddress);
            httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpServletResponse.getWriter().write("User request quota exceeded. Too many requests");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isMaximumRequestsPerSecondExceeded(String clientIpAddress){
        Integer requests = 0;
        Function<? super Object, ?> myFunction = (Function<Object, Object>) o -> 0;
        requests = (Integer) requestCountsPerIpAddress.get(clientIpAddress, myFunction);
        if(requests != null){
            if(requests > maxRequestsPerSecond) {
                requestCountsPerIpAddress.asMap().remove(clientIpAddress);
                requestCountsPerIpAddress.put(clientIpAddress, requests);
                return true;
            }

        } else {
            requests = 0;
        }
        requests++;

        requestCountsPerIpAddress.put(clientIpAddress, requests);
        return false;
    }

    public String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0]; // voor als ie achter een proxy zit
    }

    @Override
    public void destroy() {

    }
}
