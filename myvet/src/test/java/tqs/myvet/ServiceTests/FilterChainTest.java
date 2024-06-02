package tqs.myvet.ServiceTests;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tqs.myvet.config.JwtAuthenticationFilter;
import tqs.myvet.entities.User;
import tqs.myvet.repositories.UserRepository;
import tqs.myvet.services.JWT.JWTService;
import tqs.myvet.services.User.CustomUserDetailsService;

@ExtendWith(MockitoExtension.class)
class FilterChainTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void testDoFilterInternal_withoutAuthHeader() throws ServletException, IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        Mockito.when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_withAuthHeader() throws ServletException, IOException {
        JWTService realJTWService = new JWTService();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        
        // user mock
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(user.getRoles()).thenReturn(Arrays.asList("ROLE_USER"));
        Mockito.when(user.getEmail()).thenReturn("email");
        
        String token = realJTWService.generateToken(user);
        
        // userdetails mock
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        
        // functions mock
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        Mockito.when(jwtService.extractInfo(token)).thenReturn(realJTWService.extractInfo(token));
        Mockito.when(jwtService.isTokenValid(token)).thenReturn(true);
        Mockito.when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        Mockito.when(customUserDetailsService.loadUserByUsername("email")).thenReturn(userDetails);
        
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }
}
