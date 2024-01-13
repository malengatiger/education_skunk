package com.boha.skunk.data.chatgpt;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Usage {

    @SerializedName("completion_tokens")
    @Expose
    private Integer completionTokens;
    @SerializedName("prompt_tokens")
    @Expose
    private Integer promptTokens;
    @SerializedName("total_tokens")
    @Expose
    private Integer totalTokens;

    public Integer getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(Integer completionTokens) {
        this.completionTokens = completionTokens;
    }

    public Integer getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(Integer promptTokens) {
        this.promptTokens = promptTokens;
    }

    public Integer getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(Integer totalTokens) {
        this.totalTokens = totalTokens;
    }

    public Usage() {
    }
}
