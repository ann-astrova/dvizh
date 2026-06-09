package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class Filter extends OncePerRequestFilter {

    @Autowired
    JwtVerification verification;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("FILTER START");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Method: " + request.getMethod());

        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization header: [" + authorizationHeader + "]");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("No Bearer token, continuing chain");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);
        System.out.println("Token (first 20 chars): " + token.substring(0, Math.min(20, token.length())));

        try{
            String authId = verification.getAuthId(token);
            System.out.println("AuthId: " + authId);
            String email = verification.getEmail(token);
            System.out.println("Email: " + email);

            DemoUserDetails userDetails = new DemoUserDetails(authId, email);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, token, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("Authentication set successfully");
        }
        catch (Exception e){
            System.out.println("ERROR in filter: " + e.getMessage());
            e.printStackTrace();
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().println("{\"error\": \"" + e.getMessage() + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
        System.out.println("FILTER END");
    }

}
