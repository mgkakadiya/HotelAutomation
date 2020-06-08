package com.hotelautomation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotelautomation.model.Light;

public interface LightRepository extends JpaRepository<Light, Long> {

	List<Light> findAllByMainCorridorIdIn(List<Long> mainCorridorIds);

	Light findBySubCorridorId(Long subCorridorId);

}
