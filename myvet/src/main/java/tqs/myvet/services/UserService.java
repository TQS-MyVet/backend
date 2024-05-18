package tqs.myvet.services;

import java.util.List;
import java.util.Optional;

import tqs.myvet.entities.Pet;
import tqs.myvet.entities.User;

public interface UserService {

    public List<User> getUsersByName(String name);

    public Optional<User> getUserDetails(Long id);

    public List<Pet> getUserPets(Long id);

    public User createUser(User user);

    public User updateUser(long id, User updatedUser);

    public void deleteUser(long id);
}