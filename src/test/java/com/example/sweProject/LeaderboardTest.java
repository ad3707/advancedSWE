package com.example.sweProject;

import com.example.sweProject.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SweProjectApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class LeaderboardTest {
    @Autowired
    private MockMvc mvc;

    public static String asJsonString(final Object question) {
        try {
            return new ObjectMapper().writeValueAsString(question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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

    // Tests to see if the client receives the correct top k users
    @Test
    @Transactional
    void getTopKUsersTest() throws Exception {
        // Creates 3 users which different score percents
        User u1 = new User(null, "U1", 10, 5);
        User u2 = new User(null, "U2", 20, 5);
        User u3 = new User(null, "U3", 5, 5);

        mvc.perform(post("/users")
                .header("authorization",
                        getBearerToken())
                .content(asJsonString(u1))
                .contentType("application/json")
                .accept("application/json"));

        mvc.perform(post("/users")
                .header("authorization",
                        getBearerToken())
                .content(asJsonString(u2))
                .contentType("application/json")
                .accept("application/json"));

        mvc.perform(post("/users")
                .header("authorization",
                        getBearerToken())
                .content(asJsonString(u3))
                .contentType("application/json")
                .accept("application/json"));

        mvc.perform(get("/leaderboard/{k}", 3)
                        .header("authorization",
                                getBearerToken())
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("U3"))
                .andExpect(jsonPath("$[1].name").value("U1"))
                .andExpect(jsonPath("$[2].name").value("U2"));
    }

    // Tests to see if K can be greater than the population (assumed yes but result
    // will not be K length)
    @Test
    @Transactional
    void getTopKUsersExceedSizeTest() throws Exception {
        //Creates 3 users which different score percents
        User u1 = new User(null, "U1", 10, 5);
        User u2 = new User(null, "U2", 20, 5);
        User u3 = new User(null, "U3", 5, 5);

        mvc.perform(post("/users")
                .header("authorization",
                        getBearerToken())
                .content(asJsonString(u1))
                .contentType("application/json")
                .accept("application/json"));

        mvc.perform(post("/users")
                .header("authorization",
                        getBearerToken())
                .content(asJsonString(u2))
                .contentType("application/json")
                .accept("application/json"));

        mvc.perform(post("/users")
                .header("authorization",
                        getBearerToken())
                .content(asJsonString(u3))
                .contentType("application/json")
                .accept("application/json"));

        mvc.perform(get("/leaderboard/{k}", 4)
                        .header("authorization",
                                getBearerToken())
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("U3"))
                .andExpect(jsonPath("$[1].name").value("U1"))
                .andExpect(jsonPath("$[2].name").value("U2"));
    }

    @Test
    @Transactional
    void getTopKUsersIsEmpty() throws Exception {
        mvc.perform(get("/leaderboard/{k}", 4)
                        .header("authorization",
                                getBearerToken())
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").doesNotExist());
    }
}
