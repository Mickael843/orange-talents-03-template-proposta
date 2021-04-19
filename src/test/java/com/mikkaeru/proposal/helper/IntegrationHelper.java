package com.mikkaeru.proposal.helper;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public abstract class IntegrationHelper extends TestHelper {

    @Autowired
    protected MockMvc mockMvc;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    @Value("${keycloak.client-id}")
    private String clientId;

    protected HttpHeaders headers = new HttpHeaders();

    protected void addHeaders() throws Exception {
        String url = "http://localhost:8081/auth/realms/"+realm+"/protocol/openid-connect/token";

        JSONObject json = new JSONObject()
                .put("grant_type", "password")
                .put("username", username)
                .put("password", password)
                .put("client_id", clientId)
                .put("client_secret", "1610ed88-3a65-45ab-950a-c856f9ee17d2")
                .put("scope", "openid");

        MvcResult result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(status().isOk())
                .andReturn();

        String authorization = result.getResponse().getContentAsString()
                .split(":")[2]
                .replace("\"", "")
                .replace("}", "");

        headers.add("Authorization", "Bearer " + authorization);
    }
}
