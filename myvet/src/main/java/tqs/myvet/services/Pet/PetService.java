package tqs.myvet.services.Pet;

import java.util.List;

import tqs.myvet.entities.Pet;

public interface PetService {
    Pet getPetById(Long id);
    Pet savePet(Pet pet);
    Pet updatePet(Long id, Pet pet);
    void deletePet(Long id);
    List<Pet> getAllPets();
    List<Pet> getPetsByName(String name);
    List<Pet> getPetsBySpecies(String species);
}