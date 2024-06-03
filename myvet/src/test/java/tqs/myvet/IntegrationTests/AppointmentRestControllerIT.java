package tqs.myvet.IntegrationTests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
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

import tqs.myvet.entities.Appointment;
import tqs.myvet.entities.Pet;
import tqs.myvet.entities.User;
import tqs.myvet.entities.DTO.CreateAppointmentDTO;
import tqs.myvet.repositories.AppointmentRepository;
import tqs.myvet.repositories.PetRepository;
import tqs.myvet.repositories.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class AppointmentRestControllerIT {
    
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private AppointmentRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    private User user = new User(1L, "José Silva", "jose@gmail.com", "919165004", "batata123", List.of("USER"),
    List.of());
    private Pet pet = new Pet(1L, "Rex", "M", "2020-01-01", "Dog", new User());


    LocalDateTime now = LocalDateTime.now();
    LocalDateTime later = now.plusHours(1);

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
        userRepository.deleteAll();
        petRepository.deleteAll();

    }

    @Test
    void whenGetAppointments_thenReturnJsonArray() throws Exception {
        mvc.perform(get("/api/appointments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void whenGetAppointmentById_thenReturnAppointment() throws Exception {
        User test = userRepository.save(user);
        Pet testPet = petRepository.save(pet);

        Appointment appointment = new Appointment(1L, now, later, "Consultation", "Notes","Title", test, testPet);

        Appointment actualapt = repository.save(appointment);

        mvc.perform(get("/api/appointments/" + actualapt.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(actualapt.getId().intValue())))
                .andExpect(jsonPath("$.type", is("Consultation")));
    }

    @Test
    void whenGetAppointmentByInvalidId_thenReturnNotFound() throws Exception {
        mvc.perform(get("/api/appointments/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void whenGetAppointmentsByDate_thenReturnAppointments() throws Exception {
        User test = userRepository.save(user);
        Pet testPet = petRepository.save(pet);
        Appointment appointment = new Appointment(1L, now, later, "Consultation", "Notes","Title", test, testPet);

        repository.save(appointment);

        mvc.perform(get("/api/appointments/date/" + now.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type", is("Consultation")));
    }

    @Test
    void whenGetAppointmentsByType_thenReturnAppointments() throws Exception {
        User test = userRepository.save(user);
        Pet testPet = petRepository.save(pet);
        Appointment appointment = new Appointment(1L, now, later, "Consultation", "Notes","Title", test, testPet);

        repository.save(appointment);

        mvc.perform(get("/api/appointments/type/Consultation")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type", is("Consultation")));
    }

    @Test
    void whenGetAppointmentsByInvalidType_thenReturnNotFound() throws Exception {
        mvc.perform(get("/api/appointments/type/notfound")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void whenCreateAppointment_thenReturnAppointment() throws Exception {
        User test = userRepository.save(user);
        Pet testPet = petRepository.save(pet);

        CreateAppointmentDTO appointment = new CreateAppointmentDTO(now, later, "Consultation", "Notes","Title", test.getId(), testPet.getId());

        mvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(appointment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type", is("Consultation")));
    }

    @Test
    @WithMockUser(roles = "DOCTOR")
    void whenDeleteAppointment_thenReturnOk() throws Exception {
        User test = userRepository.save(user);
        Pet testPet = petRepository.save(pet);
        Appointment appointment = new Appointment(1L, now, later, "Consultation", "Notes","Title", test, testPet);

        repository.save(appointment);

        mvc.perform(delete("/api/appointments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
