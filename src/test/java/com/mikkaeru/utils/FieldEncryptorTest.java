package com.mikkaeru.utils;

import com.mikkaeru.helper.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FieldEncryptorTest extends TestHelper {

    private final String value = "67524627637";
    private final String decryptionPassword = "teste@gmail.com";

    @Test
    @DisplayName("Deve codificar um campo com base em uma senha em comum")
    void MUST_EncodeField() {
        String encryptedValue = FieldEncryptor.encode(value, decryptionPassword);
        assertNotEquals(value, encryptedValue);
    }

    @Test
    @DisplayName("Deve decodificar um campo com base em uma senha em comum")
    void MUST_DecodeField() {
        String encryptedValue = FieldEncryptor.encode(value, decryptionPassword);
        String decryptedValue = FieldEncryptor.decode(encryptedValue, decryptionPassword);
        assertEquals(value, decryptedValue);
    }
}