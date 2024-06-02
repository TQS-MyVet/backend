package tqs.myvet.ServiceTests;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import tqs.myvet.entities.User;
import tqs.myvet.repositories.UserRepository;
import tqs.myvet.services.User.CustomUserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Optional;

class CustomUserDetailsServiceTest {

    UserRepository userRepository = Mockito.mock(UserRepository.class);
    CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService(userRepository);

    @Test
    void loadUserByUsername_success() {
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRoles(Arrays.asList("USER"));

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        assertThat(userDetails.getUsername()).isEqualTo("test@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("password");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().toArray()[0].toString()).hasToString("ROLE_USER");
    }

    @Test
    void loadUserByUsername_userNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("test@example.com");
        });
    }
}