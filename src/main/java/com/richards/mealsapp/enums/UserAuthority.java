package com.richards.mealsapp.enums;

public enum UserAuthority {
    PRODUCT_READ("product:read"),
    PRODUCT_WRITE("product:write"),
    CUSTOMER_READ("customer:read"),
    CUSTOMER_WRITE("customer:write");

    private final String permission;

    UserAuthority(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
