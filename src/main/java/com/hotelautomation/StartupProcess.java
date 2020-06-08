package com.hotelautomation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hotelautomation.model.AirConditioner;
import com.hotelautomation.model.Hotel;
import com.hotelautomation.model.Light;
import com.hotelautomation.model.Status;
import com.hotelautomation.repository.AirConditionerRepository;
import com.hotelautomation.repository.HotelRepository;
import com.hotelautomation.repository.LightRepository;
import com.hotelautomation.repository.MainCorridorRepository;
import com.hotelautomation.repository.SubCorridorRepository;

@Component
public class StartupProcess {
	
	Logger logger = LogManager.getLogger(StartupProcess.class);
	SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
	
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
	
	/**
	 * Run at 6:00 AM
	 */
	@Scheduled(cron = "0 0 6 * * *")
	public void stopMainCorridorLight() {
		
		Hotel hotel = null;
		Optional<Hotel> hotelOptional = hotelRepository.findById(1L);
		if(hotelOptional.isPresent()) {
			hotel = hotelOptional.get();
		} else {
			return;
		}
		Long totalPowerConsume  = hotel.getPowerConsume();
		
		List<Long> mainCorridorIds = mainCorridorRepository.findAllId();
		List<Light> lights = lightRepository.findAllByMainCorridorIdIn(mainCorridorIds);

		for (Light light : lights) {
			light.setStatus(Status.OFF);
			totalPowerConsume -= light.getPowerConsume();
			lightRepository.save(light);
		}
		logger.info("Maximum Power Consume {}, current Power Consume {} ", hotel.getMaxPowerConsume(), totalPowerConsume);
		hotel.setPowerConsume(totalPowerConsume);
		hotelRepository.save(hotel);
	}
	
	/**
	 * Run at 6:00 PM
	 */
	@Scheduled(cron = "0 0 18 * * *")
	public void startMainCorridorLight() {
		Hotel hotel = null;
		Optional<Hotel> hotelOptional = hotelRepository.findById(1L);
		if(hotelOptional.isPresent()) {
			hotel = hotelOptional.get();
		} else {
			return;
		}
		Long totalPowerConsume  = hotel.getPowerConsume();
		
		List<Long> mainCorridorIds = mainCorridorRepository.findAllId();
		List<Light> lights = lightRepository.findAllByMainCorridorIdIn(mainCorridorIds);

		for (Light light : lights) {
			light.setStatus(Status.ON);
			totalPowerConsume += light.getPowerConsume();
			lightRepository.save(light);
		}
		logger.info("Maximum Power Consume {}, current Power Consume {} ", hotel.getMaxPowerConsume(), totalPowerConsume);
		hotel.setPowerConsume(totalPowerConsume);
		hotelRepository.save(hotel);
		//TODO : Manage hotel Maximum PowerConsume Limit
	}
	
	@org.springframework.context.event.EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
	public void init() throws ParseException {

		Long totalPowerConsume  = 0L;
		Hotel hotel = null;
		Optional<Hotel> hotelOptional = hotelRepository.findById(1L);
		if(hotelOptional.isPresent()) {
			hotel = hotelOptional.get();
		} else {
			return;
		}

		List<Long> subCorridorIds = subCorridorRepository.findAllId();
		List<Long> mainCorridorIds = mainCorridorRepository.findAllId();
		
		//default all AC ON
		List<AirConditioner> airConditioners = airConditionerRepository.findAll();
		for (AirConditioner airConditioner : airConditioners) {
			airConditioner.setStatus(Status.ON);
			totalPowerConsume +=airConditioner.getPowerConsume();
			airConditionerRepository.save(airConditioner);
		}

		//all main corridor lights ON between 6PM to 6 AM
		String currentTime = dateFormat.format(new Date());
		if (dateFormat.parse(currentTime).before(dateFormat.parse("06:00")) || dateFormat.parse(currentTime).after(dateFormat.parse("18:00"))) {
			List<Light> lights = lightRepository.findAllByMainCorridorIdIn(mainCorridorIds);
			for (Light light : lights) {
				light.setStatus(Status.ON);
				totalPowerConsume += light.getPowerConsume();
				lightRepository.save(light);
			}
		}
		logger.info("Maximum Power Consume {}, current Power Consume {} ", hotel.getMaxPowerConsume(), totalPowerConsume);
		//Hotel current status
		hotel.setMaxPowerConsume(new Long((mainCorridorIds.size() * 15) + (subCorridorIds.size() * 10)));
		hotel.setPowerConsume(totalPowerConsume);
		hotelRepository.save(hotel);
	}
}
