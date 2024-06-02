package tqs.myvet.ServiceTests;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.jsonwebtoken.Claims;
import tqs.myvet.entities.User;
import tqs.myvet.services.JWT.JWTService;
import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(token).isNotNull();
    }

    @Test
    void testExtractInfo() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(user.getRoles()).thenReturn(Arrays.asList("ROLE_USER"));

        String token = jwtService.generateToken(user);

        Claims claims = jwtService.extractInfo(token);

        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.getIssuer()).isEqualTo("myvet");
        assertThat(claims.get("roles")).hasToString(user.getRoles().toString());
    }

    @Test
    void testIsTokenValid() {
        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(user.getRoles()).thenReturn(Arrays.asList("ROLE_USER"));

        String token = jwtService.generateToken(user);

        assertThat(jwtService.isTokenValid(token)).isTrue();
    }
}