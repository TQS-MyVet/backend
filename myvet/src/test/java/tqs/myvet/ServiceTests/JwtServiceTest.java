package tqs.myvet.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tqs.myvet.config.JwtAuthenticationFilter;
import tqs.myvet.entities.User;
import tqs.myvet.services.JWT.JWTService;

class JwtServiceTest {

    @Test
    void testDoFilterInternal() throws ServletException, IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }
    
    @Test
    void testGenerateToken() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(user.getRoles()).thenReturn(Arrays.asList("ROLE_USER"));

        JWTService jwtService = new JWTService();
        String token = jwtService.generateToken(user);

        assertNotNull(token);
    }

    @Test
    void testExtractInfo() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(user.getRoles()).thenReturn(Arrays.asList("ROLE_USER"));

        JWTService jwtService = new JWTService();
        String token = jwtService.generateToken(user);

        Claims claims = jwtService.extractInfo(token);

        assertEquals("1", claims.getSubject());
        assertEquals("myvet", claims.getIssuer());
        assertEquals(user.getRoles().toString(), claims.get("roles"));
    }

    @Test
    void testIsTokenValid() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(user.getRoles()).thenReturn(Arrays.asList("ROLE_USER"));

        JWTService jwtService = new JWTService();
        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isTokenValid(token));
    }
}