package com.netmanagement.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	private Date timestamp;
	
	public void setAll(String[] Data) {
		id=Data[0];
		user=Data[1];
		Ulatitude=Double.parseDouble(Data[2]);
		Ulongtitude=Double.parseDouble(Data[3]);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		try {

			Date date = formatter.parse(Data[4]);
			timestamp=date;

		} catch (ParseException e) {
			e.printStackTrace();
		}
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
	

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(GPS arg0) {
		if (getTimestamp() == null || arg0.getTimestamp() == null)
		      return 0;
		    return getTimestamp().compareTo(arg0.getTimestamp());
	}
	
	@Override
	public String toString() {
		return id + " " + Ulatitude + " " + Ulongtitude+ " " + timestamp;
	}
}
