package com.lulobank.reporting.config;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.authorization.AuthorizationContext;

import java.util.Objects;

public class ReactiveWebSecurity {

    public static ReactiveAuthorizationManager<AuthorizationContext> checkIdClient() {
        return (authentication, context) ->
                authentication
                        .map(auth -> (Jwt) auth.getPrincipal())
                        .map(principal -> new AuthorizationDecision(Objects.equals(principal.getSubject(),
                                context.getVariables().get("idClient"))));
    }
}