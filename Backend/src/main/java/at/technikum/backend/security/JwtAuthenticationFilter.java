package at.technikum.backend.security;

import at.technikum.backend.security.auth.AuthenticationService;
import at.technikum.backend.security.userdetails.SCUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.Cookie;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    public JwtAuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (isPublicEndpoint(path)) {
            log.trace("Skipping authentication for public endpoint: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractToken(request);

            if (token != null) {
                UserDetails userDetails = authenticationService.validateToken(token);
                log.debug("Token validated successfully for user: {}", userDetails.getUsername());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

                if (userDetails instanceof SCUserDetails) {
                    request.setAttribute("userId", ((SCUserDetails) userDetails).getId());
                }
            } else {
                log.debug("No authentication token found in request to: {}", path);
            }
        } catch (Exception exception) {
            log.warn("Token validation failed for path {}: {}", path, exception.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String path) {
        return path.equals("/api/auth/login") ||
                path.equals("/api/auth/register") ||
                path.equals("/api/auth/test") ||
                path.equals("/api/auth/logout") ||
                path.startsWith("/api/auth/reset-password");
    }

    private String extractToken(HttpServletRequest request) {
        // Try cookie first
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("auth_token".equals(cookie.getName())) {
                    log.trace("Token extracted from cookie");
                    return cookie.getValue();
                }
            }
        }

        // Fallback: Authorization header
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            log.trace("Token extracted from Authorization header");
            return bearerToken.substring(7);
        }

        return null;
    }
}