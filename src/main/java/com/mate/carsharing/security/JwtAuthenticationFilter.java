package com.mate.carsharing.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Processes incoming HTTP requests to authenticate users based on a JWT token.
     *
     * Extracts the JWT token from the Authorization header, validates it, and if valid, sets the authentication in the security context for the request. If the token is invalid or missing, the request proceeds unauthenticated or returns a 401 Unauthorized response if a JWT exception occurs.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain to continue processing
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = getToken(request);

        try {
            if (token != null && jwtUtil.isValidToken(token)) {
                String name = jwtUtil.getUserName(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(name);
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);

        } catch (JwtException ex) {
            log.warn("Invalid JWT token: {}", ex.getMessage());
            handleJwtException(response, ex.getMessage());
        }
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     *
     * @param request the HTTP request containing the Authorization header
     * @return the JWT token if present and prefixed with "Bearer "; otherwise, null
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }

    /**
     * Sends a 401 Unauthorized JSON response with a specified error message.
     *
     * @param response the HTTP response to write to
     * @param message the error message to include in the response body
     * @throws IOException if an input or output exception occurs while writing the response
     */
    private void handleJwtException(
            HttpServletResponse response, String message) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        responseBody.put("error", message);

        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
    }
}
