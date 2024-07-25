package com.facialanalyse.facialAPI.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.facialanalyse.facialAPI.controller.AuthenticationController;
import com.facialanalyse.facialAPI.config.SecurityConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfig.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testWelcome() throws Exception {
        mockMvc.perform(get("/auth/welcome"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to the public endpoint"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testUserProfile_withUserRole() throws Exception {
        mockMvc.perform(get("/auth/user/userProfile"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to User Profile"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUserProfile_withAdminRole() throws Exception {
        mockMvc.perform(get("/auth/user/userProfile"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to User Profile"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testAdminProfile_withUserRole() throws Exception {
        mockMvc.perform(get("/auth/admin/adminProfile"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAdminProfile_withAdminRole() throws Exception {
        mockMvc.perform(get("/auth/admin/adminProfile"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to Admin Profile"));
    }
}