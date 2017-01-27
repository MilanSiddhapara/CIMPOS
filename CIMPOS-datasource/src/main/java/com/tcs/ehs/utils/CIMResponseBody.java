package com.tcs.ehs.utils;

import org.springframework.stereotype.Component;

@Component
public class CIMResponseBody {

	String Equipment;
	CIMBody cimBody;
	
	public CIMBody getCimBody() {
		return cimBody;
	}
	public void setCimBody(CIMBody cimBody) {
		this.cimBody = cimBody;
	}
	public String getEquipment() {
		return Equipment;
	}
	public void setEquipment(String equipment) {
		Equipment = equipment;
	}
	
	
	
}
