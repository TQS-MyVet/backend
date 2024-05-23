package tqs.myvet.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import tqs.myvet.entities.Appointment;
import tqs.myvet.services.AppointmentService;
@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {
    private final AppointmentService appointmentService;
    
    String appointmentNotFound = "Appointment not found";

    public AppointmentRestController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
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
    public ResponseEntity<Appointment> saveAppointment(@RequestBody Appointment appointment) {
        Appointment savedAppointment = appointmentService.saveAppointment(appointment);
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