package tqs.myvet.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tqs.myvet.config.JwtAuthenticationFilter;
import tqs.myvet.entities.User;
import tqs.myvet.repositories.UserRepository;
import tqs.myvet.services.JWT.JWTService;
import tqs.myvet.services.User.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JWTService jwtService;
    
    @Test
    void testGenerateToken() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(user.getRoles()).thenReturn(Arrays.asList("ROLE_USER"));

        String token = jwtService.generateToken(user);

        assertNotNull(token);
    }

    @Test
    void testExtractInfo() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(user.getRoles()).thenReturn(Arrays.asList("ROLE_USER"));

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

        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isTokenValid(token));
    }
}