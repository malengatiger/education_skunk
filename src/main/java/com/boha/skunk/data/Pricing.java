package com.boha.skunk.data;



//@Table(name = "pricing")
public class Pricing {
    //@GeneratedValue(strategy = GenerationType.AUTO)
   //@Column(name = "id", nullable = false)
    private Long id;
    //
    //(name = "country_id")
    private Long countryId;
    private String date;
    private String currency;
    private double monthlyPrice;
    private double annualPrice;

    public Long getId() {
        return id;
    }


    public Pricing() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(double monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public double getAnnualPrice() {
        return annualPrice;
    }

    public void setAnnualPrice(double annualPrice) {
        this.annualPrice = annualPrice;
    }
}
