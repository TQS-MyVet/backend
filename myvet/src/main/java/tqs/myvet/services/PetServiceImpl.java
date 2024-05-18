package tqs.myvet.services;

import org.springframework.stereotype.Service;

import java.util.List;

import tqs.myvet.repositories.PetRepository;
import tqs.myvet.entities.Pet;

@Service
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;

    public PetServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Pet getPetById(Long id) {
        return petRepository.findById(id).orElse(null);
    }

    @Override
    public Pet savePet(Pet pet) {
        return petRepository.save(pet);
    }

    @Override
    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

    @Override
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @Override
    public List<Pet> getPetsByName(String name) {
        return petRepository.findByName(name);
    }

    @Override
    public List<Pet> getPetsBySpecies(String species) {
        return petRepository.findBySpecies(species);
    }
}
