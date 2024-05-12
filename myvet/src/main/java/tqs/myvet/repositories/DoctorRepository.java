package tqs.myvet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import tqs.myvet.entities.Doctor;  

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByName(String name);
    List<Doctor> findBySpeciality(String speciality);
}
