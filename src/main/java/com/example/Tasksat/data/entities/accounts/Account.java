package com.example.Tasksat.data.entities.accounts;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.example.Tasksat.data.entities.accounts.Authority.*;

@Data
public abstract class Account implements UserDetails {
    @Id
    protected String id;

    @Indexed(unique = true)
    protected String login;

    protected String password;

    protected Set<Authority> authorities = new HashSet<>();

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !authorities.contains(LOCKED);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !authorities.contains(LOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !authorities.contains(LOCKED);
    }

    @Override
    public boolean isEnabled() {
        return !authorities.contains(LOCKED);
    }
}
