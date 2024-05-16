package tqs.myvet.RepositoryTests;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqs.myvet.entities.User;
import tqs.myvet.repositories.UserRepository;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindById_thenReturnUser() {
        User user = new User();
        user.setUsername("user");
        user.setName("Antony");
        user.setEmail("user@email.com");
        user.setPhone(123456789);
        user.setPassword("password");
        user.setRole("USER");

        entityManager.persistAndFlush(user);

        User found = userRepository.findById(user.getId()).get();

        assertThat(found.getUsername()).isEqualTo(user.getUsername());

    }

    @Test
    public void whenInvalidId_thenReturnNull() {
        User fromDb = userRepository.findById(-11L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    public void givenSetOfUsers_whenFindAll_thenReturnAllUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setName("Antony");
        user1.setEmail("antony@mail.com");
        user1.setPhone(123456789);
        user1.setPassword("password");
        user1.setRole("USER");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setName("Antony");
        user2.setEmail("dada@faf.com");
        user2.setPhone(123456789);
        user2.setPassword("password");
        user2.setRole("USER");

        User user3 = new User();
        user3.setUsername("user3");
        user3.setName("Antony");
        user3.setEmail("dad@gamilc.om");
        user3.setPhone(123456789);
        user3.setPassword("password");
        user3.setRole("DOC");

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        List<User> allUsers = userRepository.findAll();

        long countUsers = allUsers.stream().filter(user -> user.getRole().equals("USER")).count();
        long countDocs = allUsers.stream().filter(user -> user.getRole().equals("DOC")).count();

        assertThat(allUsers).hasSize(3).extracting(User::getUsername).containsOnly(user1.getUsername(), user2.getUsername(), user3.getUsername());
        assertThat(countUsers).isEqualTo(2);
        assertThat(countDocs).isEqualTo(1);
    }
    
}
