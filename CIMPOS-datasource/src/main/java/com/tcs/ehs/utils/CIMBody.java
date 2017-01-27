package com.tcs.ehs.utils;

public class CIMBody {
	int AvgValue;
	int BadValue;
	int GoodValue;
	int Parts;
	int Std;
	String Status;
	
	Long timestamp;
	public int getAvgValue() {
		return AvgValue;
	}
	public void setAvgValue(int avgValue) {
		AvgValue = avgValue;
	}
	public int getBadValue() {
		return BadValue;
	}
	public void setBadValue(int badValue) {
		BadValue = badValue;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public int getGoodValue() {
		return GoodValue;
	}
	public void setGoodValue(Integer integer) {
		GoodValue = integer;
	}
	public int getParts() {
		return Parts;
	}
	public void setParts(int parts) {
		Parts = parts;
	}
	public int getStd() {
		return Std;
	}
	public void setStd(int std) {
		Std = std;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
