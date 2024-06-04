package tqs.myvet.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tqs.myvet.services.User.CustomUserDetailsService;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    private CustomUserDetailsService userService;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public WebSecurityConfiguration(CustomUserDetailsService userService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        String api = "/api/**";
        String doctorRole = "DOCTOR";
        String receptionist = "RECEPTIONIST";
        String[] freeApiEndpoints = {"/api/users/login", "/api/queues"};
        
        http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(request -> {
            request.requestMatchers(freeApiEndpoints).permitAll();
            request.requestMatchers(HttpMethod.POST, "/api/queues/receptionist/**").hasRole(receptionist);
            request.requestMatchers(HttpMethod.POST, "/api/queues/doctor/**").hasAnyRole(doctorRole, receptionist);
            request.requestMatchers(HttpMethod.DELETE, "/api/queues/receptionist").hasRole(receptionist);
            request.requestMatchers(HttpMethod.DELETE, "/api/queues/doctor").hasRole(doctorRole);
            request.requestMatchers(HttpMethod.POST, "/api/appointments").permitAll();
            request.requestMatchers(HttpMethod.PUT, "/api/appointments").permitAll();
            request.requestMatchers(HttpMethod.DELETE, "/api/appointments").permitAll();
            request.requestMatchers(HttpMethod.PUT,"/api/users/**").permitAll();
            request.requestMatchers(HttpMethod.POST, api).hasAnyRole(doctorRole, receptionist);
            request.requestMatchers(HttpMethod.DELETE, api).hasAnyRole(doctorRole, receptionist);
            request.requestMatchers(HttpMethod.PUT, api).hasAnyRole(doctorRole, receptionist);
            request.requestMatchers(HttpMethod.GET, api).hasAnyRole("USER", doctorRole, receptionist);
            request.anyRequest().permitAll();
        }).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(Arrays.asList(provider));
    }


}
