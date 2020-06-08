package com.hotelautomation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotelautomation.model.SubCorridor;

public interface SubCorridorRepository  extends JpaRepository<SubCorridor, Long> {

	@Query(value = "SELECT id from SubCorridor")
	List<Long> findAllId();
}

