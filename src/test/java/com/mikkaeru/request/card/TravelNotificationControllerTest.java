package com.mikkaeru.request.card;

import com.mikkaeru.helper.IntegrationHelper;
import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.request.card.dto.NotificationCardRequest;
import com.mikkaeru.request.card.dto.NotificationCardResponse;
import com.mikkaeru.request.card.model.Card;
import com.mikkaeru.request.card.repository.CardRepository;
import feign.FeignException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TravelNotificationControllerTest extends IntegrationHelper {

    @Autowired
    private CardRepository cardRepository;

    @MockBean
    private CardResource cardResource;

    private Proposal proposal;
    private LocalDate endDateTravel;
    private String travelDestination;

    private final String cardId = "5555-6666-7777-8884";
    private static final String ENDPOINT = "/cards/{cardId}/notices";

    @BeforeEach
    void setUp() {
        proposal = new Proposal(
                "Proposta 02",
                "proposta2@gmail.com",
                "83525826150",
                new BigDecimal("4000"),
                "Travessa das castanheiras",
                randomUUID().toString());

        travelDestination = "Minas Gerais";
        endDateTravel = now().plusMonths(2);
    }

    @Test
    @DisplayName("Ao fornecer um json valido deve retornar status 200")
    void GIVEN_ValidJson_MUST_ReturnOK() throws Exception {
        cardRepository.save(new Card(cardId, proposal.getName(), new BigDecimal("4000"), proposal.getProposalCode(), LocalDateTime.now(), null));

        when(cardResource.notify(any(), any(NotificationCardRequest.class)))
                .thenReturn(new NotificationCardResponse("CRIADO"));

        JSONObject json = new JSONObject()
                .put("endDateTravel", endDateTravel)
                .put("travelDestination", travelDestination);

        mockMvc.perform(post(ENDPOINT, proposal.getProposalCode())
                .content(json.toString())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cardResource, times(1)).notify(any(), any(NotificationCardRequest.class));
    }

    @Test
    @DisplayName("Ao fornecer um id invalido deve retornar status 404")
    void GIVEN_InvalidCardId_MUST_ReturnNotFound() throws Exception {
        JSONObject json = new JSONObject()
                .put("endDateTravel", endDateTravel)
                .put("travelDestination", travelDestination);

        mockMvc.perform(post(ENDPOINT, randomUUID().toString())
                .content(json.toString())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar status code 422")
    void MUST_ReturnUnprocessableEntity() throws Exception {
        cardRepository.save(new Card(cardId, proposal.getName(), new BigDecimal("4000"), proposal.getProposalCode(), LocalDateTime.now(), null));

        when(cardResource.notify(any(), any(NotificationCardRequest.class))).thenThrow(FeignException.FeignClientException.class);

        JSONObject json = new JSONObject()
                .put("endDateTravel", endDateTravel)
                .put("travelDestination", travelDestination);

        mockMvc.perform(post(ENDPOINT, proposal.getProposalCode())
                .content(json.toString())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

        verify(cardResource, times(1)).notify(any(), any(NotificationCardRequest.class));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidData")
    @DisplayName("Ao fornecer um json invalido deve retornar status code 400")
    void GIVEN_InvalidJson_MUST_ReturnBadRequest(LocalDate endDateTravel, String travelDestination) throws Exception {
        JSONObject json = new JSONObject()
                .put("endDateTravel", endDateTravel)
                .put("travelDestination", travelDestination);

        mockMvc.perform(post(ENDPOINT, proposal.getProposalCode())
                .content(json.toString())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidData() {
        var validTravelDestination = "Minas Gerais";
        var validEndDateTravel = now().plusMonths(2);

        return Stream.of(
                arguments(of(2001, 9, 25), validTravelDestination),
                arguments(LocalDate.now(), validTravelDestination),
                arguments(null, validTravelDestination),
                arguments(validEndDateTravel, null),
                arguments(validEndDateTravel, "  ")
        );
    }
}