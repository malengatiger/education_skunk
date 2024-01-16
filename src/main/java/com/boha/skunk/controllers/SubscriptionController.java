package com.boha.skunk.controllers;

import com.boha.skunk.data.Pricing;
import com.boha.skunk.data.Subscription;
import com.boha.skunk.services.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subs")
//@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/addPricing")
    public ResponseEntity<String> addPricing(@RequestBody Pricing pricing) {
        try {
            String result = subscriptionService.addPricing(pricing);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getPricings")
    public ResponseEntity<List<Pricing>> getPricings(@RequestParam Long countryId) {
        try {
            List<Pricing> pricings = subscriptionService.getPricings(countryId);
            return ResponseEntity.ok(pricings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/addSubscription")
    public ResponseEntity<String> addSubscription(@RequestBody Subscription subscription) {
        try {
            String result = subscriptionService.addSubscription(subscription);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getSubscriptions")
    public ResponseEntity<List<Subscription>> getSubscriptions(@RequestParam Long organizationId) {
        try {
            List<Subscription> subscriptions = subscriptionService.getSubscriptions(organizationId);
            return ResponseEntity.ok(subscriptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/updateSubscription")
    public ResponseEntity<Integer> updateSubscription(@RequestParam Long organizationId,
                                                      @RequestParam boolean isActive) {
        try {
            int result = subscriptionService.updateSubscription(organizationId, isActive);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/addUserToSubscription")
    public ResponseEntity<Subscription> addUserToSubscription(@RequestParam Long userId,
                                                              @RequestParam Long subscriptionId) {
        try {
            Subscription result = subscriptionService.addUserToSubscription(userId, subscriptionId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/checkOrganizationSubscription")
    public ResponseEntity<Boolean> checkOrganizationSubscription(@RequestParam Long organizationId) {
        try {
            boolean result = subscriptionService.checkOrganizationSubscription(organizationId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/updateUserSubscription")
    public ResponseEntity<Integer> updateUserSubscription(@RequestParam Long userId, @RequestParam Long subscriptionId) {
        int result = subscriptionService.updateUserSubscription(userId, subscriptionId);
        return ResponseEntity.ok(result);
    }
}
