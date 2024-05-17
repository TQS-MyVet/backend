package tqs.myvet.ServiceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import tqs.myvet.entities.Pet;
import tqs.myvet.repositories.PetRepository;
import tqs.myvet.services.PetService;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {
    @Mock(lenient = true)
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @BeforeEach
    public void setUp() {
        Pet pet = new Pet(1L, "Fido", "Male", "2020-01-01", "Dog");
        Pet pet2 = new Pet(2L, "Mimi", "Female", "2019-05-01", "Cat");

        Mockito.when(petRepository.findById(pet.getId())).thenReturn(Optional.of(pet));
        Mockito.when(petRepository.findById(pet2.getId())).thenReturn(Optional.of(pet2));
        Mockito.when(petRepository.findById(-99L)).thenReturn(Optional.empty());
        Mockito.when(petRepository.findAll()).thenReturn(List.of(pet, pet2));

        Mockito.when(petRepository.findByName(pet.getName())).thenReturn(List.of(pet));
        Mockito.when(petRepository.findByName(pet2.getName())).thenReturn(List.of(pet2));
        Mockito.when(petRepository.findBySpecies(pet.getSpecies())).thenReturn(List.of(pet));
        Mockito.when(petRepository.findBySpecies(pet2.getSpecies())).thenReturn(List.of(pet2));


    }

    @Test
    @DisplayName("Test findById when pet exists")
    public void testFindById() {
        Pet result = petService.getPetById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Fido");
    }
    
    @Test
    @DisplayName("Test findById when pet does not exist")
    public void testFindByIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> petService.getPetById(-99L));
    }
    
    @Test
    @DisplayName("Test findAll")
    public void testFindAll() {
        List<Pet> result = petService.getAllPets();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Pet::getName).containsExactlyInAnyOrder("Fido", "Mimi");
    }
    
    @Test
    @DisplayName("Test findByName")
    public void testFindByName() {
        List<Pet> result = petService.getPetsByName("Fido");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Fido");
    }
    
    @Test
    @DisplayName("Test findBySpecies")
    public void testFindBySpecies() {
        List<Pet> result = petService.getPetsBySpecies("Dog");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSpecies()).isEqualTo("Dog");
    }
}