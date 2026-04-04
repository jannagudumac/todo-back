package com.descodeuses.planit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import com.descodeuses.planit.entity.UtilisateurEntity;
import com.descodeuses.planit.security.JwtFilter;
import com.descodeuses.planit.security.JwtUtil;
import com.descodeuses.planit.service.LogDocumentService;
import com.descodeuses.planit.service.UserService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    @MockBean
    private LogDocumentService logDocumentService;

    @Test
    void signupReturnsCreatedUserPayload() throws Exception {
        UtilisateurEntity user = new UtilisateurEntity();
        user.setUsername("alice");

        when(userService.registerUser(any())).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"username\":\"alice\"," +
                        "\"password\":\"secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.username").value("alice"));
    }

    @Test
    void loginReturnsTokenAndRoleWhenCredentialsAreValid() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "alice",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userDetailsService.loadUserByUsername(eq("alice")))
                .thenReturn(new User("alice", "encoded-password", List.of(new SimpleGrantedAuthority("ROLE_USER"))));
        when(jwtUtil.generateToken(any())).thenReturn("jwt-token");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"username\":\"alice\"," +
                        "\"password\":\"secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    void loginReturnsUnauthorizedWhenCredentialsAreInvalid() throws Exception {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"username\":\"alice\"," +
                        "\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }
}
