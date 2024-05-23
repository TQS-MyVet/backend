package tqs.myvet.ServiceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

import tqs.myvet.entities.Pet;
import tqs.myvet.entities.User;
import tqs.myvet.entities.DTO.CreateUserDTO;
import tqs.myvet.entities.DTO.UpdateUserDTO;
import tqs.myvet.repositories.UserRepository;
import tqs.myvet.services.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock(lenient = true)
    private UserRepository userRepository;

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Pet pet;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        pet = new Pet(1L, "Bobi", "Masculino", "15/05/2009", "Cão");
        pet2 = new Pet(2L, "Mimi", "Feminino", "15/05/2010", "Gato");
        user = new User(1L, "José Silva", "jose@gmail.com", "919165004", "batata123", Arrays.asList("USER"),
                Arrays.asList(pet, pet2));

        Mockito.when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        Mockito.when(userRepository.findByName(user.getName())).thenReturn(Arrays.asList(user));
    }

    @Test
    @DisplayName("Test get user by name")
    void testGetUserByName() {
        String name = "José Silva";
        List<User> found = userService.getUsersByName(name);
        assertThat(found).hasSize(1);
        assertEquals(name, found.get(0).getName());
    }

    @Test
    @DisplayName("Test get user details")
    void testGetUserDetails() {
        Long id = 1L;
        Optional<User> found = userService.getUserDetails(id);
        assertThat(found).isPresent();
        assertEquals(id, found.get().getId());
        assertEquals("José Silva", found.get().getName());
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
        assertEquals("Bobi", found.get(0).getName());
        assertEquals("Mimi", found.get(1).getName());
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
        assertEquals("Maria Silva", saved.getName());
        assertEquals("maria@gmail.com", saved.getEmail());
        assertEquals("919165005", saved.getPhone());
        assertEquals(Arrays.asList("USER"), saved.getRoles());
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
        assertEquals("Zezinho Silva", updated.getName());
        assertEquals("ze@gmail.com", updated.getEmail());
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
}