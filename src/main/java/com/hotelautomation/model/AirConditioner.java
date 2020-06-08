package com.hotelautomation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "air_conditioner")
public class AirConditioner {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Value("${electricity.equipment.ac}")
	@Column(name = "power_consume")
	private Long powerConsume;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;
	
	@Column(name = "sub_corridor_id")
	private Long subCorridorId;

	@Column(name = "main_corridor_id")
	private Long mainCorridorId;
	
}
