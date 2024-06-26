package tqs.myvet.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tqs.myvet.entities.User;
import tqs.myvet.repositories.UserRepository;
import tqs.myvet.services.JWT.JWTService;
import tqs.myvet.services.User.CustomUserDetailsService;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private JWTService jwtService;

    private CustomUserDetailsService customUserDetailsService;

    private UserRepository userRepository;

    @Autowired
    public JwtAuthenticationFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String authHeader = request.getHeader("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;
                }
                String jwt = authHeader.substring(7);
                Claims payload = jwtService.extractInfo(jwt);
                String userId = payload.getSubject();
                Long userIdLong = Long.parseLong(userId);
                User maybeuser = userRepository.findById(userIdLong).orElse(null);

                if (maybeuser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(maybeuser.getEmail());
                    if (userDetails != null && jwtService.isTokenValid(jwt)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(),
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
                filterChain.doFilter(request, response);
                
            }
}
