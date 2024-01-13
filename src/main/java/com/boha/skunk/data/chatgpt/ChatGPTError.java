package com.boha.skunk.data.chatgpt;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class ChatGPTError {

    @SerializedName("error")
    @Expose
    private java.lang.Error error;

    public java.lang.Error getError() {
        return error;
    }

    public void setError(java.lang.Error error) {
        this.error = error;
    }

    public ChatGPTError() {
    }
}
