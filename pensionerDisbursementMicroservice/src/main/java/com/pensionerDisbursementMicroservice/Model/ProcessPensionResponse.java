package com.pensionerDisbursementMicroservice.Model;


import lombok.EqualsAndHashCode;
import lombok.Getter;

import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode
public class ProcessPensionResponse {

	public ProcessPensionResponse() {
		//super();
		// TODO Auto-generated constructor stub
	}

	public ProcessPensionResponse(int pensionStatusCode) {
		super();
		this.pensionStatusCode = pensionStatusCode;
	}

	public int getPensionStatusCode() {
		return pensionStatusCode;
	}

	public void setPensionStatusCode(int pensionStatusCode) {
		this.pensionStatusCode = pensionStatusCode;
	}

	private int pensionStatusCode;

}
