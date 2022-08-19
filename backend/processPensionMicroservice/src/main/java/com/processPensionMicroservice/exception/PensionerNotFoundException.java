package com.processPensionMicroservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PensionerNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public PensionerNotFoundException(String msg) {
		super(msg);
	}
}
