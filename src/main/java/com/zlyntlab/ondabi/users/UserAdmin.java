package com.zlyntlab.ondabi.users;

import com.zlyntlab.ondabi.crypto.Argon2;

import java.sql.SQLException;

public class UserAdmin extends User {

    public UserAdmin(String username) throws AdminNotFoundException {
        super(username);

        if(!this.isAdmin()) throw new AdminNotFoundException(username);
    }

}
