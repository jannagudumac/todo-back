package com.descodeuses.planit.security;

// Importation des annotations et classes nécessaires de Spring
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

// Importation des classes nécessaires pour manipuler des JWT
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component // Indique que cette classe sera un "composant" géré par Spring
public class JwtUtil {

    @Value("${jwt.secret}") // Injecte la valeur de 'jwt.secret' depuis application.properties
    private String secret;

    // Méthode privée pour transformer la chaîne secrète en clé de signature sécurisée
    private Key getSigningKey() {
        // Convertit le texte secret en bytes UTF-8 et le transforme en clé HMAC SHA-256
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Méthode pour générer un token JWT à partir d'un nom d'utilisateur
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
 // Le "subject" du token, ici le nom d'utilisateur
                .setIssuedAt(new Date()) // Date de création du token (maintenant)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expiration dans 1 heure
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Signature avec la clé et l'algorithme
                .compact(); // Génère le token sous forme de chaîne
    }

    // Méthode pour extraire le nom d'utilisateur depuis un token JWT
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Utilise la même clé pour vérifier le token
                .build()
                .parseClaimsJws(token) // Analyse et décode le token
                .getBody()
                .getSubject(); // Récupère le "subject" (nom d'utilisateur)
    }

    // Vérifie que le token est valide : bon utilisateur + pas expiré
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token); // Récupère le nom d'utilisateur du token
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token); // Compare au UserDetails
    }

    // Vérifie si le token est expiré
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Utilise la même clé pour valider
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration(); // Récupère la date d'expiration du token
        return expiration.before(new Date()); // Retourne true si la date d'expiration est dépassée
    }
}