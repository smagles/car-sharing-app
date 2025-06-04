package com.mate.carsharing.security;

import static com.mate.carsharing.exception.message.JwtExceptionMessages.INVALID_TOKEN;
import static com.mate.carsharing.exception.message.JwtExceptionMessages.INVALID_TOKEN_FORMAT;
import static com.mate.carsharing.exception.message.JwtExceptionMessages.TOKEN_EXPIRED;
import static com.mate.carsharing.exception.message.JwtExceptionMessages.TOKEN_VALIDATION_FAILED;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JwtUtil {
    @Value("${jwt.expiration}")
    private long expiration;
    @Value("${jwt.secret}")
    private String secretString;
    private Key secret;

    /**
     * Initializes the cryptographic secret key for JWT operations after dependency injection.
     */
    @PostConstruct
    public void init() {
        this.secret = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token for the specified username with the configured expiration time.
     *
     * @param username the username to set as the token's subject
     * @return a signed JWT token string
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secret)
                .compact();
    }

    /**
     * Checks whether the provided JWT token is valid based on its expiration date.
     *
     * @param token the JWT token to validate
     * @return true if the token has not expired; false otherwise
     */
    public boolean isValidToken(String token) {
        return !getClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Extracts the username from the subject claim of the provided JWT token.
     *
     * @param token the JWT token from which to extract the username
     * @return the username contained in the token's subject claim
     */
    public String getUserName(String token) {
        return getClaim(token, Claims::getSubject);

    }

    /**
     * Extracts a specific claim from a JWT token using the provided function.
     *
     * @param token the JWT token string
     * @param claimsFunction a function to extract a claim from the token's claims
     * @return the extracted claim value
     * @throws JwtException if the token is expired, malformed, unsupported, has an invalid signature, or is otherwise invalid
     */
    private <T> T getClaim(String token, Function<Claims, T> claimsFunction) {
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claimsFunction.apply(claims);
        } catch (ExpiredJwtException e) {
            throw new JwtException(TOKEN_EXPIRED);
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            throw new JwtException(INVALID_TOKEN_FORMAT);
        } catch (SignatureException e) {
            throw new JwtException(TOKEN_VALIDATION_FAILED);
        } catch (IllegalArgumentException e) {
            throw new JwtException(INVALID_TOKEN);
        }
    }
}

