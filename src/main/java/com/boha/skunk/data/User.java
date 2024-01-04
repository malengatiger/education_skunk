package com.boha.skunk.data;



import java.util.List;

public class User {
    
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //
    //(name = "organization_id")
    private Organization organization;

    //@Table(mappedBy = "user")
    List<Subscription> subscriptions;

   //@Column(name = "first_name")
    private String firstName;

   //@Column(name = "last_name")
    private String lastName;

   //@Column(name = "email")
    private String email;

   //@Column(name = "cellphone")
    private String cellphone;

   //@Column(name = "firebase_user_id")
    private String firebaseUserId;

   //@Column(name = "active_flag")
    private boolean activeFlag;
    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getFirebaseUserId() {
        return firebaseUserId;
    }

    public void setFirebaseUserId(String firebaseUserId) {
        this.firebaseUserId = firebaseUserId;
    }

    public boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}
