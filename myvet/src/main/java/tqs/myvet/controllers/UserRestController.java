package tqs.myvet.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import tqs.myvet.entities.Pet;
import tqs.myvet.entities.User;
import tqs.myvet.entities.DTO.AuthenticationRequestDTO;
import tqs.myvet.entities.DTO.AuthenticationResponse;
import tqs.myvet.entities.DTO.CreateUserDTO;
import tqs.myvet.entities.DTO.UpdateUserDTO;
import tqs.myvet.services.JWT.JWTService;
import tqs.myvet.services.User.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public UserRestController(UserService userService, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/name")
    public ResponseEntity<List<User>> getUsersByName(@RequestParam String name) {
        List<User> users = userService.getUsersByName(name);
        if (users == null || users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found with the name: " + name);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserDetails(@PathVariable Long id) {
        Optional<User> user = userService.getUserDetails(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/{id}/pets")
    public ResponseEntity<List<Pet>> getUserPets(@PathVariable Long id) {
        List<Pet> pets = userService.getUserPets(id);
        if (pets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(pets, HttpStatus.OK);
        }
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDTO dto) {
        User user = userService.createUser(dto);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Authorization")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
        User user = userService.updateUser(id, dto);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticationAndGetToken(@RequestBody AuthenticationRequestDTO request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getEmail(), request.getPassword()));

        if (auth.isAuthenticated()) {
            Optional<User> optionalUser = userService.getUserByEmail(request.getEmail());
            if (!optionalUser.isPresent()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }
            
            User user = optionalUser.get();
            return new ResponseEntity<>(new AuthenticationResponse(jwtService.generateToken(user)), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

}
