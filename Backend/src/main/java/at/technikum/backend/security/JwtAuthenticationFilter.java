package at.technikum.backend.security;

import at.technikum.backend.security.auth.AuthenticationService;
import at.technikum.backend.security.userdetails.SCUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        if (path.startsWith("/api/auth/")) {
            // Login, Register, Test überspringen
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = extractToken(request);
            if (token != null){
                UserDetails userDetails = authenticationService.validateToken(token);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                if (userDetails instanceof SCUserDetails){
                    request.setAttribute("userId", ((SCUserDetails) userDetails).getId());
                }

            }
        } catch (Exception exception){
            //doesn't throw exception, User is just not authenticated
            System.out.println("Token was invalid");
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

}
