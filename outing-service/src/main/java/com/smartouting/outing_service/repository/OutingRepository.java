package com.smartouting.outing_service.repository;

import com.smartouting.outing_service.model.Outing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OutingRepository extends JpaRepository<Outing, Long> {
    List<Outing> findByStudentId(String studentId);
}
