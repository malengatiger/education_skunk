package com.boha.skunk.filters;

import com.boha.skunk.data.User;
import com.boha.skunk.util.CustomErrorResponse;
import com.boha.skunk.util.E;
import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

@Component
//@Profile("prod")
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final String xx = E.COFFEE + E.COFFEE + E.COFFEE;
    String mm = E.AMP + E.AMP + E.AMP + E.AMP + "MonitorAuthenticationFilter";


    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final Gson G = new GsonBuilder().setPrettyPrinting().create();
//    private final DataService dataService;
//    private final GioSubscriptionService subscriptionService;
    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest httpServletRequest,
                                    @NotNull HttpServletResponse httpServletResponse,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String url = httpServletRequest.getRequestURL().toString();
        if (profile.equalsIgnoreCase("test")) {
            doFilter(httpServletRequest, httpServletResponse, filterChain);
            return;
        }

        if (url.contains("uploadFile")) {   //this is my local machine
            logger.info(E.ANGRY + E.ANGRY + "this request is not subject to authentication: "
                    + E.HAND2 + url);
            doFilter(httpServletRequest, httpServletResponse, filterChain);
            return;
        }
        //allow getCountries
        if (httpServletRequest.getRequestURI().contains("getCountries")
                || httpServletRequest.getRequestURI().contains("addCountry")) {
            logger.info(mm + " contextPath: " + httpServletRequest.getContextPath()
                    + E.AMP + " requestURI: " + httpServletRequest.getRequestURI());
            logger.info(mm + " allowing addCountry and getCountries without authentication, is this OK?");

            doFilter(httpServletRequest, httpServletResponse, filterChain);
            return;
        }
        //allow api-docs
        if (httpServletRequest.getRequestURI().contains("api-docs")) {
            logger.info(mm + " contextPath: " + httpServletRequest.getContextPath()
                    + E.AMP + " requestURI: " + httpServletRequest.getRequestURI() + "\n\n");
            logger.info(mm + " allowing swagger openapi call");

            doFilter(httpServletRequest, httpServletResponse, filterChain);
            return;
        }
        //allow localhost
        if (url.contains("localhost") || url.contains("192.168.86.242")
                || url.contains("192.168.86.230")) {
            logger.info(mm + " contextPath: " + httpServletRequest.getContextPath()
                    + E.AMP + " requestURI: " + httpServletRequest.getRequestURI() + "\n\n");
            logger.info(mm + "  \uD83D\uDE0E \uD83D\uDE0E \uD83D\uDE0E allowing call from localhost or 192.168.86.242");
            doFilter(httpServletRequest, httpServletResponse, filterChain);
            return;
        }

        String m = httpServletRequest.getHeader("Authorization");
        if (m == null) {
            sendError(httpServletResponse, "Authentication token missing or invalid or expired");
            return;
        }
        String token = m.substring(7);
        try {
            ApiFuture<FirebaseToken> future = FirebaseAuth.getInstance().verifyIdTokenAsync(token, true);
            FirebaseToken mToken = future.get();

            if (mToken != null) {
                String userId = mToken.getUid();

//                User user = userRepository.findByFirebaseUserId(userId);
//                if (user == null) {
//                    logger.info("\uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21  user unknown, possible new registration");
//                    doFilter(httpServletRequest, httpServletResponse, filterChain);
//                    return;
//                }
//                boolean subscriptionIsValid = false;
//                Long id = user.getOrganization().getId();
//                logger.info("\uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 organizationId: " + id);
//                if (id != null) {
//                    subscriptionIsValid = subscriptionService.isSubscriptionValid(id);
//                }
//                if (subscriptionIsValid) {
//                    doFilter(httpServletRequest, httpServletResponse, filterChain);
//                } else {
//                    LOGGER.info("\uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 request has been forbidden, subscription invalid");
//                    sendError(httpServletResponse, "request has been forbidden, subscription invalid");
//                }

            } else {
                logger.info("\uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 request has been forbidden, token invalid");
                sendError(httpServletResponse, "request has been forbidden, token invalid");
            }

        } catch (Exception e) {
            String msg = "an Exception happened: \uD83C\uDF4E " + e.getMessage();
            logger.info(E.RED_DOT + E.RED_DOT + E.RED_DOT + E.RED_DOT + E.RED_DOT + E.RED_DOT + E.RED_DOT + " " + msg
                    + "\n failed url: " + httpServletRequest.getRequestURL().toString()+ E.RED_DOT);
            e.printStackTrace();
            sendError(httpServletResponse, e.getMessage());
        }

    }

    private static void sendError(HttpServletResponse httpServletResponse, String message) throws IOException {
        CustomErrorResponse er = new CustomErrorResponse(403, message, new Date().toString());
        httpServletResponse.setStatus(403);
        httpServletResponse.getWriter().write(G.toJson(er));
    }

    private void doFilter(@NotNull HttpServletRequest httpServletRequest,
                          @NotNull HttpServletResponse httpServletResponse,
                          FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(httpServletRequest, httpServletResponse);

        if (httpServletResponse.getStatus() != 200) {
            logger.info(reds + " \n" + httpServletRequest.getRequestURI() + " \uD83D\uDD37 Status Code: "
                    + httpServletResponse.getStatus() + "  \uD83D\uDCA6\uD83D\uDCA6  buffer size: " +
                    httpServletResponse.getBufferSize());
        } else {
            logger.info("\uD83D\uDD37\uD83D\uDD37\uD83D\uDD37\uD83D\uDD37"
                    + httpServletRequest.getRequestURI() + " \uD83D\uDD37 Status Code: "
                    + httpServletResponse.getStatus() + "  \uD83D\uDD37 \uD83D\uDD37 \uD83D\uDD37 ");
        }
    }

    static final String reds = E.RED_DOT + E.RED_DOT + E.RED_DOT + E.RED_DOT + E.RED_DOT + " Bad Response Status:";

    private void print(@NotNull HttpServletRequest httpServletRequest) {
        String url = httpServletRequest.getRequestURL().toString();
        logger.info(E.ANGRY + E.ANGRY + E.ANGRY + E.BELL + "Authenticating this url: " + E.BELL + " " + url);

        System.out.println("\uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 request header parameters ...");
        Enumeration<String> parms = httpServletRequest.getParameterNames();
        while (parms.hasMoreElements()) {
            String m = parms.nextElement();
            logger.info("\uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 parameterName: " + m);

        }
        logger.info("\uD83D\uDE21 \uD83D\uDE21 headers ...");
        Enumeration<String> names = httpServletRequest.getHeaderNames();
        while (names.hasMoreElements()) {
            String m = names.nextElement();
            logger.info("\uD83D\uDE21 \uD83D\uDE21 \uD83D\uDE21 headerName: " + m);
        }
        logger.info("\uD83D\uDC9A \uD83D\uDC9A \uD83D\uDC9A Authorization: "
                + httpServletRequest.getHeader("Authorization") + " \uD83D\uDC9A \uD83D\uDC9A");
    }

}

