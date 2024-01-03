package com.boha.skunk.data;

import jakarta.persistence.*;

import java.util.List;

@Entity()
@Table(name = "Country",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"name"})
)

public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    String name;
    String countryCode;
    double latitude;
    double longitude;
    String iso2;
    String iso3;
    String numericCode;
    String phoneCode;
    String capital;
    String currencyName;
    String currencySymbol;
    String region;
    String subregion;
    String emoji;
    String emojiU;
    boolean flag;


    @OneToMany(mappedBy = "country")
    List<Subscription> subscriptions;

    @OneToMany(mappedBy = "country")
    List<State> states;

    @OneToMany(mappedBy = "country")
    List<City> cities;

    @OneToMany(mappedBy = "country")
    List<Organization> organizations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
