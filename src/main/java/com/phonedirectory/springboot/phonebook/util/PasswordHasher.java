package com.phonedirectory.springboot.phonebook.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Replace with your plain password
        String encoded = encoder.encode("nawfs20");
        System.out.println("BCrypt hash: " + encoded);
        String encoded1 = encoder.encode("mash@20$");
        System.out.println("BCrypt hash: " + encoded1);
    }
}