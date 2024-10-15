package com.zlyntlab.octocloud.users;

public class UserAdmin extends User {

    public UserAdmin(String username) throws AdminNotFoundException {
        super(username);

        if(!this.isAdmin()) throw new AdminNotFoundException(username);
    }

}
