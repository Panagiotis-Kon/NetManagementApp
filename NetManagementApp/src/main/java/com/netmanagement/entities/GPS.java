package com.netmanagement.entities;

/**
 * 
 * Entity for the GPS
 * 
 */

public class GPS implements Comparable<GPS>{

	private String id;
	private String user;
	private double Ulatitude;
	private double Ulongtitude;
	private String timestamp;
	
	public void setAll(String[] Data) {
		id=Data[0];
		user=Data[1];
		Ulatitude=Double.parseDouble(Data[2]);
		Ulongtitude=Double.parseDouble(Data[3]);
		timestamp=Data[4];
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
	public double getUlatitude() {
		return Ulatitude;
	}
	public void setUlatitude(double ulatitude) {
		Ulatitude = ulatitude;
	}
	public double getUlongtitude() {
		return Ulongtitude;
	}
	public void setUlongtitude(double ulongtitude) {
		Ulongtitude = ulongtitude;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(GPS arg0) {
		if (getTimestamp() == null || arg0.getTimestamp() == null)
		      return 0;
		    return getTimestamp().compareTo(arg0.getTimestamp());
	}
}
