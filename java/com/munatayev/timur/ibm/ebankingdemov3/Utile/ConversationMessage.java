package com.munatayev.timur.ibm.ebankingdemov3.Utile;

import java.util.Map;

public class ConversationMessage {

    private String message;
    private String command;
    private Map<String, Object> context;

    public ConversationMessage() {}

    public ConversationMessage(Map<String,Object> context, String command) {
        this.message = "no";
        this.command = command;
        this.context = context;
    }

    public ConversationMessage(String message, Map<String, Object> context) {
        this.message = message;
        this.context = context;
        this.command = "no";
    }

    public ConversationMessage(String message, Map<String,Object> context, String command) {
        this.message = message;
        this.context = context;
        this.command = command;
    }

    public boolean isNull() {
        if(command == null) {
            return true;
        } else {
            return false;
        }
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "ConversationMessage \n[message=" + message + "\n context=" + context + " \n command=" + command + "]";
    }

}
