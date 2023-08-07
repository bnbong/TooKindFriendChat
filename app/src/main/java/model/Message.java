package model;

public class Message {
    public static String SENT_BY_ME = "me";
    public static String SENT_BY_FRIEND = "friend";

    private String sentBy;

    String name;
    String message;
    String time;

    public Message(String name, String message, String time, String sentBy) {
        this.name = name;
        this.message = message;
        this.time = time;
        this.sentBy = sentBy;
    }

    public Message() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }
}
