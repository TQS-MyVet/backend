package tqs.myvet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import tqs.myvet.entities.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwnerName(String name);
    List<Pet> findByOwnerEmail(String email);
    List<Pet> findByPetName(String name);
}