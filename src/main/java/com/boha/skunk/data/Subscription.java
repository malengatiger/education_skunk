package com.boha.skunk.data;



//@Table(name = "subscriptions")
public class Subscription {
    
    //@GeneratedValue(strategy = GenerationType.AUTO)
   //@Column(name = "id", nullable = false)
    private Long id;

    //
    //(name = "organization_id")
    private Organization organization;
    //
    //(name = "pricing_id")
    private Pricing pricing;
    private String date;
    private int subscriptionType;
    //
    //(name = "user_id")
    private User user;

    //
    //(name = "country_id")
    private Country country;
    private boolean activeFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pricing getPricing() {
        return pricing;
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(int subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }
}
