package tqs.myvet.services;

import java.util.List;

import org.springframework.stereotype.Service;

import tqs.myvet.entities.Appointment;
import tqs.myvet.repositories.AppointmentRepository;
import java.time.LocalDateTime;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    @Override
    public List<Appointment> getAppointmentsByDate(LocalDateTime date) {
        return appointmentRepository.findByDate(date);
    }

    @Override
    public List<Appointment> getAppointmentsByType(String type) {
        return appointmentRepository.findByType(type);
    }    
}
