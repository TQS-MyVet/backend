package tqs.myvet.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import tqs.myvet.entities.Appointment;
import tqs.myvet.entities.Pet;
import tqs.myvet.entities.User;
import tqs.myvet.entities.DTO.CreateAppointmentDTO;
import tqs.myvet.services.Appointment.AppointmentService;
import tqs.myvet.services.Pet.PetService;
import tqs.myvet.services.User.UserService;

@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {
    private final AppointmentService appointmentService;
    private final PetService petService;
    private final UserService userService;

    String appointmentNotFound = "Appointment not found";

    public AppointmentRestController(AppointmentService appointmentService, PetService petService, UserService userService) {
        this.appointmentService = appointmentService;
        this.petService = petService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPetId(@PathVariable Long petId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPetId(petId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);

        if (appointment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, appointmentNotFound);
        }

        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDate(@PathVariable String date) {
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(date);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format. Please use the format: yyyy-MM-ddTHH:mm:ss");
        }

        List<Appointment> appointments = appointmentService.getAppointmentsByStartDate(dateTime);

        if (appointments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No appointments found for the date: " + date);
        }

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Appointment>> getAppointmentsByType(@PathVariable String type) {
        List<Appointment> appointments = appointmentService.getAppointmentsByType(type);

        if (appointments == null || appointments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No appointments found for the type: " + type);
        }

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Appointment> saveAppointment(@RequestBody CreateAppointmentDTO appointment) {
        Optional<User> user = userService.getUserDetails(appointment.getDoctorId());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found");
        }
        
        Pet pet = petService.getPetById(appointment.getPetId());
        if (pet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
        }

        Appointment newAppointment = new Appointment(null, appointment.getStartDate(), appointment.getEndDate(), appointment.getType(), appointment.getDocNotes(),appointment.getTitle(), user.get(), pet);

        Appointment savedAppointment = appointmentService.saveAppointment(newAppointment);
        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        Appointment updatedAppointment = appointmentService.updateAppointment(id, appointment);

        if (updatedAppointment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, appointmentNotFound);
        }

        return new ResponseEntity<>(updatedAppointment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id){
        try{
            appointmentService.deleteAppointment(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, appointmentNotFound);
        }
    }
}