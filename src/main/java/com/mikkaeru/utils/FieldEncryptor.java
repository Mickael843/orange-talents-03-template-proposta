package com.mikkaeru.utils;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

public abstract class FieldEncryptor {

    private static TextEncryptor textEncryptor;
    private static final String salt = KeyGenerators.string().generateKey();

    public static String encode(String value, final String password) {
        TextEncryptor encryptor = getTextEncryptor(password);
        return encryptor.encrypt(value);
    }

    public static String decode(String encryptedValue, final String password) {
        TextEncryptor decrypt = getTextEncryptor(password);
        return decrypt.decrypt(encryptedValue);
    }

    private static TextEncryptor getTextEncryptor(String password) {

        if (textEncryptor == null) {
            textEncryptor = Encryptors.text(password, salt);
            return textEncryptor;
        }

        return textEncryptor;
    }
}
