package tqs.myvet.ControllerTests;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import tqs.myvet.controllers.AppointmentRestController;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import tqs.myvet.entities.User;
import tqs.myvet.entities.Appointment;
import tqs.myvet.services.AppointmentService;

@WebMvcTest(AppointmentRestController.class)
class AppointmentControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppointmentService appointmentService;

    private List<Appointment> appointments;

    User doctor = new User();
    LocalDateTime now = LocalDateTime.now();
    
    @BeforeEach
    void setUp() {
        appointments = new ArrayList<>();
        appointments.add(new Appointment(1L, now, "Consultation", "The dog is sick", doctor));
        appointments.add(new Appointment(2L, now, "Operation", "The dog needs surgery", doctor));
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
        when(appointmentService.getAppointmentsByDate(appointments.get(0).getDate())).thenReturn(appointments);

        mvc.perform(get("/api/appointments/date/" + appointments.get(0).getDate())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type", is(appointments.get(0).getType())))
                .andExpect(jsonPath("$[1].type", is(appointments.get(1).getType())));

        verify(appointmentService, times(1)).getAppointmentsByDate(appointments.get(0).getDate());
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

    @Test
    void whenCreateAppointment_thenReturnCreated() throws Exception {
        Appointment appointment = new Appointment(3L, now, "Consultation", "The dog is sick", doctor);

        when(appointmentService.saveAppointment(Mockito.any())).thenReturn(appointment);
        mvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(appointment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.type", is(appointment.getType())));

        verify(appointmentService, times(1)).saveAppointment(Mockito.any());
    }

    @Test
    void whenDeleteAppointment_thenReturnOk() throws Exception {
        doNothing().when(appointmentService).deleteAppointment(1L);

        mvc.perform(delete("/api/appointments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(appointmentService, times(1)).deleteAppointment(1L);
    }

    @Test
    void whenDeleteNonExistentAppointment_thenReturnOk() throws Exception {
        doNothing().when(appointmentService).deleteAppointment(-99L);

        mvc.perform(delete("/api/appointments/-99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(appointmentService, times(1)).deleteAppointment(-99L);
    }

    @Test
    void whenUpdateAppointment_thenReturnUpdated() throws Exception {
        Appointment updatedAppointment = new Appointment(1L, now, "Operation", "The dog needs surgery", doctor);


        when(appointmentService.updateAppointment(Mockito.any(), Mockito.any())).thenReturn(updatedAppointment);

        mvc.perform(put("/api/appointments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(updatedAppointment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is(updatedAppointment.getType())));

        verify(appointmentService, times(1)).updateAppointment(Mockito.any(), Mockito.any());
    }

    @Test
    void whenUpdateNonExistentAppointment_thenReturnNotFound() throws Exception {
        Appointment appointment = new Appointment(-99L, now, "Consultation", "The dog is sick", doctor);

        when(appointmentService.updateAppointment(Mockito.any(), Mockito.any())).thenReturn(null);

        mvc.perform(put("/api/appointments/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(appointment)))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).updateAppointment(Mockito.any(), Mockito.any());
    }

    @Test
    void whenUpdateAppointmentWithNonExistentDoctor_thenReturnNotFound() throws Exception {
        Appointment updatedAppointment = new Appointment(1L, now, "Operation", "The dog needs surgery", null);

        when(appointmentService.updateAppointment(Mockito.any(), Mockito.any())).thenReturn(null);

        mvc.perform(put("/api/appointments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(updatedAppointment)))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).updateAppointment(Mockito.any(), Mockito.any());
    }
    
}
