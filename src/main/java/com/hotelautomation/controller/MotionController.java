package com.hotelautomation.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hotelautomation.model.Status;
import com.hotelautomation.service.MotionService;

@Controller
public class MotionController {
	
	private final MotionService service;

	public MotionController(MotionService service) {
		this.service = service;
	}

	@GetMapping("/{subCorridorId}")
	public @ResponseBody Status detectMovement(@PathVariable("subCorridorId") Long subCorridorId) throws IOException {
		try {
			return service.activateSubCorridorMovement(subCorridorId);
		} catch (Exception e) {
			return null;
		}
	}

}
