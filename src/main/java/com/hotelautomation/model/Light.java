package com.hotelautomation.model;

import java.util.Date;

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
@Table(name = "light")
public class Light {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Value("${electricity.equipment.light}")
	@Column(name = "power_consume")
	private Long powerConsume;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;
	
	@Column(name = "sub_corridor_id")
	private Long subCorridorId;

	@Column(name = "main_corridor_id")
	private Long mainCorridorId;
	
	@Column(name = "last_motion_detect")
	private Date lastMotionDetect;
}
