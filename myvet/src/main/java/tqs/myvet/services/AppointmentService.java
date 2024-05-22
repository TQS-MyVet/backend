package tqs.myvet.services;

import java.util.List;
import java.time.LocalDateTime;

import tqs.myvet.entities.Appointment;
public interface AppointmentService {
    Appointment getAppointmentById(Long id);
    Appointment saveAppointment(Appointment appointment);
    void deleteAppointment(Long id);

    List<Appointment> getAllAppointments();
    List<Appointment> getAppointmentsByDate(LocalDateTime date);
    List<Appointment> getAppointmentsByType(String type);
}
