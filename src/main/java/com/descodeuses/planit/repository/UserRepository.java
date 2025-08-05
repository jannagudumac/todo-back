package com.descodeuses.planit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.descodeuses.planit.entity.UtilisateurEntity;

@Repository
public interface UserRepository extends JpaRepository<UtilisateurEntity, Long> {
    Optional<UtilisateurEntity> findByUsername(String username);
}
