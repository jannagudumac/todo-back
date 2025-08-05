
package com.descodeuses.planit.service;

import com.descodeuses.planit.repository.UserRepository;

import com.descodeuses.planit.entity.UtilisateurEntity; // if DCUser is your entity
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;


@Service
public class UserService  {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    public UtilisateurEntity registerUser(UtilisateurEntity utilisateurEntity) throws Exception{
        if (userRepository.findByUsername(utilisateurEntity.getUsername()).isPresent()) {
            throw new Exception("User already exists.");  // username already exists
        }

        UtilisateurEntity user = new UtilisateurEntity();
        user.setUsername(utilisateurEntity.getUsername());
        user.setPassword(passwordEncoder.encode(utilisateurEntity.getPassword()));
        user.setRole("user");

        userRepository.save(user);
        return user;
    }
}
