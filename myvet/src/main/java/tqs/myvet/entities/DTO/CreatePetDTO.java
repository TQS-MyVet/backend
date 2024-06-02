package tqs.myvet.entities.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePetDTO {
    private String name;
    private String sex;
    private String birthdate;
    private String species;
}
