package com.mikkaeru.biometry.controller;

import com.mikkaeru.biometry.dto.BiometryRequest;
import com.mikkaeru.biometry.model.Biometry;
import com.mikkaeru.biometry.repositoty.BiometryRepository;
import com.mikkaeru.exception.Problem;
import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.repository.CardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/biometrics")
public class BiometryController {

    private final CardRepository cardRepository;
    private final BiometryRepository biometryRepository;

    public BiometryController(CardRepository cardRepository, BiometryRepository biometryRepository) {
        this.cardRepository = cardRepository;
        this.biometryRepository = biometryRepository;
    }

    @Transactional
    @PostMapping(value = "/{cardCode}")
    public ResponseEntity<?> addBiometry(@PathVariable String cardCode, @RequestBody @Valid BiometryRequest biometryRequest) {

        Optional<Card> cardOptional = cardRepository.findByCardCode(cardCode);

        if (cardOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Biometry> biometryOptional = biometryRequest.toModel(cardOptional.get());

        if (biometryOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new Problem("O fingerprint está em um formato invalido!", BAD_REQUEST.value(), LocalDateTime.now())
            );
        }

        Biometry biometry = biometryRepository.save(biometryOptional.get());

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{code}")
                        .buildAndExpand(biometry.getBiometryCode())
                        .toUri()
        ).build();
    }
}
