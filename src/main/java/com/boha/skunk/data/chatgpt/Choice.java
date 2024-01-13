package com.boha.skunk.data.chatgpt;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Choice {

    @SerializedName("finish_reason")
    @Expose
    private String finishReason;
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("message")
    @Expose
    private Message message;

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Choice() {
    }
}
