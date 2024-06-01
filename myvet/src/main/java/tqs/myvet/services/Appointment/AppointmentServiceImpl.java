package tqs.myvet.services.Appointment;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
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
    @Transactional
    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public Appointment updateAppointment(Long id, Appointment appointment) {
        Appointment appointmentToUpdate = appointmentRepository.findById(id).orElse(null);

        if (appointmentToUpdate == null) {
            return null;
        }

        appointmentToUpdate.setStartDate(appointment.getStartDate());
        appointmentToUpdate.setEndDate(appointment.getEndDate());
        appointmentToUpdate.setType(appointment.getType());
        appointmentToUpdate.setDocNotes(appointment.getDocNotes());
        appointmentToUpdate.setDoctor(appointment.getDoctor());
        appointmentToUpdate.setPet(appointment.getPet());

        return appointmentRepository.save(appointmentToUpdate);
    }

    @Override
    @Transactional
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    @Override
    public List<Appointment> getAppointmentsByStartDate(LocalDateTime date) {
        return appointmentRepository.findByStartDate(date);
    }

    @Override
    public List<Appointment> getAppointmentsByType(String type) {
        return appointmentRepository.findByType(type);
    }    

    @Override
    public List<Appointment> getAppointmentsByPetId(Long petId) {
        return appointmentRepository.findByPetId(petId);
    }
}
