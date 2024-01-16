package com.boha.skunk.filters;

import com.boha.skunk.services.PricingPlanService;
import com.boha.skunk.util.E;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
//@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final String xx = E.FERN+E.FERN+E.FERN + "AppConfig: ";
    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitInterceptor.class);

    private final PricingPlanService pricingPlanService;

    public RateLimitInterceptor(PricingPlanService pricingPlanService) {
        this.pricingPlanService = pricingPlanService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        LOGGER.info(xx + " preHandle");
        String apiKey = request.getHeader("X-api-key");
        if (apiKey == null || apiKey.isEmpty()) {
            //response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Header: X-api-key");
            //return false;
            apiKey = "Plan1";
        }

        Bucket tokenBucket = pricingPlanService.resolveBucket(apiKey);
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                    "You have exhausted your API Request Quota");

            return false;
        }
    }
}
