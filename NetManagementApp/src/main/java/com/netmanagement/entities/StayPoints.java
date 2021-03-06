package com.netmanagement.entities;

import java.util.Date;

import org.apache.commons.math3.ml.clustering.Clusterable;

/**
 * 
 * Entity for the Stay Points
 * 
 */


public class StayPoints implements Clusterable{
	
	private double lat;
	private double lon;
	private Date Tstart;
	private Date Tend;
	private int visited=0; //0 : not visited , 1 : visited
	
	public void setAll(double lat, double lon, Date Tstart, Date Tend){
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

	
	public Date getTstart() {
		return Tstart;
	}

	public void setTstart(Date tstart) {
		Tstart = tstart;
	}

	public Date getTend() {
		return Tend;
	}

	public void setTend(Date tend) {
		Tend = tend;
	}

	public int getVisited() {
		return visited;
	}

	public void setVisited(int visited) {
		this.visited = visited;
	}
	
	@Override
	public double[] getPoint(){
		double point[] = new double[2];
		point[0]=this.getLat();
		point[1]=this.getLon();
		return point;
	}

}
