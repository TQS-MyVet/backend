package tqs.myvet.ServiceTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import tqs.myvet.repositories.AppointmentRepository;
import tqs.myvet.services.AppointmentServiceImpl;
import tqs.myvet.entities.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {
    
    LocalDateTime now = LocalDateTime.now();

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @BeforeEach
    void setUp() {
        User doctor = new User();
        
        Appointment ap1 = new Appointment(1L, now, "Consultation", "The dog is sick", doctor);
        Appointment ap2 = new Appointment(2L, now, "Operation", "The dog needs surgery", doctor);

        List<Appointment> allAppointments = List.of(ap1, ap2);

        Mockito.when(appointmentRepository.findById(ap1.getId())).thenReturn(Optional.of(ap1));
        Mockito.when(appointmentRepository.findById(ap2.getId())).thenReturn(Optional.of(ap2));
        Mockito.when(appointmentRepository.findById(-99L)).thenReturn(Optional.empty());
        Mockito.when(appointmentRepository.findAll()).thenReturn(allAppointments);

        Mockito.when(appointmentRepository.findByDate(ap1.getDate())).thenReturn(allAppointments);
        Mockito.when(appointmentRepository.findByDate(ap2.getDate())).thenReturn(allAppointments);

        Mockito.when(appointmentRepository.findByType(ap1.getType())).thenReturn(List.of(ap1));
        Mockito.when(appointmentRepository.findByType(ap2.getType())).thenReturn(List.of(ap2));
    }

    @Test
    @DisplayName("Test get all appointments")
    void testGetAllAppointments() {
        List<Appointment> allAppointments = appointmentService.getAllAppointments();
        assertThat(allAppointments).hasSize(2);
    }

    @Test
    @DisplayName("Test get appointment by id")
    void testGetAppointmentById() {
        Appointment ap1 = appointmentService.getAppointmentById(1L);
        assertThat(ap1.getId()).isEqualTo(1L);
        assertThat(ap1.getType()).isEqualTo("Consultation");
    }

    @Test
    @DisplayName("Test get appointment by id not found")
    void testGetAppointmentByIdNotFound() {
        Appointment ap = appointmentService.getAppointmentById(-99L);

        assertThat(ap).isNull();
    }

    @Test
    @DisplayName("Test get appointments by date")
    void testGetAppointmentsByDate() {
        List<Appointment> appointments = appointmentService.getAppointmentsByDate(now);
        assertThat(appointments).hasSize(2);
    }

    @Test
    @DisplayName("Test get appointments by type")
    void testGetAppointmentsByType() {
        List<Appointment> appointments = appointmentService.getAppointmentsByType("Consultation");
        assertThat(appointments).hasSize(1);
    }

    @Test
    @DisplayName("Create a new appointment")
    void testCreateAppointment() {
        User doctor = new User();
        Appointment ap = new Appointment(3L, now, "Consultation", "The dog is sick", doctor);
        Appointment createAp = new Appointment(3L, now, "Consultation", "The dog is sick", doctor);

        createAp.setId(1L);
        
        Mockito.when(appointmentRepository.save(ap)).thenReturn(createAp);

        Appointment savedAp = appointmentService.saveAppointment(ap);
        assertThat(savedAp.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Update an appointment")
    void testUpdateAppointment() {
        User doctor = new User();
        Appointment ap = new Appointment(1L, now, "Consultation", "The dog is sick", doctor);
        Appointment updateAp = new Appointment(1L, now, "Consultation", "The dog is sick", doctor);

        updateAp.setType("Operation");
        
        Mockito.when(appointmentRepository.save(ap)).thenReturn(updateAp);

        Appointment savedAp = appointmentService.saveAppointment(ap);
        assertThat(savedAp.getType()).isEqualTo("Operation");
    }
}
