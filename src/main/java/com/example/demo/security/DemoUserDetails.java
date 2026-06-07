package com.example.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class DemoUserDetails implements UserDetails {
    private final String authId;
    private final String email;

    public DemoUserDetails(String authId, String email) {
        this.authId = authId;
        this.email = email != null ? email : "";
    }

    @Override
    public String getUsername(){
        return authId;
    }

    @Override
    public String getPassword(){
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

    public String getAuthId() {
        return authId;
    }

    public String getEmail() {
        return email;
    }
}
