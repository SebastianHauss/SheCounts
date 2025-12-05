package at.technikum.backend.security;

import at.technikum.backend.security.auth.AuthenticationService;
import at.technikum.backend.security.userdetails.SCUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    public JwtAuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();
        System.out.println("🔍 Processing request to: " + path);

        if (path.equals("/api/auth/login") ||
                path.equals("/api/auth/register") ||
                path.equals("/api/auth/test") ||
                path.equals("/api/auth/logout") ||
                path.startsWith("/api/auth/reset-password")) {
            System.out.println("⏭️ Skipping public endpoint");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractToken(request);
            System.out.println("🍪 Token extracted: " + (token != null ? "YES" : "NO"));

            if (token != null) {
                System.out.println("✅ Validating token...");
                UserDetails userDetails = authenticationService.validateToken(token);
                System.out.println("✅ Token valid for user: " + userDetails.getUsername());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                if (userDetails instanceof SCUserDetails) {
                    request.setAttribute("userId", ((SCUserDetails) userDetails).getId());
                }

            } else {
                System.out.println("❌ No token found in request");
            }
        } catch (Exception exception) {
            System.out.println("⚠️ Token validation failed: " + exception.getMessage());
            exception.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        // 1) Try cookie first
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("auth_token".equals(c.getName())) {
                    return c.getValue();
                }
            }
        }

        // 2) Fallback: Authorization header (for API testing)
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}