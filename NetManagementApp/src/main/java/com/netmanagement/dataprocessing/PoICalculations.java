package com.netmanagement.dataprocessing;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.netmanagement.csvdatasets.ParseGPS;
import com.netmanagement.entities.GPS;
import com.netmanagement.entities.PointsofInterest;
import com.netmanagement.entities.StayPoints;

public class PoICalculations {
    private static PoICalculations PoICalculationsinstance = null;
    private double esp;
    private int minPts;
    private double[][] matrix = null;
    ArrayList<ArrayList<StayPoints>> neighbors = new ArrayList<ArrayList<StayPoints>>();
	
	private PoICalculations(){}
	
	public static PoICalculations getInstance(){
		if(PoICalculationsinstance == null){
			PoICalculationsinstance = new PoICalculations();
		}
		return PoICalculationsinstance;
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

	public ArrayList<PointsofInterest> CalculatePoI(String startDate, String endDate, String Tmin, String Tmax, Double Dmax){
		HashMap<String, ArrayList<GPS>> hgps = ParseGPS.getInstance().getHap();
		ArrayList<StayPoints> Lsp = new ArrayList<StayPoints>();
		if (!hgps.isEmpty()){
			Set<?> set = hgps.entrySet();
			Iterator<?> it = set.iterator();
			while(it.hasNext()){
				Map.Entry me = (Map.Entry)it.next();
				//System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<GPS> array = GPSCalculations.getInstance().searchUser(me.getKey().toString(), startDate, endDate);
				Lsp.addAll(GPSCalculations.getInstance().findStayPoints(array, Tmin, Tmax, Dmax));
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
		ArrayList<PointsofInterest> poilist = new ArrayList<PointsofInterest>();
		ArrayList<PointsofInterest> noiselist = new ArrayList<PointsofInterest>();
		
		int C=-1;
		for (int i=0;i<Lsp.size();i++){
			if (Lsp.get(i).getVisited()==1){
				continue;
			}
			Lsp.get(i).setVisited(1);
			neighbors.get(i).addAll(regionQuery(Lsp, i));
			if (neighbors.get(i).size() < minPts){
				//Mark Lsp.get(i) as noise
				PointsofInterest poi = new PointsofInterest();
				poi.setAll(Lsp.get(i).getLat(), Lsp.get(i).getLon(), Lsp.get(i).getLat(), Lsp.get(i).getLon(), 1, 1);
				noiselist.add(poi);
			}
			else {
				C++;
				expandCluster(poilist,Lsp,i,C);
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
	
	void expandCluster(ArrayList<PointsofInterest> Clusters, ArrayList<StayPoints> Lsp,int spointpos, int pos){
		ArrayList<ArrayList<StayPoints>> neighbor = new ArrayList<ArrayList<StayPoints>>();
		if (Clusters.size()>=pos){
			PointsofInterest point = updatePoint(Clusters.get(pos), Lsp.get(spointpos));
			Clusters.get(pos).setAll(point.getStartlat(), point.getStartlon(), point.getEndlat(), point.getEndlon(), point.getNoise(), point.getNumofPoints());
		}
		else {
			PointsofInterest point = new PointsofInterest();
			Clusters.add(point);
			point = updatePoint(Clusters.get(Clusters.size()-1), Lsp.get(spointpos));
			pos = Clusters.size()-1;
			Clusters.get(pos).setAll(point.getStartlat(), point.getStartlon(), point.getEndlat(), point.getEndlon(), point.getNoise(), point.getNumofPoints());
		}
		Clusters.get(pos).getPoints().add(Lsp.get(spointpos));
		for (int i=0;i<neighbors.size();i++){
			if (neighbors.get(pos).get(i).getVisited()==0){
				neighbors.get(pos).get(i).setVisited(1);
				neighbors.get(i).addAll(regionQuery(Lsp, pos));
				if (neighbors.get(i).size()>=minPts){
					neighbors.get(pos).addAll(neighbors.get(i));
				}
			}
			int found = 0;
			for (int k=0;k<Clusters.size();k++){
				if (Clusters.get(k).getPoints().contains(neighbors.get(pos).get(i))){
					found = 1;
					break;
				}
			}
			if (found==0){
				Clusters.get(pos).getPoints().add(neighbors.get(pos).get(i));
			}
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
