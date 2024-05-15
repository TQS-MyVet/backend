package tqs.myvet.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 30)
<<<<<<< Updated upstream
    private String ownerName;

    @NotNull
    // @Email
    private String ownerEmail;

    @NotNull
    private String ownerPhone;

    @NotNull
    @Size(min = 2, max = 30)
    private String petName;
=======
    private String name;
>>>>>>> Stashed changes

    @Column(nullable = false)
    private String sex;

<<<<<<< Updated upstream
    @NotNull
    private int petAge;
=======
    @Column(nullable = false)
    private String birthdate;
>>>>>>> Stashed changes

    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String species;

    @NotNull
    private String ownerPassword;
}