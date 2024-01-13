package com.boha.skunk.data.chatgpt;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.List;

@Generated("jsonschema2pojo")
public class ChatGPTRequest {

    @SerializedName("messages")
    @Expose
    private List<Message> messages;
    @SerializedName("model")
    @Expose
    private String model;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ChatGPTRequest() {
    }
}

