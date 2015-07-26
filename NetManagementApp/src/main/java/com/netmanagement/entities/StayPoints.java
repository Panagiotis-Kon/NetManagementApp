package com.netmanagement.entities;

public class StayPoints {
	
	private double lat;
	private double lon;
	private String Tstart;
	private String Tend;
	
	public void setAll(double lat, double lon, String Tstart, String Tend){
		this.lat = lat;
		this.lon = lon;
		this.Tstart = Tstart;
		this.Tend = Tend;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getTstart() {
		return Tstart;
	}

	public void setTstart(String tstart) {
		Tstart = tstart;
	}

	public String getTend() {
		return Tend;
	}

	public void setTend(String tend) {
		Tend = tend;
	}

}
