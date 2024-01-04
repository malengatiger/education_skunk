package com.boha.skunk.data;


import java.util.List;


//@Table(name = "Organization")

public class Organization {
    
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //@Table(mappedBy = "organization")
    List<Subscription> subscriptions;

    //@Table(mappedBy = "organization")
    List<User> users;

    //
    //(name = "country_id")
    private Country country;

    //
    //(name = "city_id")
    private City city;

   //@Column(name = "name")
    String name;

   //@Column(name = "email", columnDefinition = "TEXT")
    String email;
   //@Column(name = "cellphone")
    String cellphone;

    public Organization() {
    }

    public Organization(String name, String email, String cellphone) {
        this.name = name;
        this.email = email;
        this.cellphone = cellphone;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}
