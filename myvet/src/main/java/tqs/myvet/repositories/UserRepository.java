package tqs.myvet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import tqs.myvet.entities.User;
import tqs.myvet.entities.Pet;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<Pet> findPetsByUserId(Long id);

}