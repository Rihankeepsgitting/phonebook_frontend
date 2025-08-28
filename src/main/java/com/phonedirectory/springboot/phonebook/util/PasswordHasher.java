package com.phonedirectory.springboot.phonebook.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Replace with your plain password
        String encoded = encoder.encode("fati123");
        System.out.println("BCrypt hash: " + encoded);
        String encoded1 = encoder.encode("frk@2031$");
        System.out.println("BCrypt hash: " + encoded1);
    }
}