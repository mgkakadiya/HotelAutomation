package com.hotelautomation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelautomation.model.AirConditioner;
import com.hotelautomation.model.Status;

public interface AirConditionerRepository extends JpaRepository<AirConditioner, Long> {

	@Query(value = "SELECT a from AirConditioner a "
			+ " LEFT JOIN SubCorridor s on s.id = a.subCorridorId "
			+ " WHERE s.floorId =:floorId ")
	List<AirConditioner> findSubCorridorAcByFloorId(@Param("floorId") Long floorId);

	@Query(value = "SELECT a from AirConditioner a "
			+ " LEFT JOIN SubCorridor s on s.id = a.subCorridorId "
			+ " WHERE s.floorId =:floorId AND a.status =:status ")
	List<AirConditioner> findSubCorridorAcByFloorIdAndStatus(@Param("floorId") Long floorId,@Param("status")  Status status);

}
