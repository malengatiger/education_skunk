package com.boha.skunk.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class RequestFilter implements Filter {
    static final String mm = "\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35\uD83D\uDD35 " +
            "RequestFilter \uD83D\uDC9C";
    static final Logger logger = Logger.getLogger(RequestFilter.class.getSimpleName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic, if needed
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String ipAddress = getClientIpAddress(request);
        String fullUrl = getFullUrl(request);

        logger.info(mm + "Incoming missile: " + fullUrl);
        logger.info(mm + "IP address: " + ipAddress);

        // Continue the filter chain
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Cleanup logic, if needed
    }


    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        } else {
            // The X-Forwarded-For header can contain multiple IP addresses separated by commas.
            // The client's IP address is typically the first one in the list.
            int commaIndex = ipAddress.indexOf(",");
            if (commaIndex != -1) {
                ipAddress = ipAddress.substring(0, commaIndex);
            }
        }
        return ipAddress;
    }

    private String getFullUrl(HttpServletRequest request) {
        StringBuilder fullUrl = new StringBuilder();
        fullUrl.append(request.getRequestURL());
        String queryString = request.getQueryString();
        if (queryString != null) {
            fullUrl.append("?").append(queryString);
        }
        return fullUrl.toString();
    }

}
