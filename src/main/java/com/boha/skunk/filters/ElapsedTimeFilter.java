package com.boha.skunk.filters;

import com.boha.skunk.util.DirectoryUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Logger;

@Component
@WebFilter("/*")
public class ElapsedTimeFilter implements Filter {
    static final String mm = "\uD83C\uDF54\uD83C\uDF54\uD83C\uDF54\uD83C\uDF54" +
            " ElapsedTimeFilter \uD83C\uDF54";
    static final java.util.logging.Logger logger = Logger.getLogger(ElapsedTimeFilter.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();

        filterChain.doFilter(servletRequest, servletResponse);

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String elapsedSeconds = decimalFormat.format(elapsedTime / 1000.0);

        logger.info(mm + " Request URI: " + requestURI
                + " \uD83D\uDD52 Elapsed Time: " + elapsedSeconds
                + " seconds");

        DirectoryUtils.deleteFilesInDirectories();
    }
}
