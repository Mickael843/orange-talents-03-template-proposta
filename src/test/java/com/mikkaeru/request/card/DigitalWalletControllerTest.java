package com.mikkaeru.request.card;

import com.mikkaeru.helper.IntegrationHelper;
import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.request.card.dto.AssociateWalletRequest;
import com.mikkaeru.request.card.dto.CardResponse;
import com.mikkaeru.request.card.dto.WalletRequest;
import feign.FeignException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.mikkaeru.request.card.model.ResultWallet.ASSOCIADA;
import static com.mikkaeru.request.card.model.WalletType.PAYPAL;
import static com.mikkaeru.request.card.model.WalletType.SAMSUNG_PAY;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DigitalWalletControllerTest extends IntegrationHelper {

    private static final String ENDPOINT = "/cards/{cardId}/wallets";

    private final String proposalId = "1f58ca39-9ebc-451d-9a99-f079d5b117a7";

    private Proposal proposal;

    @MockBean
    private CardResource cardResource;

    @BeforeEach
    void setUp() {
        proposal = new Proposal(
                "Proposta 02",
                "proposta2@gmail.com",
                "83525826150",
                new BigDecimal("4000"),
                "Travessa das castanheiras",
                proposalId);
    }

    @Test
    @DisplayName("Ao fornecer um json valido deve retornar status code 201")
    void GIVEN_ValidJson_MUST_ReturnCreatedWithLocationHeader() throws Exception {
        var email = faker.name().firstName() + "@gmail.com";

        when(cardResource.getCard(proposalId))
                .thenReturn(new CardResponse("5555-6666-7777-8884", proposal.getName(), new BigDecimal("6000"),
                        proposal.getProposalCode(), null, now(), new ArrayList<>(), new ArrayList<>()));

        when(cardResource.associate(any(), any(AssociateWalletRequest.class)))
                .thenReturn(new AssociateWalletResponse(randomUUID().toString(), ASSOCIADA));

        JSONObject json = new JSONObject()
                .put("wallet", PAYPAL)
                .put("email", email);

        mockMvc.perform(post(ENDPOINT, proposalId)
                .contentType(APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(status().isCreated())
                .andExpect(header().exists(LOCATION));

        verify(cardResource, times(1)).getCard(proposalId);
        verify(cardResource, times(1)).associate(any(), any(AssociateWalletRequest.class));
    }

    @Test
    @DisplayName("Ao fornecer um id invalido deve retornar status code 404")
    void GIVEN_InvalidProposalId_MUST_ReturnNotFound() throws Exception {
        var email = faker.name().firstName() + "@gmail.com";
        var InvalidProposalId = randomUUID().toString();

        when(cardResource.getCard(InvalidProposalId)).thenThrow(FeignException.class);

        JSONObject json = new JSONObject()
                .put("wallet", PAYPAL)
                .put("email", email);

        mockMvc.perform(post(ENDPOINT, InvalidProposalId)
                .contentType(APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(status().isNotFound());

        verify(cardResource, times(1)).getCard(InvalidProposalId);
    }

    @Test
    @DisplayName("Ao fornecer uma carteira existente deve retornar status code 422")
    void GIVEN_ExistentDigitalWallet_MUST_ReturnUnprocessableEntity() throws Exception {
        var email = faker.name().firstName() + "@gmail.com";
        List<WalletRequest> list = List.of(new WalletRequest(randomUUID().toString(), email, SAMSUNG_PAY.getName(), now()));

        when(cardResource.getCard(proposalId))
                .thenReturn(new CardResponse("5555-6666-7777-8884", proposal.getName(), new BigDecimal("6000"),
                        proposal.getProposalCode(), null, now(), new ArrayList<>(), list));

        JSONObject json = new JSONObject()
                .put("email", email)
                .put("wallet", SAMSUNG_PAY);

        mockMvc.perform(post(ENDPOINT, proposalId)
                .contentType(APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(status().isUnprocessableEntity());

        verify(cardResource, times(1)).getCard(proposalId);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidData")
    @DisplayName("Ao fornecer um json invalido deve retornar status code 400")
    void GIVEN_InvalidJson_MUST_ReturnBadRequest(String email, String wallet) throws Exception {
        JSONObject json = new JSONObject()
                .put("wallet", wallet)
                .put("email", email);

        mockMvc.perform(post(ENDPOINT, proposalId)
                .contentType(APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidData() {
        var validEmail = faker.name().firstName() + "@gmail.com";

        return Stream.of(
                arguments("  ", SAMSUNG_PAY.getName()),
                arguments(null, SAMSUNG_PAY.getName()),
                arguments("formatoInvalido", PAYPAL.getName()),
                arguments(validEmail, null),
                arguments(validEmail, "  ")
        );
    }
}