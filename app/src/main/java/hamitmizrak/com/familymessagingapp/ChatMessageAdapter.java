package hamitmizrak.com.familymessagingapp;

import java.util.Objects;

//important: Chat Message list connected Class
public class ChatMessageAdapter {
    //Global variable
    private String username;
    private String userMessage;

    //parametreli constructor
    public ChatMessageAdapter(String username, String userMessage) {
        this.username = username;
        this.userMessage = userMessage;
    }

    //ToString
    @Override
    public String toString() {
        return "ChatMessageAdapter{" +
                "username='" + username + '\'' +
                ", userMessage='" + userMessage + '\'' +
                '}';
    }

    // equals Hash Code
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessageAdapter that = (ChatMessageAdapter) o;
        return Objects.equals(username, that.username) && Objects.equals(userMessage, that.userMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, userMessage);
    }

    //getter and setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}
