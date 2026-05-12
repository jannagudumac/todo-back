package com.descodeuses.planit.service;

import com.descodeuses.planit.repository.UserRepository;
import com.descodeuses.planit.entity.UtilisateurEntity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.descodeuses.planit.dto.RegisterRequest;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public UtilisateurEntity registerUser(RegisterRequest request) throws Exception {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new Exception("User already exists.");
        }

        UtilisateurEntity user = new UtilisateurEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // First user to register becomes admin automatically
        boolean hasAdmin = !userRepository.findByRole("admin").isEmpty();
        user.setRole(hasAdmin ? "user" : "admin");

        return userRepository.save(user);
    }

    public List<UtilisateurEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void changePassword(String currentPassword, String newPassword, String username) {
        UtilisateurEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Mot de passe actuel incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void changeEmail(String newEmail, String currentUsername) {
        if (userRepository.findByUsername(newEmail).isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }
        UtilisateurEntity user = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setUsername(newEmail);
        userRepository.save(user);
    }

    public void promoteToAdmin(Long id) {
        UtilisateurEntity user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setRole("admin");
        userRepository.save(user);
    }

    public void demoteToUser(Long id) {
        UtilisateurEntity user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setRole("user");
        userRepository.save(user);
    }
}
