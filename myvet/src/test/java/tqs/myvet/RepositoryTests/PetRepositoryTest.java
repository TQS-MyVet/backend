package tqs.myvet.RepositoryTests;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqs.myvet.entities.Pet;
import tqs.myvet.repositories.PetRepository;

@DataJpaTest
class PetRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PetRepository petRepository;

    @Test
    void whenFindById_thenReturnPet() {
        Pet pet = new Pet();
        pet.setName("Rex");
        pet.setSex("M");
        pet.setBirthdate("2021-05-05");
        pet.setSpecies("Dog");
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        Pet fromDb = petRepository.findById(-11L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void givenSetOfPets_whenFindAll_thenReturnAllPets() {
        Pet pet1 = new Pet();
        pet1.setName("Rex");
        pet1.setSex("M");
        pet1.setBirthdate("2021-05-05");
        pet1.setSpecies("Dog");

        Pet pet2 = new Pet();
        pet2.setName("Rex");
        pet2.setSex("M");
        pet2.setBirthdate("2021-05-05");
        pet2.setSpecies("Dog");

        Pet pet3 = new Pet();
        pet3.setName("Rex");
        pet3.setSex("M");
        pet3.setBirthdate("2021-05-05");
        pet3.setSpecies("Dog");

        entityManager.persist(pet1);
        entityManager.persist(pet2);
        entityManager.persist(pet3);

        List<Pet> pets = petRepository.findAll();

        assertThat(pets).hasSize(3).extracting(Pet::getName).containsOnly(pet1.getName(), pet2.getName(), pet3.getName());
    }
}
