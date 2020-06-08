package com.hotelautomation.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelautomation.exception.HotelAutomationException;
import com.hotelautomation.model.AirConditioner;
import com.hotelautomation.model.Hotel;
import com.hotelautomation.model.Light;
import com.hotelautomation.model.Status;
import com.hotelautomation.model.SubCorridor;
import com.hotelautomation.repository.AirConditionerRepository;
import com.hotelautomation.repository.HotelRepository;
import com.hotelautomation.repository.LightRepository;
import com.hotelautomation.repository.MainCorridorRepository;
import com.hotelautomation.repository.SubCorridorRepository;
import com.hotelautomation.service.MotionService;

@Service
public class MotionServiceImpl implements MotionService {

	static final long ONE_MINUTE_IN_MILLIS = 60000;
	Logger logger = LogManager.getLogger(MotionServiceImpl.class);
	
	@Autowired
	MainCorridorRepository mainCorridorRepository;
	
	@Autowired
	SubCorridorRepository subCorridorRepository;

	@Autowired
	AirConditionerRepository airConditionerRepository;

	@Autowired
	LightRepository lightRepository;

	@Autowired
	HotelRepository hotelRepository;

	@Override
	public Status activateSubCorridorMovement(Long subCorridorId) throws HotelAutomationException {
		Optional<SubCorridor> subCorridorOptional = subCorridorRepository.findById(subCorridorId);
		if (subCorridorOptional.isPresent()) {
			SubCorridor subCorridor = subCorridorOptional.get();
			Hotel hotel = hotelRepository.findBySubCorridorId(subCorridorId);
			Long maxPowerConsume = hotel.getMaxPowerConsume();
			Long currentPowerConsume = hotel.getPowerConsume();

			Light light = lightRepository.findBySubCorridorId(subCorridorId);
			light.setStatus(Status.ON);
			light.setLastMotionDetect(new Date());
			lightRepository.save(light);
			currentPowerConsume += light.getPowerConsume();
			logger.info("Maximum Power Consume {}, current Power Consume {} ", hotel.getMaxPowerConsume(), currentPowerConsume);
			if (currentPowerConsume > maxPowerConsume) {
				currentPowerConsume = reducedPowerConsume(subCorridor.getFloorId(), maxPowerConsume,
						currentPowerConsume);
			}
			hotel.setPowerConsume(currentPowerConsume);
			hotelRepository.save(hotel);

			scheduleLightForSwitchOff(subCorridorId, subCorridor.getFloorId());

		} else {
			throw new HotelAutomationException("Invalid subCorridorId");
		}
		return Status.OFF;
	}

	private void scheduleLightForSwitchOff(Long subCorridorId, Long floorId) {
		final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
		executor.schedule(new Runnable() {
			@Override
			public void run() {
				Hotel hotel = hotelRepository.findBySubCorridorId(subCorridorId);
				Long maxPowerConsume = hotel.getMaxPowerConsume();
				Long currentPowerConsume = hotel.getPowerConsume();

				Light light = lightRepository.findBySubCorridorId(subCorridorId);

				Calendar date = Calendar.getInstance();
				Long t = date.getTimeInMillis();
				Long lastUpdatedTime = light.getLastMotionDetect().getTime();
				if ((t - ONE_MINUTE_IN_MILLIS) > lastUpdatedTime) {
					light.setStatus(Status.OFF);
					light.setLastMotionDetect(new Date());
					lightRepository.save(light);
					currentPowerConsume -= light.getPowerConsume();

					List<AirConditioner> airConditioners = airConditionerRepository
							.findSubCorridorAcByFloorIdAndStatus(floorId, Status.OFF);
					for (AirConditioner airConditioner : airConditioners) {
						if ((airConditioner.getPowerConsume() + currentPowerConsume) <= maxPowerConsume) {
							airConditioner.setStatus(Status.ON);
							currentPowerConsume += airConditioner.getPowerConsume();
							airConditionerRepository.save(airConditioner);
						}
					}
					hotel.setPowerConsume(currentPowerConsume);
					hotelRepository.save(hotel);
				}
			}
		}, 1, TimeUnit.MINUTES);

	}

	private Long reducedPowerConsume(Long floorlId, Long maxPowerConsume, Long currentPowerConsume) {
		List<AirConditioner> airConditioners = airConditionerRepository.findSubCorridorAcByFloorId(floorlId);
		for (AirConditioner airConditioner : airConditioners) {
			if (airConditioner.getStatus() != null && airConditioner.getStatus().equals(Status.ON)) {
				airConditioner.setStatus(Status.OFF);
				airConditionerRepository.save(airConditioner);
				currentPowerConsume -= airConditioner.getPowerConsume();
				if (currentPowerConsume <= maxPowerConsume) {
					return currentPowerConsume;
				}
			}
		}
		return currentPowerConsume;
	}
}
