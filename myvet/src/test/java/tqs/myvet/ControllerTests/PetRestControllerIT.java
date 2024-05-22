package tqs.myvet.ControllerTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;


import tqs.myvet.repositories.PetRepository;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import tqs.myvet.entities.Pet;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class PetRestControllerIT {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PetRepository repository;



    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void whenGetPets_thenReturnPets() throws Exception {
        Pet pet = new Pet(1L, "Rex", "M", "2020-01-01", "Dog");
        Pet pet2 = new Pet(2L, "Mimi", "F", "2020-01-01", "Cat");
        repository.save(pet);
        repository.save(pet2);

        List<Pet> pets = Arrays.asList(pet, pet2);

        mvc.perform(get("/api/pets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(pets.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(pets.get(1).getName())));
    }

    @Test
    void whenGetPetById_thenReturnPet() throws Exception {
        Pet pet = new Pet(1L, "Rex", "M", "2020-01-01", "Dog");
        repository.save(pet);

        mvc.perform(get("/api/pets/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(pet.getName())));

    }

    @Test
    void whenGetPetByNonExistentId_thenReturnNotFound() throws Exception {
        mvc.perform(get("/api/pets/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void whenGetPetsBySpecies_thenReturnPets() throws Exception {
        Pet pet = new Pet(1L, "Rex", "M", "2020-01-01", "Dog");
        repository.save(pet);

        mvc.perform(get("/api/pets/species/Dog")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(pet.getName())));


    }

    @Test
    void whenGetPetsByNonExistentSpecies_thenReturnNotFound() throws Exception {
        mvc.perform(get("/api/pets/species/Bird")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void whenGetPetsByName_thenReturnPets() throws Exception {
        Pet pet = new Pet(1L, "Rex", "M", "2020-01-01", "Dog");
        Pet pet2 = new Pet(2L, "Mimi", "F", "2020-01-01", "Cat");
        repository.save(pet);
        repository.save(pet2);

        List<Pet> pets = Arrays.asList(pet, pet2);

        mvc.perform(get("/api/pets/name/Rex")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(pets.get(0).getName())));

    }

    @Test
    void whenGetPetsByNonExistentName_thenReturnNotFound() throws Exception {
        mvc.perform(get("/api/pets/name/John")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void whenSavePet_thenReturnsSavedPet() throws Exception {
        Pet newPet = new Pet(3L, "Birdy", "M", "2021-01-01", "Bird");

        mvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(newPet)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(newPet.getName())));
    }

    @Test
    void whenDeletePet_thenStatusOk() throws Exception {
        mvc.perform(delete("/api/pets/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
