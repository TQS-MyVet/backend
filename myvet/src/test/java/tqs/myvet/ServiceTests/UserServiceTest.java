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

import tqs.myvet.entities.Pet;
import tqs.myvet.entities.User;
import tqs.myvet.repositories.UserRepository;
import tqs.myvet.services.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock(lenient = true)
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        Pet pet = new Pet(1L, "Bobi", "Masculino", "15/05/2009", "Cão");
        Pet pet2 = new Pet(2L, "Mimi", "Feminino", "15/05/2010", "Gato");
        User user = new User(1L, "José Silva", "jose@gmail.com", 919165004, "batata123", Arrays.asList("ROLE_USER"),
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
        User user = new User();
        user.setId(2L);
        user.setName("Maria Silva");
        user.setEmail("maria@gmail.com");
        user.setPhone(919165005);
        user.setPassword("batata123");
        user.setRoles(Arrays.asList("ROLE_USER"));

        Mockito.when(userRepository.save(user)).thenReturn(user);

        User saved = userService.createUser(user);

        assertThat(saved).isNotNull();
        assertEquals("Maria Silva", saved.getName());
    }

    @Test
    @DisplayName("Test update user")
    void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Zezinho Silva");
        updatedUser.setEmail("ze@gmail.com");
        updatedUser.setPhone(919165006);
        updatedUser.setPassword("batata123");
        updatedUser.setRoles(Arrays.asList("ROLE_USER"));

        Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User updated = userService.updateUser(1L, updatedUser);

        assertThat(updated).isNotNull();
        assertEquals("Zezinho Silva", updated.getName());
        assertEquals("ze@gmail.com", updated.getEmail());
    }

    @Test
    @DisplayName("Test update user with invalid id")
    void testUpdateUserWithInvalidId() {
        User updatedUser = new User();
        updatedUser.setId(-1L);
        updatedUser.setName("Zezinho Silva");
        updatedUser.setEmail("ze@gmail.com");
        updatedUser.setPhone(919165006);
        updatedUser.setPassword("batata123");
        updatedUser.setRoles(Arrays.asList("ROLE_USER"));

        Mockito.when(userRepository.findById(updatedUser.getId())).thenReturn(Optional.empty());

        User updated = userService.updateUser(updatedUser.getId(), updatedUser);

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