package com.netmanagement.entities;



public class Battery {

	private String id;
	private String user;
	private int level;
	private int plugged;
	private int temperature;
	private int voltage;
	private String timestamp;
	
	public void setAll(String[] Data) {
		id=Data[0];
		user=Data[1];
		level=Integer.parseInt(Data[2]);
		plugged=Integer.parseInt(Data[3]);
		temperature=Integer.parseInt(Data[4]);
		voltage=Integer.parseInt(Data[4]);
		timestamp=Data[6];
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
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPlugged() {
		return plugged;
	}
	public void setPlugged(int plugged) {
		this.plugged = plugged;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public int getVoltage() {
		return voltage;
	}
	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
