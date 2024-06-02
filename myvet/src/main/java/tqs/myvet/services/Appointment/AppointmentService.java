package tqs.myvet.services.Appointment;

import java.util.List;
import java.time.LocalDateTime;

import tqs.myvet.entities.Appointment;
public interface AppointmentService {
    Appointment getAppointmentById(Long id);
    Appointment saveAppointment(Appointment appointment);
    Appointment updateAppointment(Long id, Appointment appointment);
    void deleteAppointment(Long id);

    List<Appointment> getAllAppointments();
    List<Appointment> getAppointmentsByStartDate(LocalDateTime date);
    List<Appointment> getAppointmentsByType(String type);
    List<Appointment> getAppointmentsByPetId(Long petId);
}
