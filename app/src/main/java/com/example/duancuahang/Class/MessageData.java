package com.example.duancuahang.Class;

import java.io.Serializable;

public class MessageData implements Serializable {
    private String idSender;
    private String contentMessage;
    private String dateTime;
    private boolean statusMessage;

    @Override
    public String toString() {
        return "MessageData{" +
                "idSender='" + idSender + '\'' +
                ", contentMessage='" + contentMessage + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", statusMessage=" + statusMessage +
                '}';
    }

    public MessageData(String idSender, String contentMessage, String dateTime, boolean statusMessage) {
        this.idSender = idSender;
        this.contentMessage = contentMessage;
        this.dateTime = dateTime;
        this.statusMessage = statusMessage;
    }

    public MessageData() {
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
    }
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
