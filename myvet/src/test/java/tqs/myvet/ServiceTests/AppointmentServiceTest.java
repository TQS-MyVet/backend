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
    LocalDateTime later = now.plusHours(1);

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @BeforeEach
    void setUp() {

        User doctor = new User();
        
        Appointment ap1 = new Appointment(1L, now, later, "Consultation", "The dog is sick", doctor);
        Appointment ap2 = new Appointment(2L, now, later, "Operation", "The dog needs surgery", doctor);

        List<Appointment> allAppointments = List.of(ap1, ap2);

        Mockito.lenient().when(appointmentRepository.findById(ap1.getId())).thenReturn(Optional.of(ap1));
        Mockito.lenient().when(appointmentRepository.findById(ap2.getId())).thenReturn(Optional.of(ap2));
        Mockito.lenient().when(appointmentRepository.findById(-99L)).thenReturn(Optional.empty());
        Mockito.lenient().when(appointmentRepository.findAll()).thenReturn(allAppointments);

        Mockito.lenient().when(appointmentRepository.findByStartDate(ap1.getStartDate())).thenReturn(allAppointments);
        Mockito.lenient().when(appointmentRepository.findByStartDate(ap2.getStartDate())).thenReturn(allAppointments);

        Mockito.lenient().when(appointmentRepository.findByType(ap1.getType())).thenReturn(List.of(ap1));
        Mockito.lenient().when(appointmentRepository.findByType(ap2.getType())).thenReturn(List.of(ap2));
    }

    @Test
    @DisplayName("Test get all appointments")
    void testGetAllAppointments() {
        List<Appointment> allAppointments = appointmentService.getAllAppointments();
        assertThat(allAppointments).hasSize(2);

        Mockito.verify(appointmentRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Test get appointment by id")
    void testGetAppointmentById() {
        Appointment ap1 = appointmentService.getAppointmentById(1L);
        assertThat(ap1.getId()).isEqualTo(1L);
        assertThat(ap1.getType()).isEqualTo("Consultation");

        Mockito.verify(appointmentRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test get appointment by id not found")
    void testGetAppointmentByIdNotFound() {
        Appointment ap = appointmentService.getAppointmentById(-99L);

        assertThat(ap).isNull();

        Mockito.verify(appointmentRepository, Mockito.times(1)).findById(-99L);
    }

    @Test
    @DisplayName("Test get appointments by date")
    void testGetAppointmentsByDate() {
        List<Appointment> appointments = appointmentService.getAppointmentsByStartDate(now);
        assertThat(appointments).hasSize(2);

        Mockito.verify(appointmentRepository, Mockito.times(1)).findByStartDate(now);
    }

    @Test
    @DisplayName("Test get appointments by type")
    void testGetAppointmentsByType() {
        List<Appointment> appointments = appointmentService.getAppointmentsByType("Consultation");
        assertThat(appointments).hasSize(1);

        Mockito.verify(appointmentRepository, Mockito.times(1)).findByType("Consultation");
    }

    @Test
    @DisplayName("Create a new appointment")
    void testCreateAppointment() {
        User doctor = new User();
        Appointment ap = new Appointment(3L, now, later, "Consultation", "The dog is sick", doctor);
        Appointment createAp = new Appointment(3L, now, later, "Consultation", "The dog is sick", doctor);

        createAp.setId(1L);
        
        Mockito.when(appointmentRepository.save(ap)).thenReturn(createAp);

        Appointment savedAp = appointmentService.saveAppointment(ap);
        assertThat(savedAp.getId()).isEqualTo(1L);

        Mockito.verify(appointmentRepository, Mockito.times(1)).save(ap);
    }

    @Test
    @DisplayName("Update an appointment")
    void testUpdateAppointment() {
        User doctor = new User();
        Appointment ap = new Appointment(1L, now, later, "Consultation", "The dog is sick", doctor);
        Mockito.when(appointmentRepository.save(Mockito.any())).thenReturn(ap);

        appointmentService.saveAppointment(ap);

        Appointment updateAp = new Appointment(1L, now, later, "Operation", "The dog is sick", doctor);
                
        Mockito.when(appointmentRepository.save(Mockito.any())).thenReturn(updateAp);

        Appointment savedAp = appointmentService.updateAppointment(1L, updateAp);
        assertThat(savedAp.getType()).isEqualTo("Operation");

        Mockito.verify(appointmentRepository, Mockito.times(2)).save(Mockito.any());
    }
}
