package com.apigateway.customproviders;

import com.apigateway.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@Component
public class CustomReactiveManager implements ReactiveAuthenticationManager {

    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        //authentication object will have token in the principal
        System.out.println("Inside authenticate");
        String token = (String)authentication.getPrincipal();
        boolean isValidated = jwtUtils.validateToken(token);
        if(isValidated)
            return Mono.just(new UsernamePasswordAuthenticationToken(token, null, null));
        else
        return null;
    }
}
