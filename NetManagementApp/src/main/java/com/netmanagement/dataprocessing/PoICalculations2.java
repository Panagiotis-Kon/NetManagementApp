package com.netmanagement.dataprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.netmanagement.csvdatasets.ParseGPS;
import com.netmanagement.entities.GPS;
import com.netmanagement.entities.PointsofInterest;
import com.netmanagement.entities.StayPoints;

public class PoICalculations2 {
	private static PoICalculations2 PoICalculationsinstance = null;
    private double esp;
    private int minPts;
    private double[][] matrix = null;
    String startDate;
    String endDate;
    String Tmin;
    String Tmax;
    Double Dmax;
    ArrayList<ArrayList<StayPoints>> neighbors = new ArrayList<ArrayList<StayPoints>>();
	
	private PoICalculations2(){}
	
	public static PoICalculations2 getInstance(){
		if(PoICalculationsinstance == null){
			PoICalculationsinstance = new PoICalculations2();
		}
		return PoICalculationsinstance;
	}
	
	public void setAll(String startDate, String endDate, String Tmin, String Tmax, Double Dmax, Double esp, int minPts){
		this.startDate = startDate;
		this.endDate = endDate;
		this.Tmin = Tmin;
		this.Tmax = Tmax;
		this.Dmax = Dmax;
		this.esp = esp;
		this.minPts = minPts;
	}
	
	public double getEsp() {
		return esp;
	}

	public void setEsp(double esp) {
		this.esp = esp;
	}

	public int getMinPts() {
		return minPts;
	}

	public void setMinPts(int minPts) {
		this.minPts = minPts;
	}

	public double[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTmin() {
		return Tmin;
	}

	public void setTmin(String tmin) {
		Tmin = tmin;
	}

	public String getTmax() {
		return Tmax;
	}

	public void setTmax(String tmax) {
		Tmax = tmax;
	}

	public Double getDmax() {
		return Dmax;
	}

	public void setDmax(Double dmax) {
		Dmax = dmax;
	}
	
	public void neighborsInit(int size){
		for (int i=0;i<size;i++){
			neighbors.add(new ArrayList<StayPoints>());
		}
	}

	public ArrayList<PointsofInterest> CalculatePoI(){
		HashMap<String, ArrayList<GPS>> hgps = ParseGPS.getInstance().getHap();
		ArrayList<StayPoints> Lsp = new ArrayList<StayPoints>();
		if (!hgps.isEmpty()){
			Set<?> set = hgps.entrySet();
			Iterator<?> it = set.iterator();
			while(it.hasNext()){
				Map.Entry me = (Map.Entry)it.next();
				//System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<GPS> array = GPSCalculations.getInstance().searchUser(me.getKey().toString(), startDate, endDate);
				ArrayList<StayPoints> temp = GPSCalculations.getInstance().findStayPoints(array, Tmin, Tmax, Dmax);
				if (!temp.isEmpty()){
					Lsp.addAll(temp);
				}
			}
		}
		generateDistanceMatrix(Lsp);
		return DBSCAN(Lsp);
	}
	
	public void generateDistanceMatrix(ArrayList<StayPoints> Lsp){
		double[][] matrix = new double[Lsp.size()][Lsp.size()];
		for (int i=0;i<Lsp.size();i++){
			for (int j=0;j<Lsp.size();j++){
				matrix[i][j]=Distance(Lsp.get(i), Lsp.get(j));
			}
		}
		setMatrix(matrix);
	}
	
	public ArrayList<PointsofInterest> DBSCAN(ArrayList<StayPoints> Lsp){
		ArrayList<PointsofInterest> poilist = new ArrayList<PointsofInterest>(); //cluster
		ArrayList<PointsofInterest> noiselist = new ArrayList<PointsofInterest>();
		ArrayList<StayPoints> visited = new ArrayList<StayPoints>();
		final int size = Lsp.size();
		neighborsInit(size);
		int c=0;
		for (int i=0;i<size;i++){
			if (visited.contains(Lsp.get(i))){
				continue;
			}
			visited.add(Lsp.get(i));
			neighbors.get(i).addAll(regionQuery(Lsp, i));
			if (neighbors.get(i).size()>minPts){
				poilist.add(new PointsofInterest());
				poilist.get(poilist.size()-1).getPoints().add(Lsp.get(i));
				updatePoint(poilist.get(c), Lsp.get(i));
				expandCluster(poilist.get(poilist.size()-1), Lsp, visited, neighbors.get(i));
				c++;
			}
		}
		return poilist;
	}
	
	double Distance(StayPoints sp1, StayPoints sp2){
		//Find Pythagorean distance calculation between given variables
		double distance=0,lat=0,lon=0;
		if (sp1.getLat()>sp2.getLat()){
			lat=sp1.getLat()-sp2.getLat();
		}
		else {
			lat=sp2.getLat()-sp1.getLat();
		}
		lat=lat*lat;
		if (sp1.getLon()>sp2.getLon()){
			lon=sp1.getLon()-sp2.getLon();
		}
		else {
			lon=sp2.getLon()-sp1.getLon();
		}
		lon=lon*lon;
		distance=Math.sqrt(lat+lon);
		return distance;
	}
	
	ArrayList<StayPoints> regionQuery(ArrayList<StayPoints> Lsp, int pos){
		ArrayList<StayPoints> list = new ArrayList<StayPoints>();
		for (int j=0;j<Lsp.size();j++){
			if (matrix[pos][j] <= esp){
				list.add(Lsp.get(j));
			}
		}
		return list;
	}
	
	void expandCluster(PointsofInterest cluster, ArrayList<StayPoints> Lsp, ArrayList<StayPoints> visited, ArrayList<StayPoints> neighbours){
		ArrayList<StayPoints> currentneighbours = new ArrayList<StayPoints>();
		currentneighbours.addAll(neighbours);
		for (int i=0;i<neighbours.size();i++){
			if (visited.contains(neighbours.get(i))){
				continue;
			}
			visited.add(neighbours.get(i));
			ArrayList<StayPoints> extendedneighbours = regionQuery(Lsp, Lsp.indexOf(neighbours.get(i)));
			currentneighbours.addAll(extendedneighbours);
			neighbours.addAll(extendedneighbours);
			cluster.getPoints().add(neighbours.get(i));
			updatePoint(cluster, neighbours.get(i));
		}
	}
	
	PointsofInterest updatePoint(PointsofInterest point, StayPoints spoint){
		if (point.getNumofPoints()==0){
			point.setAll(spoint.getLat(), spoint.getLon(), spoint.getLat(), spoint.getLon(), 1,point.getNumofPoints()+1);
		}
		else {
			if (point.getStartlat()>spoint.getLat()){
				point.setStartlat(spoint.getLat());
			}
			if (point.getEndlat()<spoint.getLat()){
				point.setEndlat(spoint.getLat());
			}
			if (point.getStartlon()>spoint.getLon()){
				point.setStartlon(spoint.getLon());
			}
			if (point.getEndlon()<spoint.getLon()){
				point.setEndlon(spoint.getLon());
			}
		}
		return point;
	}

}
