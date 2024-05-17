package tqs.myvet.RepositoryTests;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqs.myvet.entities.User;
import tqs.myvet.repositories.UserRepository;

import java.util.Arrays;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void whenFindById_thenReturnUser() {
        User user = new User();
        user.setName("Antony");
        user.setEmail("user@email.com");
        user.setPhone(123456789);
        user.setPassword("password");
        user.setRoles(Arrays.asList("USER"));

        entityManager.persistAndFlush(user);

        User found = userRepository.findById(user.getId()).get();

        assertThat(found.getName()).isEqualTo(user.getName());
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        User fromDb = userRepository.findById(-11L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void givenSetOfUsers_whenFindAll_thenReturnAllUsers() {
        User user1 = new User();
        user1.setName("Antony");
        user1.setEmail("antony@mail.com");
        user1.setPhone(123456789);
        user1.setPassword("password");
        user1.setRoles(Arrays.asList("USER"));

        User user2 = new User();
        user2.setName("Antony");
        user2.setEmail("dada@faf.com");
        user2.setPhone(123456789);
        user2.setPassword("password");
        user2.setRoles(Arrays.asList("USER"));

        User user3 = new User();
        user3.setName("Antony");
        user3.setEmail("dad@gamilc.om");
        user3.setPhone(123456789);
        user3.setPassword("password");
        user3.setRoles(Arrays.asList("DOC"));

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        List<User> allUsers = userRepository.findAll();

        long countUsers = allUsers.stream().filter(user -> user.getRoles().contains("USER")).count();
        long countDocs = allUsers.stream().filter(user -> user.getRoles().contains("DOC")).count();

        assertThat(allUsers).hasSize(3).extracting(User::getName).containsOnly(user1.getName(), user2.getName(), user3.getName());
        assertThat(countUsers).isEqualTo(2);
        assertThat(countDocs).isEqualTo(1);
    }
    
}
