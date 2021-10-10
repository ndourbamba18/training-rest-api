package com.parlonsdev.repository;

import com.parlonsdev.entities.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	
	Page<Student> findByTrainingId(Long trainingId, Pageable pageable);
	
	Optional<Student> findByIdAndTrainingId(Long id, Long trainingId);

    Optional<Student> findByEmailContaining(String email);
    Optional<Student> findByPhoneContaining(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
