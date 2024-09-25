package com.zlyntlab.ondabi.controllers;

import com.zlyntlab.ondabi.users.UserNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/user")
public class User {
    @RequestMapping(value = "session", method = RequestMethod.GET)

    private boolean IsSessionStarted(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    public String IsLoggedIn(HttpSession session) {
        return IsSessionStarted(session) ? "ok" : "nok";
    }

    @RequestMapping(value = "session", method = RequestMethod.POST)
    public String Login(String username, String password, HttpSession session) {
        if(username == null || password == null) return "Username em falta!";

        // User exists?
        com.zlyntlab.ondabi.users.User user;
        try {
            user = new com.zlyntlab.ondabi.users.User(username);
        } catch (UserNotFoundException e) {
            return "Wrong username or password";
        }

        // Is the password wrong?
        if (!user.checkPassword(password)) {
            return "Wrong username or password";
        }

        if(session.getAttribute("user") == null)
            session.setAttribute("user", user);

        return "ok";
    }

    @RequestMapping(value = "session", method = RequestMethod.DELETE)
    public String Logout(HttpSession session) {
        session.invalidate();
        return "ok";
    }

    @RequestMapping(value = "create",method = RequestMethod.POST)
    public String Register(String username, String password) {
        if(username == null || password == null) return "Username em falta!";

        try {
            com.zlyntlab.ondabi.users.User user = new com.zlyntlab.ondabi.users.User(username);

            return "User already exists";
        } catch (UserNotFoundException e) {
        }

        try {
            com.zlyntlab.ondabi.users.User.create(username, password);
        } catch (SQLException e) {
            return "Failed to create the new user";
        }

        return "ok";

    }

    @RequestMapping(value = "password", method = RequestMethod.PATCH)
    public String ChangePassword(String oldPass, String newPass, HttpSession session) {
        if(!IsSessionStarted(session)) return "Invalid Session";

        com.zlyntlab.ondabi.users.User user = (com.zlyntlab.ondabi.users.User) session.getAttribute("user");

        if(!user.checkPassword(oldPass)) return "Wrong current password";

        user.setPassword(newPass);

        return "ok";
    }

}
