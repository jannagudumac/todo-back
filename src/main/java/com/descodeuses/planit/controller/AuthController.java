package com.descodeuses.planit.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.descodeuses.planit.security.JwtUtil;
import com.descodeuses.planit.service.LogDocumentService;
import com.descodeuses.planit.service.UserService;
import com.descodeuses.planit.dto.AuthRequest;
import com.descodeuses.planit.dto.AuthResponse;
import com.descodeuses.planit.entity.LogDocument;
import com.descodeuses.planit.entity.UtilisateurEntity;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
	private LogDocumentService logService;

    @GetMapping("/hello")
    public ResponseEntity<String> Hello() {

        ArrayList<Integer> liste = new ArrayList<Integer>();
        liste.add(21);
        liste.add(22);
        liste.add(23);

        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        LogDocument entry = new LogDocument();
        entry.setTimestamp(LocalDateTime.now());
        entry.setText("Login called");
        this.logService.addLog(entry);
        try {
            // 1. Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            // 2. Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            // 3. Generate token
            String token = jwtUtil.generateToken(userDetails);

            // 4. Return response
            return ResponseEntity.ok(new AuthResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Authentication error");
        }
    }



    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UtilisateurEntity utilisateurEntity) {
        try {
            UtilisateurEntity registeredUser = userService.registerUser(utilisateurEntity);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("username", registeredUser.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

}

/*
 * 
 package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.AuthRequest;
import com.example.demo.DTO.AuthResponse;
import com.example.demo.model.LogDocument;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.LogDocumentService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

	@Autowired
	private LogDocumentService logService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Map<String, Object> extras = Map.of(
            "request", request
        );
        LogDocument entry = new LogDocument();
        entry.setTimestamp(LocalDateTime.now());
        entry.setText("Login called");
        entry.setExtras(extras);
        this.logService.addLog(entry);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
                
        String token = jwtUtil.generateToken(request.getUsername());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}

 */
