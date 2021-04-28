package com.mikkaeru.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class FieldEncryptor {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encode(String value) {
        return encoder.encode(value);
    }

    public static boolean matchers(String rawValue, String encodedValue) {
        return encoder.matches(rawValue, encodedValue);
    }
}
