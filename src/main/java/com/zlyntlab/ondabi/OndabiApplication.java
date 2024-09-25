package com.zlyntlab.ondabi;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.io.InputStreamReader;
import java.sql.SQLException;

import com.zlyntlab.ondabi.crypto.Argon2;
import com.zlyntlab.ondabi.database.Database;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.zlyntlab.ondabi.users.User;

@SpringBootApplication
public class OndabiApplication {

	public static void main(String[] args) {
		System.out.println("Welcome to the Grid!");


		SpringApplication app = new SpringApplication(OndabiApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
	}

}
