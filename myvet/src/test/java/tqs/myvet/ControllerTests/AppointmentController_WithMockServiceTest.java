package tqs.myvet.ControllerTests;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import tqs.myvet.controllers.AppointmentRestController;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import tqs.myvet.entities.User;
import tqs.myvet.entities.DTO.CreateAppointmentDTO;
import tqs.myvet.repositories.UserRepository;
import tqs.myvet.services.Appointment.AppointmentService;
import tqs.myvet.services.JWT.JWTService;
import tqs.myvet.services.Pet.PetService;
import tqs.myvet.services.User.CustomUserDetailsService;
import tqs.myvet.services.User.UserService;
import tqs.myvet.entities.Appointment;
import tqs.myvet.entities.Pet;

@WebMvcTest(AppointmentRestController.class)
class AppointmentController_WithMockServiceTest {
    
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private PetService petService;
    
    @MockBean
    private UserService userService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private UserRepository userRepository;
    
    private List<Appointment> appointments;

    User user = new User();
    Pet pet = new Pet();
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime later = now.plusHours(1);
    
    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        appointments = new ArrayList<>();
        appointments.add(new Appointment(1L, now, later, "Consultation", "The dog is sick","Title", user, pet));
        appointments.add(new Appointment(2L, now, later, "Operation", "The dog needs surgery","Title", user, pet));
    }

    @Test
    void whenGetAppointments_thenReturnAppointments() throws Exception {
        when(appointmentService.getAllAppointments()).thenReturn(appointments);

        mvc.perform(get("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type", is(appointments.get(0).getType())))
                .andExpect(jsonPath("$[1].type", is(appointments.get(1).getType())));

        verify(appointmentService, times(1)).getAllAppointments();
    }

    @Test
    void whenGetAppointmentById_thenReturnAppointment() throws Exception {
        when(appointmentService.getAppointmentById(1L)).thenReturn(appointments.get(0));

        mvc.perform(get("/api/appointments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is(appointments.get(0).getType())));

        verify(appointmentService, times(1)).getAppointmentById(1L);
    }
    
    @Test
    void whenGetAppointmentById_thenReturnNotFound() throws Exception {
        when(appointmentService.getAppointmentById(-99L)).thenReturn(null);

        mvc.perform(get("/api/appointments/-99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).getAppointmentById(-99L);
    }

    @Test
    void whenGetAppointmentsByDate_thenReturnAppointments() throws Exception {
        when(appointmentService.getAppointmentsByStartDate(appointments.get(0).getStartDate())).thenReturn(List.of(appointments.get(0), appointments.get(1)));

        mvc.perform(get("/api/appointments/date/" + appointments.get(0).getStartDate())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type", is(appointments.get(0).getType())))
                .andExpect(jsonPath("$[1].type", is(appointments.get(1).getType())));

        verify(appointmentService, times(1)).getAppointmentsByStartDate(appointments.get(0).getStartDate());
    }

    @Test
    void whenGetAppointmentsByType_thenReturnAppointments() throws Exception {
        when(appointmentService.getAppointmentsByType(appointments.get(0).getType())).thenReturn(List.of(appointments.get(0)));

        mvc.perform(get("/api/appointments/type/" + appointments.get(0).getType())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type", is(appointments.get(0).getType())));

        verify(appointmentService, times(1)).getAppointmentsByType(appointments.get(0).getType());
    }

    @Test
    void whenGetAppointmentsByNonExistentType_thenReturnNotFound() throws Exception {
        when(appointmentService.getAppointmentsByType("NonExistentType")).thenReturn(null);

        mvc.perform(get("/api/appointments/type/NonExistentType")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).getAppointmentsByType("NonExistentType");
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenCreateAppointment_thenReturnCreated() throws Exception {
        CreateAppointmentDTO appointmentDTO = new CreateAppointmentDTO(now, later, "Consultation", "The dog is sick","Title", 1L, 1L);
        Appointment newAppointment = new Appointment(3L, now, later, "Consultation", "The dog is sick","Title", user, pet);
        
        when(appointmentService.saveAppointment(Mockito.any())).thenReturn(newAppointment);
        when(userService.getUserDetails(1L)).thenReturn(Optional.of(user));
        when(petService.getPetById(1L)).thenReturn(pet);

        mvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(appointmentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.type", is(appointmentDTO.getType())));
    
        verify(appointmentService, times(1)).saveAppointment(Mockito.any());
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenDeleteAppointment_thenReturnOk() throws Exception {
        doNothing().when(appointmentService).deleteAppointment(1L);

        mvc.perform(delete("/api/appointments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(appointmentService, times(1)).deleteAppointment(1L);
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenDeleteNonExistentAppointment_thenReturnOk() throws Exception {
        doNothing().when(appointmentService).deleteAppointment(-99L);

        mvc.perform(delete("/api/appointments/-99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(appointmentService, times(1)).deleteAppointment(-99L);
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenUpdateAppointment_thenReturnUpdated() throws Exception {
        Appointment updatedAppointment = new Appointment(1L, now, later, "Operation", "The dog needs surgery","Title", user, pet);


        when(appointmentService.updateAppointment(Mockito.any(), Mockito.any())).thenReturn(updatedAppointment);

        mvc.perform(put("/api/appointments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(updatedAppointment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is(updatedAppointment.getType())));

        verify(appointmentService, times(1)).updateAppointment(Mockito.any(), Mockito.any());
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenUpdateNonExistentAppointment_thenReturnNotFound() throws Exception {
        Appointment appointment = new Appointment(-99L, now, later, "Consultation", "The dog is sick","Title", user, pet);

        when(appointmentService.updateAppointment(Mockito.any(), Mockito.any())).thenReturn(null);

        mvc.perform(put("/api/appointments/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(appointment)))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).updateAppointment(Mockito.any(), Mockito.any());
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenUpdateAppointmentWithNonExistentDoctor_thenReturnNotFound() throws Exception {
        Appointment updatedAppointment = new Appointment(1L, now, later, "Operation", "The dog needs surgery","Title", null, null);

        when(appointmentService.updateAppointment(Mockito.any(), Mockito.any())).thenReturn(null);

        mvc.perform(put("/api/appointments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(updatedAppointment)))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).updateAppointment(Mockito.any(), Mockito.any());
    }
    
}
