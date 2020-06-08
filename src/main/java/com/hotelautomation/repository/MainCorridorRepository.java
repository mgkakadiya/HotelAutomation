package com.hotelautomation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotelautomation.model.MainCorridor;

public interface MainCorridorRepository extends JpaRepository<MainCorridor, Long> {

	@Query(value = "SELECT id from MainCorridor")
	public List<Long> findAllId();

}
