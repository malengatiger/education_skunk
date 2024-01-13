package com.boha.skunk.data.chatgpt;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.annotation.Generated;

@Data
@Generated("jsonschema2pojo")
public class Phone {

    @SerializedName("phone")
    @Expose
    private String phone;
    private String company;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Phone() {
    }
}
