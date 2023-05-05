package com.example.Tasksat.data.entities.users;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {

    LOCKED, USER, ADMIN, WORKER;

    @Override
    public String getAuthority() {
        return name();
    }
}