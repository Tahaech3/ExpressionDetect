package com.facialanalyse.facialAPI.configs;

import com.facialanalyse.facialAPI.model.LoginRequest;
import com.facialanalyse.facialAPI.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testPublicEndpointAccess() throws Exception {
        mockMvc.perform(get("/api/auth/public/test"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginEndpointAccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser", "testpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUserEndpointAccessWithUserRole() throws Exception {
        mockMvc.perform(get("/api/auth/user/test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUserEndpointAccessWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/auth/user/test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAdminEndpointAccessWithUserRole() throws Exception {
        mockMvc.perform(get("/api/auth/admin/test"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminEndpointAccessWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/auth/admin/test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testExpressionEndpointAccessWithUserRole() throws Exception {
        mockMvc.perform(get("/api/expressions/test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testProductEndpointAccessWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/products/test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUserEndpointAccessWithUserRole_Forbidden() throws Exception {
        mockMvc.perform(get("/api/users/test"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUserEndpointAccessWithAdminRole_Allowed() throws Exception {
        mockMvc.perform(get("/api/users/test"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testRecommendationEndpointAccessWithUserRole() throws Exception {
        mockMvc.perform(get("/api/recommendations/test"))
                .andExpect(status().isOk());
    }

    @Test
    void testUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/api/auth/user/test"))
                .andExpect(status().isUnauthorized());
    }
}