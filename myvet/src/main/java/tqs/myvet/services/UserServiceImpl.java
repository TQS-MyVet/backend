package tqs.myvet.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import tqs.myvet.entities.Pet;
import tqs.myvet.entities.User;
import tqs.myvet.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getUsersByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public Optional<User> getUserDetails(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<Pet> getUserPets(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get().getPets();
        } else {
            return Arrays.asList();
        }
    }

    @Override
    public User createUser(User user) {
        // TODO: generate password, encode it and generate the email
        return userRepository.save(user);
    }

    @Override
    public User updateUser(long id, User updatedUser) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User updated = user.get();

            updated.setName(updatedUser.getName());
            updated.setEmail(updatedUser.getEmail());
            updated.setPhone(updatedUser.getPhone());
            updated.setPassword(updatedUser.getPassword());
            updated.setRoles(updatedUser.getRoles());

            return userRepository.save(updated);
        } else {
            return null;
        }
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

}
