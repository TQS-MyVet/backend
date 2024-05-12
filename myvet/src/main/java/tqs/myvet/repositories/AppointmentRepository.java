package tqs.myvet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import tqs.myvet.entities.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDate(String date);
    List<Appointment> findByHour(String hour);
    List<Appointment> findByType(String type);
}
