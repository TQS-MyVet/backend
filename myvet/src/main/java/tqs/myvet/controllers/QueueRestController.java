package tqs.myvet.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/api/queues")
public class QueueRestController {
    
    private class QueueInfo {
        public final Long userId;
        public final String queueType;
        public final int queuePos;

        public QueueInfo(Long userId, String queueType,int queuePos) {
            this.userId = userId;
            this.queuePos = queuePos;
            this.queueType = queueType;
        }
    }

    private int receptionistQueuePos = 0;
    private int doctorQueuePos = 0;

    private Queue<QueueInfo> receptionistQueue = new LinkedList<>();
    private Queue<QueueInfo> doctorQueue = new LinkedList<>();

    @GetMapping
    public ResponseEntity<List<Queue<QueueInfo>>> getQueues() {
        List<Queue<QueueInfo>> queues = new ArrayList<>();
        queues.add(receptionistQueue);
        queues.add(doctorQueue);
        return new ResponseEntity<>(queues, HttpStatus.OK);
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping("/receptionist/{userId}")
    public ResponseEntity<QueueInfo> addToReceptionistQueue(@PathVariable Long userId) {
        // check if user is already in queue
        for (QueueInfo queueInfo : receptionistQueue) {
            if (queueInfo.userId.equals(userId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User already in queue");
            }
        }

        QueueInfo queueInfo = new QueueInfo(userId, "R",receptionistQueuePos++);
        if (receptionistQueuePos == 100) {
            receptionistQueuePos = 0;
        }
        receptionistQueue.add(queueInfo);
        return new ResponseEntity<>(queueInfo, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping("/doctor/{userId}")
    public ResponseEntity<QueueInfo> addToDoctorQueue(@PathVariable Long userId) {
        // check if user is already in queue
        for (QueueInfo queueInfo : doctorQueue) {
            if (queueInfo.userId.equals(userId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User already in queue");
            }
        }

        QueueInfo queueInfo = new QueueInfo(userId, "D",doctorQueuePos++);
        if (doctorQueuePos == 100) {
            doctorQueuePos = 0;
        }
        doctorQueue.add(queueInfo);
        return new ResponseEntity<>(queueInfo, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/receptionist")
    public ResponseEntity<QueueInfo> removeFromReceptionistQueue() {
        try {
            return new ResponseEntity<>(receptionistQueue.remove(), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Queue is empty");
        }
    }

    @SecurityRequirement(name = "Authorization")
    @DeleteMapping("/doctor")
    public ResponseEntity<QueueInfo> removeFromDoctorQueue() {
        try {
            return new ResponseEntity<>(doctorQueue.remove(), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Queue is empty");
        }
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/receptionist/{userId}")
    public ResponseEntity<QueueInfo> getReceptionistQueuePos(@PathVariable Long userId) {
        for (QueueInfo queueInfo : receptionistQueue) {
            if (queueInfo.userId.equals(userId)) {
                return new ResponseEntity<>(queueInfo, HttpStatus.OK);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found in queue");
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping("/doctor/{userId}")
    public ResponseEntity<QueueInfo> getDoctorQueuePos(@PathVariable Long userId) {
        for (QueueInfo queueInfo : doctorQueue) {
            if (queueInfo.userId.equals(userId)) {
                return new ResponseEntity<>(queueInfo, HttpStatus.OK);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found in queue");
    }
}
