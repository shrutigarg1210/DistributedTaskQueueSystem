package DistributedTaskQueueSystem.demo.Config;

import java.util.List;

import DistributedTaskQueueSystem.demo.Repository.UserRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.*;
@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
    
    private final JwUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                   FilterChain filterChain)
                                    throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null && authHeader.startsWith("Bearer ")) {
           filterChain.doFilter(request, response);
           return;
        }
         String token = authHeader.substring(7); // Remove "Bearer "

         if (jwtUtil.isTokenValid(token)) {
            String email = jwtUtil.extractEmail(token);

            userRepository.findByEmail(email).ifPresent(user -> {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                        );
                SecurityContextHolder.getContext().setAuthentication(auth);
            });
        }
       filterChain.doFilter(request, response);
    }
}
