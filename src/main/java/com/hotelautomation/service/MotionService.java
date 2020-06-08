package com.hotelautomation.service;

import com.hotelautomation.exception.HotelAutomationException;
import com.hotelautomation.model.Status;

public interface MotionService {
	
	public Status activateSubCorridorMovement(Long subCorridorId) throws HotelAutomationException;

}
