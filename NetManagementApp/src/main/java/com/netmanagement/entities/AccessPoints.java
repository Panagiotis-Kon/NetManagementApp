package com.netmanagement.entities;

import java.util.Date;



public class AccessPoints implements Comparable<AccessPoints>{
	
	private String id;
	private String user;
	private String ssid;
	private String bssid;
	private int rssi;
	private int frequency;
	private double APlatitude;
	private double APlongtitude;
	private String timestamp;
	
	public void setAll(String[] Data) {
		id=Data[0];
		user=Data[1];
		ssid=Data[2];
		bssid=Data[3];
		rssi=Integer.parseInt(Data[4]);
		frequency=Integer.parseInt(Data[5]);
		APlatitude=Double.parseDouble(Data[6]);
		APlongtitude=Double.parseDouble(Data[7]);
		timestamp=Data[8];
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
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getSsid() {
		return ssid;
	}
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	public String getBssid() {
		return bssid;
	}
	public void setBssid(String bssid) {
		this.bssid = bssid;
	}
	public int getRssi() {
		return rssi;
	}
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	 @Override
	  public int compareTo(AccessPoints o) {
	    if (getTimestamp() == null || o.getTimestamp() == null)
	      return 0;
	    return getTimestamp().compareTo(o.getTimestamp());
	  }
	
}
