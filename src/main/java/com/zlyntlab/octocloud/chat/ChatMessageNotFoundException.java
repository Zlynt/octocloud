package com.zlyntlab.octocloud.chat;

public class ChatMessageNotFoundException extends RuntimeException {

    public ChatMessageNotFoundException(){
        super("Could not find the chat message");
    }
}
