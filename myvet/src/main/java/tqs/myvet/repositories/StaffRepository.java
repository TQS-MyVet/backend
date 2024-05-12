package tqs.myvet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import tqs.myvet.entities.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByName(String name);
}
