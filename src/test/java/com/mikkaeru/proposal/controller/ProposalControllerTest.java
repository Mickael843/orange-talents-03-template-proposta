package com.mikkaeru.proposal.controller;

import com.mikkaeru.helper.IntegrationHelper;
import com.mikkaeru.proposal.model.Proposal;
import com.mikkaeru.proposal.repository.ProposalRepository;
import com.mikkaeru.request.card.CardResource;
import com.mikkaeru.request.card.dto.CardRequest;
import com.mikkaeru.request.card.dto.CardResponse;
import com.mikkaeru.request.solicitation.SolicitationReview;
import com.mikkaeru.request.solicitation.dto.ReviewRequest;
import com.mikkaeru.request.solicitation.dto.ReviewResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static com.mikkaeru.request.solicitation.model.SolicitationReviewStatus.SEM_RESTRICAO;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProposalControllerTest extends IntegrationHelper {

    private static final String ENDPOINT = "/proposals";

    @MockBean
    private SolicitationReview solicitationReview;

    @MockBean
    private CardResource cardResource;

    @Autowired
    private ProposalRepository proposalRepository;

    private Proposal proposal;

    private final String document = "12813279307";

    @BeforeEach
    void setUp() {
        proposal = new Proposal(
                "Proposta 01",
                "proposta01@gmail.com",
                document,
                new BigDecimal("4000"),
                "Travessa das castanheiras",
                UUID.randomUUID().toString());

        when(solicitationReview.solicitation(Mockito.any(ReviewRequest.class)))
                .thenReturn(new ReviewResponse(
                        proposal.getDocument(), proposal.getName(), proposal.getProposalCode(), SEM_RESTRICAO.name()
                ));

        when(cardResource.processCard(any(CardRequest.class)))
                .thenReturn(new CardResponse(
                        "5485755481460022", proposal.getName(), new BigDecimal("4000"), proposal.getProposalCode(), null, LocalDateTime.now(), null, null
                ));
    }

    @Test
    @DisplayName("Ao fornecer um id valido deve retornar a proposta")
    void GIVEN_ValidProposalCode_MUST_ReturnProposal() throws Exception {
        proposalRepository.save(proposal);

        mockMvc.perform(get(ENDPOINT+"/{code}", proposal.getProposalCode()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(proposal.getName())))
                .andExpect(jsonPath("$.state", is(proposal.getState())))
                .andExpect(jsonPath("$.email", is(proposal.getEmail())))
                .andExpect(jsonPath("$.code", is(proposal.getProposalCode())));
    }

    @Test
    @DisplayName("Ao fornecer um id invalido deve retornar status code 404")
    void GIVEN_InvalidProposalCode_MUST_ReturnNotFound() throws Exception {
        mockMvc.perform(get(ENDPOINT+"/{code}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Ao fornecer dados validos deve criar uma proposta")
    void GIVEN_ValidData_MUST_ReturnCreatedWithLocationHeader() throws Exception {
        JSONObject json = new JSONObject()
                .put("salary", 4000)
                .put("name", "Proposta 01")
                .put("document", "85888489883")
                .put("email", "proposta01@gmail.com")
                .put("address", "Rua do carinha que mora logo ali");

        mockMvc.perform(post(ENDPOINT)
                .content(json.toString())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists(LOCATION));
    }

    @Test
    @DisplayName("Ao fornecer um documento duplicado deve retornar status code 422")
    void GIVEN_DocumentDuplicated_MUST_ReturnUnprocessableEntity() throws Exception {
        proposalRepository.save(proposal);

        JSONObject json = new JSONObject()
                .put("salary", 6500)
                .put("name", "Proposta 02")
                .put("document", document)
                .put("email", "proposta02@gmail.com")
                .put("address", "Rua do carinha que mora logo ali");

        mockMvc.perform(post(ENDPOINT)
                .content(json.toString())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidData")
    void GIVEN_InvalidData_MUST_ReturnBadRequest(String name, String document, String email, BigDecimal salary, String address) throws Exception {
        JSONObject json = new JSONObject()
                .put("name", name)
                .put("email", email)
                .put("salary", salary)
                .put("address", address)
                .put("document", document);

        mockMvc.perform(post(ENDPOINT)
                .content(json.toString())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> provideInvalidData() {
        var validDocument = "12813279307";
        var validName = faker.name().firstName();
        var validEmail = faker.name().firstName() + "@gmail.com";
        var validAddress = faker.address().secondaryAddress();
        var validSalary = new BigDecimal(faker.number().numberBetween(500, 10600));

        return Stream.of(
                Arguments.arguments(null, validDocument, validEmail, validSalary, validAddress),
                Arguments.arguments("  ", validDocument, validEmail, validSalary, validAddress),
                Arguments.arguments(validName, null, validEmail, validSalary, validAddress),
                Arguments.arguments(validName, "  ", validEmail, validSalary, validAddress),
                Arguments.arguments(validName, "123456789", validEmail, validSalary, validAddress),
                Arguments.arguments(validName, "123456789101112131415", validEmail, validSalary, validAddress),
                Arguments.arguments(validName, validDocument, null, validSalary, validAddress),
                Arguments.arguments(validName, validDocument, "  ", validSalary, validAddress),
                Arguments.arguments(validName, validDocument, "NãoTaNoFormato", validSalary, validAddress),
                Arguments.arguments(validName, validDocument, validEmail, null, validAddress),
                Arguments.arguments(validName, validDocument, validEmail, new BigDecimal(0), validAddress),
                Arguments.arguments(validName, validDocument, validEmail, new BigDecimal(-1), validAddress),
                Arguments.arguments(validName, validDocument, validEmail, validSalary, null),
                Arguments.arguments(validName, validDocument, validEmail, validSalary, "  ")
        );
    }
}