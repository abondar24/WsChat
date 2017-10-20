package org.abondar.experimental.wschat.server;

public class Message {

    private String sender;
    private String recepient;
    private String message;


    public Message(){}

    public Message(String sender, String recepient, String message) {
        this.sender = sender;
        this.recepient = recepient;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecepient() {
        return recepient;
    }

    public void setRecepient(String recepient) {
        this.recepient = recepient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
