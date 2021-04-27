package com.mikkaeru.utils;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

public abstract class FieldEncryptor {

    private static TextEncryptor textEncryptor;
    private static final String salt = KeyGenerators.string().generateKey();
    private final static String password = "$2a$10$LPLdQX.JoLVx.GQQtdyzGOAnpfCqHihv9SiFe3b3b0Bl46XVs4z8.";

    public static String encode(String value) {
        TextEncryptor encryptor = getTextEncryptor();
        return encryptor.encrypt(value);
    }

    public static String decode(String encryptedValue) {
        TextEncryptor decrypt = getTextEncryptor();
        return decrypt.decrypt(encryptedValue);
    }

    private static TextEncryptor getTextEncryptor() {

        if (textEncryptor == null) {
            textEncryptor = Encryptors.text(password, salt);
            return textEncryptor;
        }

        return textEncryptor;
    }
}
