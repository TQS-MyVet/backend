package tqs.myvet.controllers;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import tqs.myvet.entities.Pet;
import tqs.myvet.services.PetService;

@RestController
@RequestMapping("/api/pets")
public class PetRestController {
    private final PetService petService;

    public PetRestController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        List<Pet> pets = petService.getAllPets();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Pet pet = petService.getPetById(id);
        if (pet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
        }
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @GetMapping("/species/{species}")
    public ResponseEntity<List<Pet>> getPetsBySpecies(@PathVariable String species) {
        List<Pet> pets = petService.getPetsBySpecies(species);
        if (pets == null || pets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No pets found for the species: " + species);
        }
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Pet>> getPetsByName(@PathVariable String name) {
        List<Pet> pets = petService.getPetsByName(name);
        if (pets == null || pets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No pets found with the name: " + name);
        }
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Pet> savePet(@RequestBody @Valid Pet pet) {
        Pet savedPet = petService.savePet(pet);
        return new ResponseEntity<>(savedPet, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        try {
            petService.deletePet(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            // Pet not found, but we return OK status anyway
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
