package com.netmanagement.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * Entity for Base Stations
 * 
 */

public class BaseStations {
	
	private String id;
	private String user;
	private String Operator;
	private int mcc;
	private int mnc;
	private int cid;
	private int lac;
	private String BSlatitude;
	private String BSlongtitude;
	private Date timestamp;
	
	public void setAll(String[] Data) {
		id=Data[0];
		user=Data[1];
		Operator=Data[2];
		mcc=Integer.parseInt(Data[3]);
		mnc=Integer.parseInt(Data[4]);
		cid=Integer.parseInt(Data[5]);
		lac=Integer.parseInt(Data[6]);
		BSlatitude=Data[7];
		BSlongtitude=Data[8];
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		try {

			Date date = formatter.parse(Data[9]);
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
	public String getOperator() {
		return Operator;
	}
	public void setOperator(String operator) {
		Operator = operator;
	}
	public int getMcc() {
		return mcc;
	}
	public void setMcc(int mcc) {
		this.mcc = mcc;
	}
	public int getMnc() {
		return mnc;
	}
	public void setMnc(int mnc) {
		this.mnc = mnc;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getLac() {
		return lac;
	}
	public void setLac(int lac) {
		this.lac = lac;
	}
	public String getBSlatitude() {
		return BSlatitude;
	}
	public void setBSlatitude(String bSlatitude) {
		BSlatitude = bSlatitude;
	}
	public String getBSlongtitude() {
		return BSlongtitude;
	}
	public void setBSlongtitude(String bSlongtitude) {
		BSlongtitude = bSlongtitude;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return id + " " + Operator + " " + mcc + " " + mnc + " "
				+ cid + " " + lac + " " + BSlatitude + " " + BSlongtitude +" " + 
				 timestamp;
	}
	
}
