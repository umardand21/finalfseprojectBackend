package com.processPensionMicroservice.service;

import java.io.IOException;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.processPensionMicroservice.client.AuthorizationClient;
import com.processPensionMicroservice.client.PensionDisbursementClient;
import com.processPensionMicroservice.client.PensionerDetailClient;
import com.processPensionMicroservice.controller.processPensionController;
import com.processPensionMicroservice.exception.AuthorizationException;
import com.processPensionMicroservice.exception.PensionerDetailsException;
import com.processPensionMicroservice.exception.PensionerNotFoundException;
import com.processPensionMicroservice.model.PensionDetail;
import com.processPensionMicroservice.model.PensionerDetail;
import com.processPensionMicroservice.model.PensionerInput;
import com.processPensionMicroservice.model.ProcessInput;
import com.processPensionMicroservice.model.ProcessPensionInput;
import com.processPensionMicroservice.model.ProcessPensionResponse;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProcessPensionServiceImpl implements ProcessPensionService {
	private static final Logger log = LoggerFactory.getLogger(ProcessPensionServiceImpl.class);

	@Autowired
	private PensionerDetailClient pensionerDetailClient;
	@Autowired
	private PensionDisbursementClient pensionDisbursementClient;
	@Autowired
	private AuthorizationClient authorizationClient;

	@Autowired
	private ProcessPensionService processPensionService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private static final Logger Log = LoggerFactory.getLogger(processPensionController.class);
	
	public double getresult(PensionerDetail pensionerDetail) {
		double pensionAmount = 0.0;

		if (pensionerDetail.getPensionType().equalsIgnoreCase("self"))
			pensionAmount = (pensionerDetail.getSalary() * Constants.SELF_SERVICE_CHARGE
					+ pensionerDetail.getAllowance());
		else if (pensionerDetail.getPensionType().equalsIgnoreCase("family"))
			pensionAmount = (pensionerDetail.getSalary() * Constants.FAMILY_SERVICE_CHARGE
					+ pensionerDetail.getAllowance());
		log.info("" + pensionAmount);
		return pensionAmount;

	}

	public double getServiceCharge(String bankType) {
		if (bankType.equalsIgnoreCase("public")) {
			return 500.0;
		} else if (bankType.equalsIgnoreCase("private")) {
			return 550.0;
		} else {
			return -1;
		}
	}

	@Override
	public ProcessPensionResponse getcode(String header, @Valid ProcessInput processInput)
			throws AuthorizationException, PensionerNotFoundException, IOException {
		if (authorizationClient.authorizeRequest(header)) {
			PensionerDetail pensionerDetail = pensionerDetailClient.getPensionerDetailByAadhaar(header,
					processInput.getAadharNumber());
			if (pensionerDetail.getName() == null) {
				throw new PensionerNotFoundException("Pensioner with given aadhaar number not found");
			}
			double serviceCharge = processPensionService.getServiceCharge(pensionerDetail.getBank().getBankType());
			ProcessPensionInput processPensionInput = new ProcessPensionInput(processInput.getAadharNumber(),
					processInput.getPensionAmount(), serviceCharge);

			ProcessPensionResponse responseCode = null;
			for (int i = 1; i <= 3; i++) {
				responseCode = pensionDisbursementClient.getcode(header, processPensionInput);
				if (responseCode.getPensionStatusCode() == 10) {
					Log.info("End ProcessPension");
					return responseCode;
				}
				Log.info("retrying");
			}

			Log.info("End ProcessPension");
			return responseCode;

		} else {
			throw new AuthorizationException("User not authorized");
		}

	}

	@Override
	public PensionDetail getPensionDetails(String header, @Valid PensionerInput pensionerInput)
			throws PensionerNotFoundException, PensionerDetailsException, AuthorizationException {
		if (authorizationClient.authorizeRequest(header)) {
			PensionerDetail pensionerDetail = pensionerDetailClient.getPensionerDetailByAadhaar(header,
					pensionerInput.getAadharNumber());
			Log.info(pensionerDetail.getName());
			if (pensionerDetail.getName() == null) {
				throw new PensionerNotFoundException("Pensioner with given aadhar not found");
			}
			PensionerDetail receivedPensionerDetail = modelMapper.map(pensionerInput, PensionerDetail.class);
			if (pensionerDetail.compareTo(receivedPensionerDetail) < 0) {
				throw new PensionerDetailsException("Incorrect Pensioner Details.");
			}

			double pensionAmount = processPensionService.getresult(pensionerDetail);
			Log.info("" + pensionAmount);
			PensionDetail pensionDetail = modelMapper.map(pensionerDetail, PensionDetail.class);
			pensionDetail.setPensionAmount(pensionAmount);
			Log.info("" + pensionDetail.getPensionAmount());
			return pensionDetail;

		} else {
			throw new AuthorizationException("User not authorized");
		}
	}
}
