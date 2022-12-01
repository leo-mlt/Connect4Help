package org.template.rest.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;


import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import java.security.Key;
@Model
public class DecodeToken {

    @Inject
    private KeyGenerator keyGenerator;

    private int id;
    private String role;
    private Key key;


    public DecodeToken() {

    }
    public void decodeToken(String authorizationHeader) throws Exception{

        String token = authorizationHeader.substring("Bearer".length()).trim();
        this.key = keyGenerator.generateKey();
        Claims claims= Jwts.parser().setSigningKey(this.key).parseClaimsJws(token).getBody();
        this.setId(claims.get("id", Integer.class));
        this.setRole(claims.get("role", String.class));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
