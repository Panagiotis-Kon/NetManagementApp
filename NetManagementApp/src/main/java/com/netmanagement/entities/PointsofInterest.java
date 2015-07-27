package com.netmanagement.entities;

import java.util.ArrayList;

public class PointsofInterest {
	private double startlat;
	private double startlon;
	private double endlat;
	private double endlon;
	private int noise=0; //0 : poi , 1 : noise
	private int numofPoints=0;
	private ArrayList<StayPoints> points = new ArrayList<StayPoints>();
	
	public void setAll(double startlat, double startlon, double endlat, double endlon, int noise, int numofPoints){
		this.startlat = startlat;
		this.startlon = startlon;
		this.endlat = endlat;
		this.endlon = endlon;
		this.noise = noise;
		this.numofPoints = numofPoints;
	}

	public double getStartlat() {
		return startlat;
	}

	public void setStartlat(double startlat) {
		this.startlat = startlat;
	}

	public double getStartlon() {
		return startlon;
	}

	public void setStartlon(double startlon) {
		this.startlon = startlon;
	}

	public double getEndlat() {
		return endlat;
	}

	public void setEndlat(double endlat) {
		this.endlat = endlat;
	}

	public double getEndlon() {
		return endlon;
	}

	public void setEndlon(double endlon) {
		this.endlon = endlon;
	}

	public int getNoise() {
		return noise;
	}

	public void setNoise(int noise) {
		this.noise = noise;
	}

	public int getNumofPoints() {
		return numofPoints;
	}

	public void setNumofPoints(int numofPoints) {
		this.numofPoints = numofPoints;
	}

	public ArrayList<StayPoints> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<StayPoints> points) {
		this.points = points;
	}

}
