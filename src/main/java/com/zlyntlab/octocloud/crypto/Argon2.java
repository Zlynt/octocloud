package com.zlyntlab.octocloud.crypto;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class Argon2 {
    // Settings
    private static final int saltLength     = 16;
    private static final int hashLength     = 128;
    private static final int interactions   = 10;
    private static final int memorySize     = 65536;
    private static final int pararelism  = 2;

    private Argon2PasswordEncoder encoder;

    public Argon2(){
        super();
        this.encoder = new Argon2PasswordEncoder(
                saltLength, hashLength, pararelism, memorySize, interactions
        );
    }

    public String hash(String information) {
        return this.encoder.encode(information);
    }

    public Boolean verify(String information, String hash) {
        return this.encoder.matches(information, hash);
    }
}
