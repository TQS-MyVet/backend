package tqs.myvet.services.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import tqs.myvet.entities.Pet;
import tqs.myvet.entities.User;
import tqs.myvet.entities.DTO.CreateUserDTO;
import tqs.myvet.entities.DTO.UpdateUserDTO;
import tqs.myvet.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JavaMailSender emailSender;
    private final PasswordEncoder passwordEncoder;


    @Value("${spring.mail.username}")
    private String fromEmail;

    public UserServiceImpl(UserRepository userRepository, JavaMailSender emailSender, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
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
    public User createUser(CreateUserDTO user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setRoles(Arrays.asList("USER"));

        UUID uuid = UUID.randomUUID();

        String password = uuid.toString().replace("-", "");
        password = password.substring(0, 16);
        
        String hashPassword = passwordEncoder.encode(password);
        newUser.setPassword(hashPassword);

        generateEmail(newUser, password);

        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(long id, UpdateUserDTO updatedUser) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User updated = user.get();

            updated.setName(updatedUser.getName());
            updated.setEmail(updatedUser.getEmail());
            updated.setPhone(updatedUser.getPhone());
            updated.setPassword(updatedUser.getPassword());

            return userRepository.save(updated);
        } else {
            return null;
        }
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    private void generateEmail(User user, String password) {
        String emailContent = "Dear " + user.getName() + ",\n\n"
                + "Welcome to MyVet! We are glad to have you as a new user.\n\n"
                + "Your password is: " + password + "\n\n"
                + "Best regards,\n"
                + "MyVet Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Password of MyVet Account");
        message.setText(emailContent);

        emailSender.send(message);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}