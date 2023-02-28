package com.richards.mealsapp.enums;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.richards.mealsapp.enums.UserAuthority.*;

public enum UserRole {
    GUEST(Sets.newHashSet(PRODUCT_READ)),
    CUSTOMER(Sets.newHashSet(PRODUCT_READ)),
    ADMIN(Sets.newHashSet(PRODUCT_READ, CUSTOMER_READ)),
    SUPERADMIN(Sets.newHashSet(PRODUCT_READ, PRODUCT_WRITE, CUSTOMER_READ, CUSTOMER_WRITE));

    private final Set<UserAuthority> permissions;

    UserRole(Set<UserAuthority> permissions) {
        this.permissions = permissions;
    }

    public Set<UserAuthority> getPermissions() {
        return permissions;
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        Set<GrantedAuthority> permissions = this.permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority(this.name()));

        return permissions;
    }

}
