package tech.justjava.alumni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.justjava.alumni.entity.AlumniRequest;

public interface AlumniRequestRepository extends JpaRepository<AlumniRequest, Long> {
}
