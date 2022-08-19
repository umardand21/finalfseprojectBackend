package com.pensionerDisbursementMicroservice.service;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.pensionerDisbursementMicroservice.Model.PensionerDetail;
import com.pensionerDisbursementMicroservice.Model.ProcessPensionInput;
import com.pensionerDisbursementMicroservice.Model.ProcessPensionResponse;

public interface PensionDisbursementService {
	
	public ProcessPensionResponse code(PensionerDetail pensionerDetail, ProcessPensionInput processPensionInput);
	public ProcessPensionResponse getcode(@RequestHeader("Authorization") String header,
			@Valid @RequestBody ProcessPensionInput processPensionInput) throws Exception;
}
