package tqs.myvet.ControllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import tqs.myvet.entities.Pet;
import tqs.myvet.entities.DTO.CreatePetDTO;
import tqs.myvet.repositories.UserRepository;
import tqs.myvet.services.JWT.JWTService;
import tqs.myvet.services.Pet.PetService;
import tqs.myvet.services.User.CustomUserDetailsService;
import tqs.myvet.controllers.PetRestController;

@WebMvcTest(PetRestController.class)
class PetController_WithMockServiceTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private UserRepository userRepository;
    
    private MockMvc mvc;

    @MockBean
    private PetService petService;

    private List<Pet> pets;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        pets = new ArrayList<>();
        pets.add(new Pet(1L, "Rex", "M", "2020-01-01", "Dog"));
        pets.add(new Pet(2L, "Mimi", "F", "2020-01-01", "Cat"));
    }

    @Test
    void whenGetPets_thenReturnPets() throws Exception {
        when(petService.getAllPets()).thenReturn(pets);

        mvc.perform(get("/api/pets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(pets.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(pets.get(1).getName())));

        verify(petService, times(1)).getAllPets();
    }

    @Test
    void whenGetPetById_thenReturnPet() throws Exception {
        when(petService.getPetById(1L)).thenReturn(pets.get(0));

        mvc.perform(get("/api/pets/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(pets.get(0).getName())));

        verify(petService, times(1)).getPetById(1L);
    }

    @Test
    void whenGetPetByNonExistentId_thenReturnNotFound() throws Exception {
        when(petService.getPetById(3L)).thenReturn(null);

        mvc.perform(get("/api/pets/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(petService, times(1)).getPetById(3L);
    }

    @Test
    void whenGetPetsBySpecies_thenReturnPets() throws Exception {
        when(petService.getPetsBySpecies("Dog")).thenReturn(Arrays.asList(pets.get(0)));

        mvc.perform(get("/api/pets/species/Dog")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(pets.get(0).getName())));

        verify(petService, times(1)).getPetsBySpecies("Dog");

    }

    @Test
    void whenGetPetsByNonExistentSpecies_thenReturnNotFound() throws Exception {
        when(petService.getPetsBySpecies("Bird")).thenReturn(null);

        mvc.perform(get("/api/pets/species/Bird")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(petService, times(1)).getPetsBySpecies("Bird");
    }

    @Test
    void whenGetPetsByName_thenReturnPets() throws Exception {
        when(petService.getPetsByName("Rex")).thenReturn(Arrays.asList(pets.get(0)));

        mvc.perform(get("/api/pets/name/Rex")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(pets.get(0).getName())));

        verify(petService, times(1)).getPetsByName("Rex");
    }

    @Test
    void whenGetPetsByNonExistentName_thenReturnNotFound() throws Exception {
        when(petService.getPetsByName("John")).thenReturn(null);

        mvc.perform(get("/api/pets/name/John")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(petService, times(1)).getPetsByName("John");
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenSavePet_thenReturnsSavedPet() throws Exception {
        Pet newPet = new Pet(3L, "Birdy", "M", "2021-01-01", "Bird");
        when(petService.savePet(any(Pet.class))).thenReturn(newPet);

        mvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(newPet)))
                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.name", is(newPet.getName())));

        verify(petService, times(1)).savePet(any(Pet.class));
    }

    @WithMockUser(roles="DOCTOR")
    @Test
    void whenDeletePet_thenStatusOk() throws Exception {
        doNothing().when(petService).deletePet(1L);

        mvc.perform(delete("/api/pets/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(petService, times(1)).deletePet(1L);
    }

    @Test
    @WithMockUser(roles="DOCTOR")
    void whenUpdatePet_thenReturnsUpdatedPet() throws Exception {
        // Arrange
        Long id = 1L;
        CreatePetDTO petDTO = new CreatePetDTO();
        petDTO.setName("Rexy");
        petDTO.setSex("M");
        petDTO.setBirthdate("2020-01-01");
        petDTO.setSpecies("Dog");
    
        Pet petToUpdate = new Pet(id, petDTO.getName(), petDTO.getSex(), petDTO.getBirthdate(), petDTO.getSpecies());
        Pet updatedPet = new Pet(id, "Rexy", "M", "2020-01-01", "Dog");
    
        when(petService.updatePet(eq(id), any(Pet.class))).thenReturn(updatedPet);
    
        // Act & Assert
        mvc.perform(put("/api/pets/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.toJson(petDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedPet.getName())));
    
        verify(petService, times(1)).updatePet(eq(id), any(Pet.class));
    }
}
