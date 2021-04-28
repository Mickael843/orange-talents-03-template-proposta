package com.mikkaeru.utils;

import com.mikkaeru.helper.TestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldEncryptorTest extends TestHelper {

    private final String value = "67524627637";

    @Test
    @DisplayName("Deve codificar um campo com base em uma senha em comum")
    void MUST_EncodeField() {
        String encryptedValue = FieldEncryptor.encode(value);
        assertNotEquals(value, encryptedValue);
    }

    @Test
    @DisplayName("Deve verificar se é o mesmo valor")
    void MUST_VerifyThatItIsSameValue() {
        String encryptedValue = FieldEncryptor.encode(value);
        assertTrue(FieldEncryptor.matchers(value, encryptedValue));
    }
}