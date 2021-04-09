package com.mikkaeru.proposal.model;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ProposalTest {

    @ParameterizedTest
    @MethodSource("provideInvalidValues")
    @DisplayName("Ao fornecer dados inválidos na criação de uma proposta deve ser lançada uma exceção")
    void GIVEN_InvalidValues_MUST_ThrowBindException(String name, String email, String document,
                                                     BigDecimal salary, String address, UUID code) {
        try {
            var proposal = new Proposal(name, email, document, salary, address, code);
            fail("O objeto proposal não deve ser construído com campos inválidos!");
        } catch (Exception e) {
            assertFalse(e.getMessage().isEmpty());
        }
    }

    private static Stream<Arguments> provideInvalidValues() {
        Faker faker = new Faker();

        var validDocument = "53529230000109";
        var validCode = UUID.randomUUID();
        var validSalary = new BigDecimal("3500");
        var validName = faker.name().fullName();
        var validAddress = faker.address().stateAbbr();
        var validEmail = faker.name().firstName() + "@gmail.com";

        return Stream.of(
                arguments(null, validEmail, validDocument, validSalary, validAddress, validCode),
                arguments(validName, null, validDocument, validSalary, validAddress, validCode),
                arguments(validName, validEmail, null, validSalary, validAddress, validCode),
                arguments(validName, validEmail, validDocument, null, validAddress, validCode),
                arguments(validName, validEmail, validDocument, validSalary, null, validCode),
                arguments(validName, validEmail, validDocument, validSalary, validAddress, null)
        );
    }
}