package com.hotelautomation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelautomation.model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

	@Query(value = "SELECT h from Hotel h "
			+ " LEFT JOIN Floor f on f.hotelId = h.id "
			+ " LEFT JOIN SubCorridor s ON s.floorId = f.id "
			+ " WHERE s.id =:subCorridorId ")
	Hotel findBySubCorridorId(@Param("subCorridorId") Long subCorridorId);

}
