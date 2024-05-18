package tqs.myvet.ServiceTests;

import static org.assertj.core.api.Assertions.assertThat;
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

import tqs.myvet.entities.Pet;
import tqs.myvet.repositories.PetRepository;
import tqs.myvet.services.PetServiceImpl;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {
    @Mock(lenient = true)
    private PetRepository petRepository;

    @InjectMocks
    private PetServiceImpl petService;

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
    void testFindById() {
        Pet result = petService.getPetById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Fido");

        Mockito.verify(petRepository, Mockito.times(1)).findById(any());
    }
    
    @Test
    @DisplayName("Test findById when pet does not exist")
    void testFindByIdNotFound() {
        Pet result = petService.getPetById(-99L);
        assertThat(result).isNull();

        Mockito.verify(petRepository, Mockito.times(1)).findById(any());
    }
    
    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        List<Pet> result = petService.getAllPets();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Pet::getName).containsExactlyInAnyOrder("Fido", "Mimi");

        Mockito.verify(petRepository, Mockito.times(1)).findAll();
    }
    
    @Test
    @DisplayName("Test findByName")
    void testFindByName() {
        List<Pet> result = petService.getPetsByName("Fido");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Fido");

        Mockito.verify(petRepository, Mockito.times(1)).findByName(any());
    }
    
    @Test
    @DisplayName("Test findBySpecies")
    void testFindBySpecies() {
        List<Pet> result = petService.getPetsBySpecies("Dog");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSpecies()).isEqualTo("Dog");

        Mockito.verify(petRepository, Mockito.times(1)).findBySpecies(any());
    }

}