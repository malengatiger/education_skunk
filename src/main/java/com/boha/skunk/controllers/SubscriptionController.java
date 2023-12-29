package com.boha.skunk.controllers;

import com.boha.skunk.data.Pricing;
import com.boha.skunk.data.PricingRepository;
import com.boha.skunk.data.Subscription;
import com.boha.skunk.services.SubscriptionService;
import com.boha.skunk.util.CustomErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/subs")
@RequiredArgsConstructor
public class SubscriptionController {
    static final String mm = " \uD83C\uDF4E \uD83C\uDF4E \uD83C\uDF4E " +
            "SubscriptionController  \uD83C\uDF4E";
    static final Logger logger = Logger.getLogger(SubscriptionController.class.getSimpleName());

    private final SubscriptionService subscriptionService;
    private final PricingRepository pricingRepository;

    @GetMapping("/getSubscriptions")
    public ResponseEntity<List<Subscription>> getSubscriptions() throws Exception {
        List<Subscription> subscriptions = subscriptionService.getSubscriptions();
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @PostMapping("/addSubscription")
    public ResponseEntity<Object> addSubscription(@RequestBody Subscription subscription) throws Exception {
        try {
            Subscription sub = subscriptionService.addSubscription(subscription);
            return new ResponseEntity<>(sub, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CustomErrorResponse(
                    400, e.getMessage(), new Date().toString()));
        }
    }

    @PostMapping("/addPricing")
    public ResponseEntity<Object> addPricing(@RequestBody Pricing pricing) {
        try {
            Pricing addedPricing = subscriptionService.addPricing(pricing);
            return new ResponseEntity<>(addedPricing, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CustomErrorResponse(
                    400, e.getMessage(), new Date().toString()));
        }
    }

    @GetMapping("/getOrganizationSubscriptions")
    public ResponseEntity<List<Subscription>> getOrganizationSubscriptions(
            @RequestParam Long organizationId) throws Exception {
        List<Subscription> subscriptions = subscriptionService.getOrganizationSubscriptions(organizationId);
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @GetMapping("/getPricing")
    public ResponseEntity<List<Pricing>> getPricing() throws Exception {
        List<Pricing> pricing = subscriptionService
                .getPricing();
        return new ResponseEntity<>(pricing, HttpStatus.OK);
    }

}
