package com.example.sweProject;

import com.example.sweProject.entities.User;
import com.example.sweProject.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SweProjectApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserIntTest {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MockMvc mvc;

    public static String getBearerToken() {
        HttpResponse<String> response = Unirest.post(
                        "https://dev-lb0aibabfhuc6e6j.us.auth0.com/oauth/token")
                .header("content-type", "application/json")
                .body("{\"client_id\":\"LsVAxRmvrm8yxktqXOzdDWWn6mlAxd6P\",\"client_secret\":\"DJeBImCv2Mi6Qbe3_m2mYPwAHSkuJO_YoXm_XlnWRg1B0myVdS4BPhO1BeaeCa3I\",\"audience\":\"localhost:8080\",\"grant_type\":\"client_credentials\"}")
                .asString();

        String responseBody = response.getBody();

        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            String accessToken = jsonResponse.getString("access_token");
            return "Bearer " + accessToken;
        } catch (JSONException err) {
            return null;
        }
    }

    public static String asJsonString(final Object question) {
        try {
            return new ObjectMapper().writeValueAsString(question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Transactional
    void testUserIsStored() throws Exception {
        User u = new User(null, "User2", 5, 3);

        mvc.perform(post("/users")
                .content(asJsonString(u))
                .contentType("application/json")
                .header("authorization",
                        getBearerToken())
                .accept("application/json"));

        assertThat(userRepo.findAll()).hasSize(1);
    }

    @Test
    @Transactional
    void testAllUsersAreRetrieved() throws Exception {
        User u = new User(null, "User1", 5, 2);

        mvc.perform(post("/users")
                .content(asJsonString(u))
                .contentType("application/json")
                .header("authorization",
                        getBearerToken())
                .accept("application/json"));

        mvc.perform(get("/users")
                        .header("authorization",
                                getBearerToken())
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("User1"))
                .andExpect(jsonPath("$[0].attempted", is(5)))
                .andExpect(jsonPath("$[0].correct", is(2)));
    }

    @Test
    @Transactional
    void testUserIsDeleted() throws Exception {
        User u = new User(null, "User2", 5, 3);

        MvcResult response = mvc.perform(post("/users")
                .content(asJsonString(u))
                .contentType("application/json")
                .header("authorization",
                        getBearerToken())
                .accept("application/json")).andReturn();

        int id = JsonPath.read(
                response.getResponse().getContentAsString(),
                "$.id");

        mvc.perform(delete("/users/{id}", id)
                        .header("authorization",
                                getBearerToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("User2"))
                .andExpect(jsonPath("$.attempted").value(5))
                .andExpect(jsonPath("$.correct").value(3));

        assertThat(userRepo.findAll()).hasSize(0);
    }
}
