package com.processPensionMicroservice.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.processPensionMicroservice.exception.AuthorizationException;
import com.processPensionMicroservice.exception.PensionerDetailsException;
import com.processPensionMicroservice.exception.PensionerNotFoundException;
import com.processPensionMicroservice.model.PensionDetail;
import com.processPensionMicroservice.model.PensionerInput;
import com.processPensionMicroservice.model.ProcessInput;
import com.processPensionMicroservice.model.ProcessPensionResponse;
import com.processPensionMicroservice.service.ProcessPensionServiceImpl;

import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/process")
@CrossOrigin
public class processPensionController {
	private static final Logger Log = LoggerFactory.getLogger(processPensionController.class);

	@Autowired
	ProcessPensionServiceImpl processPensionServiceImpl;

	/*
	 * POST: localhost:8084/process/pensionDetail
	 * 
	 * { "name" : "Padmini", "dateOfBirth" : "2000-08-30", "pan" : "PCASD1234Q",
	 * "aadharNumber" : 102233445566, "pensionType" : "family" }
	 */

	@PostMapping("/PensionDetail")
	public PensionDetail getPensionDetails(@RequestHeader("Authorization") String header,
			@Valid @RequestBody PensionerInput pensionerInput)
			throws PensionerNotFoundException, PensionerDetailsException, AuthorizationException, RetryableException {
		// modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		Log.info("start getPensionDetails");

		Log.debug("" + pensionerInput);
		return processPensionServiceImpl.getPensionDetails(header, pensionerInput);
	}

	/*
	 * POST: localhost:8084/process/ProcessPension
	 * 
	 * { "aadharNumber" : 112233445566, "pensionAmount": 23955.0, "serviceCharge":
	 * 500 }
	 */

	@PostMapping("/ProcessPension")
	public ProcessPensionResponse getcode(@RequestHeader("Authorization") String header,
			@Valid @RequestBody ProcessInput processInput)
			throws AuthorizationException, IOException, PensionerNotFoundException {
		Log.info("start processPension");
		return processPensionServiceImpl.getcode(header, processInput);

	}

}
