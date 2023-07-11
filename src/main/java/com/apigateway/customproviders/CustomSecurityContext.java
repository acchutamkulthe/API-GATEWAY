package com.apigateway.customproviders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomSecurityContext implements ServerSecurityContextRepository {

    @Autowired
    ReactiveAuthenticationManager reactiveAuthenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        throw new UnsupportedOperationException("Not Supported yet ");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        System.out.println("Inside load method ::");
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith("Bearer "))
                .flatMap(authHeader -> {
                    String token = authHeader.substring(7);
                    Authentication auth = new UsernamePasswordAuthenticationToken(token,token);
                    return this.reactiveAuthenticationManager.authenticate(auth)
                            .switchIfEmpty(Mono.error(new Exception("Invalid Authentication Token")))
                            .map(SecurityContextImpl::new);
                });
    }
}
