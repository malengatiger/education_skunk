package com.boha.skunk.data;



import java.util.List;


//@Table(name = "states")
public class State {
    
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //
    //(name = "country_id")
    private Country country;

    //@Table(mappedBy = "state")
    private List<City> cities;

   //@Column(name = "name")
    private String name;

   //@Column(name = "latitude")
    private double latitude;

   //@Column(name = "longitude")
    private double longitude;
    //

    public Long getId() {
        return id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
