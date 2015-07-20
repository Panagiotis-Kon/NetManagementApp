package com.netmanagement.entities;

import java.util.Date;

public class GPS {

	private String id;
	private String user;
	private double Ulatitude;
	private double Ulongtitude;
	private String timestamp;
	
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
}
