package com.zlyntlab.octocloud.chat;

import com.zlyntlab.octocloud.users.User;
import com.zlyntlab.octocloud.users.UserController;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

//@RestController
//@RequestMapping("/api/msg")
public class ChatHistoryController {

    @RequestMapping(value = "history", method = RequestMethod.GET)
    public ArrayList<ChatMessage> MessageHistory(String group, HttpSession session) {
        if(!UserController.IsSessionStarted(session))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        User user = (User) session.getAttribute("user");
        ChatHistory chatHistory = new ChatHistory(user.getUsername(), group);

        return chatHistory.getMessageHistory();
    }
}
