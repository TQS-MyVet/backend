package tqs.myvet.ServiceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import tqs.myvet.entities.Pet;
import tqs.myvet.entities.User;
import tqs.myvet.entities.DTO.CreateUserDTO;
import tqs.myvet.entities.DTO.UpdateUserDTO;
import tqs.myvet.repositories.UserRepository;
import tqs.myvet.services.User.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock(lenient = true)
    private UserRepository userRepository;

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Pet pet;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        pet = new Pet(1L, "Bobi", "Masculino", "15/05/2009", "Cão",new User());
        pet2 = new Pet(2L, "Mimi", "Feminino", "15/05/2010", "Gato",new User());
        user = new User(1L, "José Silva", "jose@gmail.com", "919165004", "batata123", Arrays.asList("USER"),
                Arrays.asList(pet, pet2));

        Mockito.when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        Mockito.when(userRepository.findByName(user.getName())).thenReturn(Arrays.asList(user));
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    @Test
    @DisplayName("Test get user by name")
    void testGetUserByName() {
        String name = "José Silva";
        List<User> found = userService.getUsersByName(name);
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("Test get user details")
    void testGetUserDetails() {
        Long id = 1L;
        Optional<User> found = userService.getUserDetails(id);
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(id);
        assertThat(found.get().getName()).isEqualTo("José Silva");
    }

    @Test
    @DisplayName("Test get user details with invalid id")
    void testGetUserDetailsWithInvalidId() {
        Long id = -1L;
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        Optional<User> found = userService.getUserDetails(id);
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Test get the pets of a user")
    void testGetUserPets() {
        Long id = 1L;
        List<Pet> found = userService.getUserPets(id);
        assertThat(found).hasSize(2);
        assertThat(found.get(0).getName()).isEqualTo("Bobi");
        assertThat(found.get(1).getName()).isEqualTo("Mimi");
    }

    @Test
    @DisplayName("Test get the pets of a user with invalid id")
    void testGetUserPetsWithInvalidId() {
        Long id = -1L;
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        List<Pet> found = userService.getUserPets(id);
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Test create user")
    void testCreateUser() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setName("Maria Silva");
        dto.setEmail("maria@gmail.com");
        dto.setPhone("919165005");

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRoles(Arrays.asList("USER"));

        Mockito.doNothing().when(emailSender).send(any(SimpleMailMessage.class));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);

        User saved = userService.createUser(dto);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Maria Silva");
        assertThat(saved.getEmail()).isEqualTo("maria@gmail.com");
        assertThat(saved.getPhone()).isEqualTo("919165005");
        assertThat(saved.getRoles()).containsExactly("USER");
    }

    @Test
    @DisplayName("Test update user")
    void testUpdateUser() {
        UpdateUserDTO updatedUser = new UpdateUserDTO();
        updatedUser.setName("Zezinho Silva");
        updatedUser.setEmail("ze@gmail.com");
        updatedUser.setPhone("919165006");
        updatedUser.setPassword("batata123");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(new User(1L, "Zezinho Silva", "ze@gmail.com",
                "919165006", "batata123", Arrays.asList("USER"), Arrays.asList(pet, pet2)));

        User updated = userService.updateUser(1L, updatedUser);

        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo("Zezinho Silva");
        assertThat(updated.getEmail()).isEqualTo("ze@gmail.com");
    }

    @Test
    @DisplayName("Test update user with invalid id")
    void testUpdateUserWithInvalidId() {
        UpdateUserDTO updatedUser = new UpdateUserDTO();
        updatedUser.setName("Zezinho Silva");
        updatedUser.setEmail("ze@gmail.com");
        updatedUser.setPhone("919165006");
        updatedUser.setPassword("batata123");

        Mockito.when(userRepository.findById(-1L)).thenReturn(Optional.empty());

        User updated = userService.updateUser(-1L, updatedUser);

        assertThat(updated).isNull();

        Mockito.verify(userRepository, Mockito.times(0)).save(any(User.class));
    }

    @Test
    @DisplayName("Test delete user")
    void testDeleteUser() {
        Long id = 1L;
        userService.deleteUser(id);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Test get user by email")
    void testGetUserByEmail() {
        String email = "jose@gmail.com";
        Optional<User> found = userService.getUserByEmail(email);
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(email);
    }
}