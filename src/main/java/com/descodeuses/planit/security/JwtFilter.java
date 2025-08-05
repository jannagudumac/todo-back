package com.descodeuses.planit.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.descodeuses.planit.service.UserDetailsServiceImpl;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Этот фильтр перехватывает каждый HTTP-запрос, извлекает JWT токен из заголовка Authorization,
 * проверяет его подлинность и устанавливает пользователя как "авторизованного" в системе,
 * если токен действителен.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // Утилита для работы с JWT: извлечение, проверка подписи и срока действия

    @Autowired
    private UserDetailsServiceImpl userService; // Сервис для загрузки данных пользователя по имени (из базы или памяти)

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        //NEW
        String path = request.getServletPath();
        if (path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        } 
        
        //NEW
        // Получаем заголовок Authorization из запроса
        final String authHeader = request.getHeader("Authorization");

        String username = null; // Имя пользователя из JWT
        String jwt = null;      // Сам JWT-токен

        // Проверяем, что заголовок есть и начинается с "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Убираем "Bearer " и оставляем только сам токен
            username = jwtUtil.extractUsername(jwt); // Получаем имя пользователя из токена
        }

        // Если имя пользователя есть и в контексте безопасности ещё никто не авторизован
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Загружаем информацию о пользователе по имени (например, из базы данных)
            UserDetails userDetails = userService.loadUserByUsername(username);

            // Проверяем, что токен действительно принадлежит этому пользователю и ещё не просрочен
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // Создаём объект аутентификации (Spring Security будет считать пользователя "вошедшим в систему")
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Добавляем дополнительные данные из запроса (например, IP, User-Agent)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Устанавливаем объект аутентификации в контекст безопасности
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Передаём запрос дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }

}
