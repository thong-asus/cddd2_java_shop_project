package com.example.duancuahang.Class;

import java.io.Serializable;
import java.util.ArrayList;

public class MessageData implements Serializable {
    private String idSender;
    private String idReceiver;
    private ArrayList<ItemMessage> arrItemMessage;

    @Override
    public String toString() {
        return "MessageData{" +
                "idSender='" + idSender + '\'' +
                ", idReceiver='" + idReceiver + '\'' +
                ", arrItemMessage=" + arrItemMessage +
                '}';
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public ArrayList<ItemMessage> getArrItemMessage() {
        return arrItemMessage;
    }

    public void setArrItemMessage(ArrayList<ItemMessage> arrItemMessage) {
        this.arrItemMessage = arrItemMessage;
    }

    public MessageData() {
    }

    public MessageData(String idSender, String idReceiver, ArrayList<ItemMessage> arrItemMessage) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.arrItemMessage = arrItemMessage;
    }
}
