package tech.justjava.alumni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.justjava.alumni.entity.AlumniProcess;

public interface AlumniProcessRepository extends JpaRepository<AlumniProcess, Long> {
}
