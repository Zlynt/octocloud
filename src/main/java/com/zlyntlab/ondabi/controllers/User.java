package com.zlyntlab.ondabi.controllers;

import com.zlyntlab.ondabi.users.UserNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class User {

    @RequestMapping(value = "/api/auth", method = RequestMethod.GET)
    public String Login(String username, String password) {
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

        return "ok";
    }

    @RequestMapping(value = "/api/auth",method = RequestMethod.POST)
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
;}
