package com.netmanagement.entities;

public class APResults {
	String BSSID;
	private double APlatitude;
	private double APlongtitude;
	public String getBSSID() {
		return BSSID;
	}
	public void setBSSID(String bSSID) {
		BSSID = bSSID;
	}

	public double getAPlatitude() {
		return APlatitude;
	}
	public void setAPlatitude(double aPlatitude) {
		APlatitude = aPlatitude;
	}
	public double getAPlongtitude() {
		return APlongtitude;
	}
	public void setAPlongtitude(double aPlongtitude) {
		APlongtitude = aPlongtitude;
	}
	
}
