package com.green.fefu.sms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Setter;

@Data
public class SmsRequest {
    @JsonIgnore
    private String from;
    @Setter
    private String to;
    @Setter
    private String message;

    // Getters and setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

}