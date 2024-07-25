package com.facialanalyse.facialAPI.configs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    public void testPublicEndpoint() throws Exception {
        mockMvc.perform(get("/auth/welcome"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUserEndpointWithUserRole() throws Exception {
        mockMvc.perform(get("/auth/user/userProfile"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUserEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/auth/user/userProfile"))
                .andExpect(status().is3xxRedirection()); // Expecting a redirect status
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAdminEndpointWithAdminRole() throws Exception {
        mockMvc.perform(get("/auth/admin/adminProfile"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAdminEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/auth/admin/adminProfile"))
                .andExpect(status().is3xxRedirection()); // Expecting a redirect status
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testProductEndpointWithUserRole() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    @Test
    public void testProductEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().is3xxRedirection()); // Expecting a redirect status
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testExpressionEndpointWithUserRole() throws Exception {
        mockMvc.perform(get("/api/expressions"))
                .andExpect(status().isOk());
    }

    @Test
    public void testExpressionEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/expressions"))
                .andExpect(status().is3xxRedirection()); // Expecting a redirect status
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testRecommendationEndpointWithUserRole() throws Exception {
        mockMvc.perform(get("/api/recommendations"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRecommendationEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/recommendations"))
                .andExpect(status().is3xxRedirection()); // Expecting a redirect status
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void givenAdminUser_whenGetUsers_thenStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void givenRegularUser_whenGetUsers_thenStatus403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void givenNoAuth_whenGetUsers_thenStatus302() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }
}

