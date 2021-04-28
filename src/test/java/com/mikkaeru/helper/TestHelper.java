package com.mikkaeru.helper;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public abstract class TestHelper {

    protected static Faker faker = new Faker();
}
