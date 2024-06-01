package tqs.myvet.ControllerTests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import tqs.myvet.entities.Pet;
import tqs.myvet.entities.User;
import tqs.myvet.entities.DTO.CreateUserDTO;
import tqs.myvet.entities.DTO.UpdateUserDTO;
import tqs.myvet.repositories.PetRepository;
import tqs.myvet.repositories.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class UserRestControllerIT {

	@Autowired
    private WebApplicationContext context;

	private MockMvc mvc;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PetRepository petRepository;

	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        petRepository.deleteAll();
    }

    @Test
    void whenGetUsersByName_thenReturnUsers() throws Exception {
        User user = new User(1L, "José Silva", "jose@gmail.com", "919165004", "batata123", List.of("USER"),
                List.of());

        repository.save(user);

        mvc.perform(get("/api/users/name")
                .param("name", "José Silva")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(user.getName())));
    }

    @Test
    void whenGetUsersByInvalidName_thenReturnNotFound() throws Exception {
        mvc.perform(get("/api/users/name")
                .param("name", "Maria Silva")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetUserDetails_thenReturnUser() throws Exception {
        User user = new User(null, "José Silva", "jose@gmail.com", "919165004", "batata123", List.of("USER"),
                List.of());

        User savedUser = repository.save(user);

        mvc.perform(get("/api/users/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())));
    }

    @Test
    void whenGetUserDetailsWithInvalidId_thenReturnNotFound() throws Exception {
        mvc.perform(get("/api/users/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetUserPets_thenReturnPets() throws Exception {
        Pet pet1 = new Pet(null, "Rex", "M", "2020-01-01", "Dog");
        Pet pet2 = new Pet(null, "Mimi", "F", "2020-01-01", "Cat");

        petRepository.save(pet1);
        petRepository.save(pet2);

        User user = new User(null, "José Silva", "jose@gmail.com", "919165004", "batata123", List.of("USER"),
                List.of(pet1, pet2));

        User savedUser = repository.save(user);

        mvc.perform(get("/api/users/" + savedUser.getId() + "/pets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(user.getPets().get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(user.getPets().get(1).getName())));
    }

    @Test
    void whenGetUserPetsWithInvalidId_thenReturnNotFound() throws Exception {
        mvc.perform(get("/api/users/-1/pets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenCreateUser_thenReturnUser() throws Exception {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setName("Maria Silva");
        dto.setEmail("maria@gmail.com");
        dto.setPhone("919165005");

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.phone", is(dto.getPhone())))
                .andExpect(jsonPath("$.roles", hasSize(1)));
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenUpdateUser_thenReturnUser() throws Exception {
        User user = new User(null, "José Silva", "jose@gmail.com", "919165004", "batata123", List.of("USER"),
                List.of());

        User savedUser = repository.save(user);

        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setName("Maria Silva");
        dto.setEmail("maria@gmail.com");
        dto.setPhone("919165005");
        dto.setPassword("password");

        mvc.perform(put("/api/users/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(dto.getName())))
                .andExpect(jsonPath("$.email", is(dto.getEmail())))
                .andExpect(jsonPath("$.phone", is(dto.getPhone())))
                .andExpect(jsonPath("$.roles", hasSize(1)));
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenUpdateUserWithInvalidId_thenReturnNotFound() throws Exception {
        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setName("Maria Silva");
        dto.setEmail("maria@gmail.com");
        dto.setPhone("919165005");
        dto.setPassword("password");

        mvc.perform(put("/api/users/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(dto)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenDeleteUser_thenReturnOk() throws Exception {
        mvc.perform(delete("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetAllUsers_thenReturnUsers() throws Exception {
        List<User> users = List.of(new User(1L, "José Silva", "jose@gmail.com", "919165004", "batata123", List.of("USER"),
                List.of()));

        repository.saveAll(users);

        mvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(users.get(0).getName())));
    }
}
