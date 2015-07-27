package com.netmanagement.entities;

public class PointsofInterest {
	private double startlat;
	private double startlon;
	private double endlat;
	private double endlon;
	private int visited=0; //0 : not visited , 1 : visited
	
	public void setAll(double startlat, double startlon, double endlat, double endlon, int visited){
		this.startlat = startlat;
		this.startlon = startlon;
		this.endlat = endlat;
		this.endlon = endlon;
		this.visited = visited;
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

	public int getVisited() {
		return visited;
	}

	public void setVisited(int visited) {
		this.visited = visited;
	}

}
