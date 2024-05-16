package tqs.myvet.RepositoryTests;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqs.myvet.entities.Appointment;
import tqs.myvet.repositories.AppointmentRepository;
import tqs.myvet.entities.User;

@DataJpaTest
class AppointmentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void whenFindById_thenReturnAppointment() {
        User user = new User();
        Appointment appointment = new Appointment();
        appointment.setDate(LocalDateTime.now());
        appointment.setType("Consultation");
        appointment.setDocNotes("The dog is sick");
        appointment.setUser(user);

        
        entityManager.persistAndFlush(appointment);

        Appointment found = appointmentRepository.findById(appointment.getId()).get();

        assertThat(found.getDate()).isEqualTo(appointment.getDate());
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        Appointment fromDb = appointmentRepository.findById(-11L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void givenSetOfAppointments_whenFindAll_thenReturnAllAppointments() {
        Appointment appointment1 = new Appointment();
        appointment1.setDate(LocalDateTime.now());
        appointment1.setType("Consultation");
        appointment1.setDocNotes("The dog is sick");

        Appointment appointment2 = new Appointment();
        appointment2.setDate(LocalDateTime.now());
        appointment2.setType("Consultation");
        appointment2.setDocNotes("The dog is sick");

        Appointment appointment3 = new Appointment();
        appointment3.setDate(LocalDateTime.now());
        appointment3.setType("Consultation");
        appointment3.setDocNotes("The dog is sick");

        entityManager.persist(appointment1);
        entityManager.persist(appointment2);
        entityManager.persist(appointment3);
        entityManager.flush();

        List<Appointment> allAppointments = appointmentRepository.findAll();

        assertThat(allAppointments).hasSize(3).extracting(Appointment::getDate).containsOnly(appointment1.getDate(), appointment2.getDate(), appointment3.getDate());
    }

}

