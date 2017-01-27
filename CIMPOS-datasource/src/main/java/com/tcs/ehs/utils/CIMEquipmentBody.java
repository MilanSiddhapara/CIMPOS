package com.tcs.ehs.utils;

import java.util.List;

import org.springframework.stereotype.Component;
@Component
public class CIMEquipmentBody {

	String Equipment;
	List<CIMBody> cimBodies;
	public String getEquipment() {
		return Equipment;
	}
	public void setEquipment(String equipment) {
		Equipment = equipment;
	}
	public List<CIMBody> getCimBodies() {
		return cimBodies;
	}
	public void setCimBodies(List<CIMBody> cimBodies) {
		this.cimBodies = cimBodies;
	}
	
	
}
