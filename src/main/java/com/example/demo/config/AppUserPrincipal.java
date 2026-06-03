package com.example.demo.config;

import com.example.demo.enums.Role;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// Текущий пользователь в Security Context (id + роль из таблицы user).
public record AppUserPrincipal(UUID userId, Role role) {

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
