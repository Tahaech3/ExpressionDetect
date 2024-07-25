package com.facialanalyse.facialAPI.controllers;

import com.facialanalyse.facialAPI.model.LoginRequest;
import com.facialanalyse.facialAPI.model.LoginResponse;
import com.facialanalyse.facialAPI.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.facialanalyse.facialAPI.controller.AuthenticationController;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    public void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPassword");

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("testToken");

        when(authenticationService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\", \"password\":\"testPassword\"}"))
                .andExpect(status().isOk());

        ResponseEntity<LoginResponse> responseEntity = authenticationController.login(loginRequest);
        assertEquals("testToken", responseEntity.getBody().getToken());
    }

    @Test
    public void testLogout() throws Exception {
        doNothing().when(authenticationService).logout();

        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk());

        ResponseEntity<Void> responseEntity = authenticationController.logout();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testPublicEndpoint() throws Exception {
        mockMvc.perform(get("/api/auth/public/test"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUserEndpoint() throws Exception {
        mockMvc.perform(get("/api/auth/user/test"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/auth/admin/test"))
                .andExpect(status().isOk());
    }
}
