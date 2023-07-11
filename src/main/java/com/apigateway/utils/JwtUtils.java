package com.apigateway.utils;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {


    private static final long EXPIRATION_DURATION = 20 * 60 * 60 * 1000; //24 HOURS

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    private String SECRET_KEY = "secret";

   /* public boolean validateAccessToken(String token)
    {
        try
        {
            String email = getSubject(token);
            System.out.println("subject: "+email);
            //Retrive user by username
            UserRegister foundUser =  service.getUser(email);
            if(foundUser.getJwtToken().equals(token))
            {
                Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
                return true;
            }
            else
                return false;
        }
        catch(ExpiredJwtException e)
        {
            LOGGER.error("JWT expired");
        }
        catch(IllegalArgumentException e)
        {
            LOGGER.error("Token is null");
        }
        catch(MalformedJwtException e)
        {
            LOGGER.error("JWT invalid");
        }
        catch(UnsupportedJwtException e)
        {
            LOGGER.error("JWT is not supported");
        }
        catch(SignatureException e)
        {
            LOGGER.error("JWT signature validation failed");
        }
        return false;
    }*/

    public String getSubject(String token)
    {
        return parsClaims(token).getSubject();
    }

    private Claims parsClaims(String token)
    {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean validateToken(String token) {
        final String email = getSubject(token);
        System.out.println("email ==>"+email);
        //return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }catch (Exception e){
         return false;
        }

    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }
}
