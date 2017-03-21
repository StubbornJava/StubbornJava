package com.stubbornjava.examples.undertow.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

// {{start:user}}
public class User {
    private final String email;
    private final Set<Role> roles;
    private final LocalDate dateCreated;

    public User(
            @JsonProperty("email") String email,
            @JsonProperty("roles") Set<Role> roles,
            @JsonProperty("dateCreated") LocalDate dateCreated) {
        super();
        this.email = email;
        this.roles = roles;
        this.dateCreated = dateCreated;
    }

    public String getEmail() {
        return email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public static enum Role {
        USER, ADMIN
    }

    private static final TypeReference<User> typeRef = new TypeReference<User>() {};
    public static TypeReference<User> typeRef() {
        return typeRef;
    }
    private static final TypeReference<List<User>> listTypeRef = new TypeReference<List<User>>() {};
    public static TypeReference<List<User>> listTypeRef() {
        return listTypeRef;
    }
}
// {{end:user}}