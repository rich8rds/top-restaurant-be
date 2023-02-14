package com.richards.mealsapp.config.userdetails;

import com.richards.mealsapp.entity.Person;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder
@Setter
@Getter
public class AppUser implements UserDetails {
    private Person person;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return person.getRole().getGrantedAuthorities();
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return person.getIsAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return person.getIsEnabled();
    }
}
