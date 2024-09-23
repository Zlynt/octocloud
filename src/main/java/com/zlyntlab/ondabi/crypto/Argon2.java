package com.zlyntlab.ondabi.crypto;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Objects;

public class Argon2 {
    // Settings
    private static final int saltLength     = 16;
    private static final int hashLength     = 128;
    private static final int interactions   = 10;
    private static final int memorySize     = 65536;
    private static final int pararelism  = 2;

    public Argon2(){
        super();
    }

    private byte[] genSalt() {
        byte[] salt = new byte[saltLength];
        new SecureRandom().nextBytes(salt);

        return salt;
    }

    public String hash(String information) {
        byte[] salt = this.genSalt();

        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withParallelism(pararelism)
                .withMemoryAsKB(memorySize)
                .withIterations(interactions);

        Argon2BytesGenerator generate = new Argon2BytesGenerator();
        generate.init(builder.build());
        byte[] result = new byte[hashLength];
        generate.generateBytes(information.getBytes(StandardCharsets.UTF_8), result, 0, result.length);

        String hash = "";
        for(byte i : result){
            hash += String.format("%02X", i);
        }

        return hash;
    }

    public Boolean verify(String information, String hash) {
        String hashedInformation = this.hash(information);

        return hashedInformation.equals(hash);
    }
}
