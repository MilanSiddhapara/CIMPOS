package com.tcs.ehs.utils;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

@Component
public class CIMResponseBody {

	String Equipment;
	//CIMBody cimBody;
	ArrayList<CIMBody> cimBodies;
	public ArrayList<CIMBody> getCimBodies() {
		return cimBodies;
	}
	public void setCimBodies(ArrayList<CIMBody> cimBodies) {
		this.cimBodies = cimBodies;
	}
	/*public CIMBody getCimBody() {
		return cimBody;
	}
	public void setCimBody(CIMBody cimBody) {
		this.cimBody = cimBody;
	}*/
	public String getEquipment() {
		return Equipment;
	}
	public void setEquipment(String equipment) {
		Equipment = equipment;
	}
	
	
	
}
