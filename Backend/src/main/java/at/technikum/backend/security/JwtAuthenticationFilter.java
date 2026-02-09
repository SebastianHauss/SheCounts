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
        String method = request.getMethod();

        if (isPublicEndpoint(path, method)) { 
            log.trace("Skipping authentication for public endpoint: {} {}", method, path);
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

    private boolean isPublicEndpoint(String path, String method) {
        // Auth endpoints - nur POST
        if (path.startsWith("/api/auth/") && "POST".equals(method)) {
            return path.equals("/api/auth/login") ||
                    path.equals("/api/auth/register") ||
                    path.equals("/api/auth/test");
        }

        // Files - nur GET ist öffentlich
        if (path.startsWith("/api/files/") && "GET".equals(method)) {
            return true;
        }

        // Articles/Comments - nur GET
        if ((path.startsWith("/api/articles") || path.startsWith("/api/comments"))
                && "GET".equals(method)) {
            return true;
        }

        // Swagger/Actuator
        return path.startsWith("/actuator/") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs");
    }

    private String extractToken(HttpServletRequest request) {
        // cookie only
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("auth_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // optional header fallback
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}