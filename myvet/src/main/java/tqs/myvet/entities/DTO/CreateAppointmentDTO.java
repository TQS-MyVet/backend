package tqs.myvet.entities.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String type;
    private String docNotes;
    private Long doctorId;
    private Long petId;
}
