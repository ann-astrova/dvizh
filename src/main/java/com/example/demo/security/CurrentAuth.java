package com.example.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// Утилита: достать authId текущего запроса из Security Context.
// authId туда кладёт Filter (как DemoUserDetails) после проверки JWT.
public final class CurrentAuth {

    private CurrentAuth() {}

    public static String authId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof DemoUserDetails details)) {
            return null;
        }
        return details.getAuthId();
    }
}
