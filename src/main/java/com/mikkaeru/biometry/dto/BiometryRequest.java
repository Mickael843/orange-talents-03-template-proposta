package com.mikkaeru.biometry.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mikkaeru.biometry.model.Biometry;
import com.mikkaeru.request.card.model.Card;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.UUID;

public class BiometryRequest {

    @NotBlank
    private final String fingerprint;

    @JsonCreator
    public BiometryRequest(@JsonProperty("fingerprint") String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Optional<Biometry> toModel(Card card) {
        boolean isBase64 = Base64.isBase64(fingerprint);

        if (!isBase64) {
            return Optional.empty();
        }

        return Optional.of(new Biometry(this.fingerprint, card, UUID.randomUUID()));
    }
}
