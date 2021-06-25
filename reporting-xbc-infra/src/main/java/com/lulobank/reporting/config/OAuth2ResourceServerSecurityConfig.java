package com.lulobank.reporting.config;

import com.lulobank.core.security.spring.LuloBankClaimsSetVerifier;
import com.lulobank.core.security.spring.LuloBankJwtDecoder;
import com.lulobank.reporting.config.wrapper.ReactiveJwtDecodeWrapper;
import com.lulobank.reporting.kernel.exception.UnauthorizedException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class OAuth2ResourceServerSecurityConfig {
    private static final String UNKNOWN_TENANT_MESSAGE = "Unknown tenant";
    private static final String ERROR_PARSING_MESSAGE = "Error parsing JWT";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_HEADER_PRIMER = "Bearer ";

    @Value("${spring.security.oauth2.resourceserver.jwt.tenantAWS.jwk-set-uri}")
    private String jwkSetUri;
    @Value("${spring.security.oauth2.resourceserver.jwt.tenantAWS.issuer}")
    private String issuerAWS;
    @Value("${spring.security.oauth2.resourceserver.jwt.tenantLulo.private-key-value}")
    private RSAPrivateKey privateKey;
    @Value("${spring.security.oauth2.resourceserver.jwt.tenantLulo.public-key-value}")
    private RSAPublicKey publicKey;
    @Value("${spring.security.oauth2.resourceserver.jwt.tenantLulo.issuer:https://lulobank.com.co}")
    private String issuerLulo;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public SecurityWebFilterChain security(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .authorizeExchange()
                .pathMatchers("/swagger-ui/**","/swagger-resources/**","/webjars/springfox-swagger-ui/**","/**/api-docs")
                .permitAll()
                .pathMatchers("/actuator/**", "/css/**", "/img/**").permitAll()
                .pathMatchers("/client/{idClient}/**").access(this::checkClientId)
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .authenticationManagerResolver(multitenantAuthenticationManager()))
                .build();
    }
    private Mono<AuthorizationDecision> checkClientId(Mono<Authentication> authentication, AuthorizationContext context) {

        return authentication
                .map(Authentication::getPrincipal)
                .map(principal -> (Jwt) principal)
                .map(claim -> claim.getClaim("sub"))
                .map(idClient -> idClient.equals(context.getVariables().get("idClient")))
                .map(AuthorizationDecision::new);
    }

    @Bean
    public ReactiveAuthenticationManagerResolver<ServerHttpRequest> multitenantAuthenticationManager() {
        Map<String, Mono<ReactiveAuthenticationManager>> authenticationManagers = new HashMap<>();
        authenticationManagers.put(this.issuerAWS, tenantAWS());
        authenticationManagers.put(issuerLulo, tenantLulo());
        return request -> {
            HttpHeaders header = request.getHeaders();
            String token = Option.of(header.getFirst(AUTHORIZATION_HEADER)).getOrElse("").replace(AUTHORIZATION_HEADER_PRIMER,
                    "");

            Option<String> tenantId = Try.of(() -> JWTParser.parse(token).getJWTClaimsSet())
                    .onFailure(e -> logger.error(ERROR_PARSING_MESSAGE, e))
                    .map(jwtClaimsSet -> Option.of(jwtClaimsSet)
                            .filter(claims -> Objects.nonNull(claims.getIssuer()))
                            .map(JWTClaimsSet::getIssuer)
                            .getOrElse(() -> issuerLulo))
                    .toOption();

            return tenantId
                    .flatMap(tenant -> Option.of(authenticationManagers.get(tenant)))
                    .getOrElseThrow(() -> new UnauthorizedException(UNKNOWN_TENANT_MESSAGE));
        };
    }

    private Mono<ReactiveAuthenticationManager> tenantAWS() {
        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
        JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager = new JwtReactiveAuthenticationManager(jwtDecoder);
        jwtReactiveAuthenticationManager.setJwtAuthenticationConverter(new ReactiveJwtAuthenticationConverter());
        return Mono.just(jwtReactiveAuthenticationManager);
    }

    private Mono<ReactiveAuthenticationManager> tenantLulo() {
        LuloBankJwtDecoder jwtDecoder = LuloBankJwtDecoder.withEncryptionKey(this.privateKey, this.publicKey)
                .build(new LuloBankClaimsSetVerifier());
        ReactiveJwtDecodeWrapper reactiveJwtDecodeWrapper = new ReactiveJwtDecodeWrapper(jwtDecoder);
        JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager =
                new JwtReactiveAuthenticationManager(reactiveJwtDecodeWrapper);
        jwtReactiveAuthenticationManager.setJwtAuthenticationConverter(new ReactiveJwtAuthenticationConverter());
        return Mono.just(jwtReactiveAuthenticationManager);
    }
}

