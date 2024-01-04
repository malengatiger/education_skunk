package com.boha.skunk.data;


import java.util.List;

/*
model City {
  id            Int             @default(autoincrement())
  name          String
  latitude      Float
  longitude     Float
  position      Json           @default("{}")
  state         State          @relation(fields: [stateId], references: [id])
  stateId       Int
  country       Country        @relation(fields: [countryId], references: [id])
  countryId     Int
  organizations Organization[]
  projects      Project[]

  @@unique([stateId, name])
}
 */

//@Table(name = "City")
public class City {
    
    //@GeneratedValue(strategy = GenerationType.AUTO)
   //@Column(name = "id", nullable = false)
    private Long id;

    String name;
    double latitude;
    double longitude;
    //
    //(name = "state_id")
    State state;
    //
    //(name = "country_id")
    Country country;

    //@Table(mappedBy = "city")
    List<Organization> organizations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
