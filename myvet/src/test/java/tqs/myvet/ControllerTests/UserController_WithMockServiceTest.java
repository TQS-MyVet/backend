package tqs.myvet.ControllerTests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import tqs.myvet.controllers.UserRestController;
import tqs.myvet.entities.Pet;
import tqs.myvet.entities.User;
import tqs.myvet.entities.DTO.AuthenticationRequestDTO;
import tqs.myvet.entities.DTO.CreateUserDTO;
import tqs.myvet.entities.DTO.UpdateUserDTO;
import tqs.myvet.repositories.UserRepository;
import tqs.myvet.services.JWT.JWTService;
import tqs.myvet.services.User.CustomUserDetailsService;
import tqs.myvet.services.User.UserService;

@WebMvcTest(UserRestController.class)
class UserController_WithMockServiceTest {


    @Autowired
    private WebApplicationContext context;
    
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    private List<User> users;

    @BeforeEach
    void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
        users = new ArrayList<>();
        Pet pet1 = new Pet(1L, "Rex", "M", "2020-01-01", "Dog",new User());
        Pet pet2 = new Pet(2L, "Mimi", "F", "2020-01-01", "Cat",new User());
        users.add(new User(1L, "José Silva", "jose@gmail.com", "919165004", "batata123", List.of("USER"),
                List.of(pet1, pet2)));
    }

    @Test
    void whenGetUsersByName_thenReturnUsers() throws Exception {
        when(userService.getUsersByName("José Silva")).thenReturn(users);

        mvc.perform(get("/api/users/name")
                .param("name", "José Silva")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(users.get(0).getName())));

        verify(userService, times(1)).getUsersByName("José Silva");
    }

    @Test
    void whenGetUsersByInvalidName_thenReturnNotFound() throws Exception {
        when(userService.getUsersByName("Maria Silva")).thenReturn(new ArrayList<>());

        mvc.perform(get("/api/users/name")
                .param("name", "Maria Silva")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUsersByName("Maria Silva");
    }

    @Test
    void whenGetUserDetails_thenReturnUser() throws Exception {
        when(userService.getUserDetails(1L)).thenReturn(java.util.Optional.of(users.get(0)));

        mvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(users.get(0).getName())));

        verify(userService, times(1)).getUserDetails(1L);
    }

    @Test
    void whenGetUserDetailsWithInvalidId_thenReturnNotFound() throws Exception {
        when(userService.getUserDetails(3L)).thenReturn(java.util.Optional.empty());

        mvc.perform(get("/api/users/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserDetails(3L);
    }

    @Test
    void whenGetUserPets_thenReturnPets() throws Exception {
        when(userService.getUserPets(1L)).thenReturn(users.get(0).getPets());

        mvc.perform(get("/api/users/1/pets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(users.get(0).getPets().get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(users.get(0).getPets().get(1).getName())));

        verify(userService, times(1)).getUserPets(1L);
    }

    @Test
    void whenGetUserPetsWithInvalidId_thenReturnNotFound() throws Exception {
        when(userService.getUserPets(-1L)).thenReturn(new ArrayList<>());

        mvc.perform(get("/api/users/-1/pets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserPets(-1L);
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenCreateUser_thenReturnUser() throws Exception {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setName("Maria Silva");
        dto.setEmail("maria@gmail.com");
        dto.setPhone("919165005");

        User newUser = new User(2L, dto.getName(), dto.getEmail(), dto.getPhone(),
                "password", List.of("USER"), new ArrayList<>());

        when(userService.createUser(any(CreateUserDTO.class))).thenReturn(newUser);

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.phone", is(dto.getPhone())))
                .andExpect(jsonPath("$.roles", hasSize(1)));

        verify(userService, times(1)).createUser(any(CreateUserDTO.class));
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenUpdateUser_thenReturnUser() throws Exception {
        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setName("Maria Silva");
        dto.setEmail("maria@gmail.com");
        dto.setPhone("919165005");
        dto.setPassword("password");

        User updatedUser = new User(1L, dto.getName(), dto.getEmail(), dto.getPhone(),
                dto.getPassword(), List.of("USER"), new ArrayList<>());

        when(userService.updateUser(anyLong(), any(UpdateUserDTO.class))).thenReturn(updatedUser);

        mvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.phone", is(dto.getPhone())))
                .andExpect(jsonPath("$.roles", hasSize(1)));

        verify(userService, times(1)).updateUser(anyLong(), any(UpdateUserDTO.class));
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenUpdateUserWithInvalidId_thenReturnNotFound() throws Exception {
        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setName("Maria Silva");
        dto.setEmail("maria@gmail.com");
        dto.setPhone("919165005");
        dto.setPassword("password");

        when(userService.updateUser(eq(-1L), any(UpdateUserDTO.class))).thenReturn(null);

        mvc.perform(put("/api/users/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(dto)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUser(eq(-1L), any(UpdateUserDTO.class));
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenDeleteUser_thenReturnOk() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mvc.perform(delete("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(1L);
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenGetAllUsers_thenReturnUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(users);

        mvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(users.get(0).getName())));

        verify(userService, times(1)).getAllUsers();
    }

        @Test
    void authenticationAndGetToken_success() throws Exception {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("test@example.com", "password");
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()))).thenReturn(authentication);
        when(userService.getUserByEmail(request.getEmail())).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(testUser)).thenReturn("testToken");

        mvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bearerToken", is("testToken")));

        verify(authenticationManager, times(1)).authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        verify(userService, times(1)).getUserByEmail(request.getEmail());
        verify(jwtService, times(1)).generateToken(testUser);
    }
}
