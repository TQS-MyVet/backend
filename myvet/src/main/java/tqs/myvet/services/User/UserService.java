package tqs.myvet.services.User;

import java.util.List;
import java.util.Optional;

import tqs.myvet.entities.Pet;
import tqs.myvet.entities.User;
import tqs.myvet.entities.DTO.CreateUserDTO;
import tqs.myvet.entities.DTO.UpdateUserDTO;

public interface UserService {

    public List<User> getUsersByName(String name);

    public Optional<User> getUserDetails(Long id);

    public List<Pet> getUserPets(Long id);

    public User createUser(CreateUserDTO user);

    public User updateUser(long id, UpdateUserDTO updatedUser);

    public void deleteUser(long id);

    public List<User> getAllUsers();

    public Optional<User> getUserByEmail(String email);

    public User addPetToUser(Long userId, Pet pet);
}