package com.boha.skunk.services;


import com.boha.skunk.data.*;
import com.boha.skunk.util.Constants;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PricingRepository pricingRepository;
    private final OrganizationRepository organizationRepository;
    private final CountryRepository countryRepository;

    public Pricing addPricing(Pricing pricing) {
        return pricingRepository.save(pricing);
    }


    public void generateFreeSubscriptions() {
        List<Organization> orgs = organizationRepository.findAll();
        for (Organization org : orgs) {
            Subscription gs = new Subscription();
            gs.setSubscriptionType(Constants.FREE_SUBSCRIPTION);
            gs.setDate(DateTime.now().toDateTimeISO().toString());
            gs.setActiveFlag(true);
            gs.setCountry(org.getCountry());
            gs.setOrganization(org);
            subscriptionRepository.save(gs);
        }
    }

    public List<Pricing> generateTestPricing() {
        List<Pricing> pricing = new ArrayList<>();
        List<Country> countries = countryRepository.findAll();
        for (Country country : countries) {
            switch (country.getName()) {
                case "South Africa" -> pricing.add(addPricing(country, 100.00, 899, "ZAR"));
                case "Zimbabwe" -> pricing.add(addPricing(country, 1000.00, 9000, "ZW"));
                case "Namibia" -> pricing.add(addPricing(country, 100.00, 800.00, "NAD"));
                case "Botswana" -> pricing.add(addPricing(country, 200.00, 1999.00, "BWP"));
                default -> pricing.add(addPricing(country, 100.00, 900.00, "ZAR"));
            }
        }
        generateFreeSubscriptions();
        return pricing;
    }

    public Pricing addPricing(Country country, double monthly, double annual, String currency) {
        Pricing p = new Pricing();
        p.setCountry(country);
        p.setAnnualPrice(annual);
        p.setMonthlyPrice(monthly);
        p.setCurrency(currency);
        p.setDate(DateTime.now().toDateTimeISO().toString());

        Pricing mp = pricingRepository.save(p);
        return mp;
    }

    public Subscription addSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }


    public List<Subscription> getSubscriptions() {
        return subscriptionRepository.findAll();

    }
    public List<Pricing> getPricing() {
        return pricingRepository.findAll();

    }
    public List<Subscription> getOrganizationSubscriptions(Long organizationId) {
        return subscriptionRepository.findByOrganizationId(organizationId);

    }

    public boolean isSubscriptionValid(Long organizationId) throws Exception {
        List<Subscription> subscriptions = subscriptionRepository
                .findByOrganizationId(organizationId);
        if (subscriptions.isEmpty()) {
            return false;
        }
        Subscription subscription = subscriptions.get(0);
        if (subscription.isActiveFlag()) {
            return false;
        }
        return checkSubscriptionValid(subscription);
    }

    private boolean checkSubscriptionValid(Subscription subscription) throws Exception {

        boolean isValid = false;
        switch (subscription.getSubscriptionType()) {
            case Constants.FREE_SUBSCRIPTION -> {
                isValid = isFreeSubscriptionValid(subscription);
            }
            case Constants.MONTHLY_SUBSCRIPTION -> {
                isValid = isMonthlySubscriptionValid(subscription);
            }
            case Constants.ANNUAL_SUBSCRIPTION -> {
                isValid = isAnnualSubscriptionValid(subscription);
            }
            case Constants.CORPORATE_SUBSCRIPTION -> {
                isValid = isCorporateSubscriptionValid(subscription);
            }

            default -> {
                return false;
            }
        }

        return isValid;
    }

    public Subscription deActivateSubscription(Long id) throws Exception {
        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(id);
        if (subscriptionOptional.isPresent()) {
            Subscription subscription = subscriptionOptional.get();
            subscription.setActiveFlag(false);
            return subscriptionRepository.save(subscription);
        } else {
            throw new Exception("Subscription does not exist");
        }
    }

    public Subscription activateSubscription(Long id) throws Exception {
        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(id);
        if (subscriptionOptional.isPresent()) {
            Subscription subscription = subscriptionOptional.get();
            subscription.setActiveFlag(true);
            return subscriptionRepository.save(subscription);
        } else {
            throw new Exception("Subscription does not exist");
        }

    }

    private boolean isFreeSubscriptionValid(Subscription subscription) {

        return true;
    }

    private boolean isCorporateSubscriptionValid(Subscription subscription) {

        return true;
    }

    private boolean isMonthlySubscriptionValid(Subscription subscription) {

        return true;
    }

    private boolean isAnnualSubscriptionValid(Subscription subscription) {

        return true;
    }
}
