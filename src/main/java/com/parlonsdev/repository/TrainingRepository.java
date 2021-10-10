package com.parlonsdev.repository;

import com.parlonsdev.entities.Training;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
	
	 // CUSTOM QUERY
    @Query(value = "select * from training t where t.name like %:keyword% or t.duration like %:keyword%", nativeQuery = true)
    List<Training> findByKeyword(@Param("keyword") String keyword);
    
	Page<Training> findByStarted(boolean started, Pageable pageable);
	
	Page<Training> findByNameContains(String name, Pageable pageable);
	
	List<Training> findByNameContains(String name, Sort sort);

    public Optional<Training> findByNameContaining(String name);

    public boolean existsByName(String name);
}
