package com.centillion.userservice.security.models;

import com.centillion.userservice.models.Role;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.core.GrantedAuthority;

@JsonDeserialize
public class CustomGrantedAuthority implements GrantedAuthority {

    String authority;

    public CustomGrantedAuthority(){}

    public CustomGrantedAuthority(Role role) {
        this.authority = role.getValue();
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
