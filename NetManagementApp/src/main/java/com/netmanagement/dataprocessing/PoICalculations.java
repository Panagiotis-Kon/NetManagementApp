package com.netmanagement.dataprocessing;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import com.netmanagement.csvdatasets.ParseGPS;
import com.netmanagement.entities.GPS;
import com.netmanagement.entities.PointsofInterest;
import com.netmanagement.entities.StayPoints;

public class PoICalculations {
	private static PoICalculations PoICalculationsinstance = null;
	private double esp;
	private int minPts;
	private double[][] matrix = null;
	private Date startDate;
	private Date endDate;
	private String Tmin;
	private String Tmax;
	private Double Dmax;
	private ArrayList<ArrayList<StayPoints>> neighbors = null;
	private int option = 0; // 0 : DBSCAN created by authors, 1 : DBSCAN from
							// http://commons.apache.org/proper/commons-math/jacoco/index.html

	private PoICalculations() {
	}

	public static PoICalculations getInstance() {
		if (PoICalculationsinstance == null) {
			PoICalculationsinstance = new PoICalculations();
		}
		return PoICalculationsinstance;
	}

	public void setAll(Date startDate, Date endDate, String Tmin,
			String Tmax, Double Dmax, Double esp, int minPts) {
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

	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
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

	public int getOption() {
		return option;
	}

	public void setOption(int option) {
		this.option = option;
	}

	public void neighborsInit(int size) {
		neighbors = new ArrayList<ArrayList<StayPoints>>();
		for (int i = 0; i < size; i++) {
			neighbors.add(new ArrayList<StayPoints>());
		}
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<PointsofInterest> CalculatePoI() {
		// Calculate Points of Interests. First find stay points and give the
		// array list to DBSCAN Algorithm
		HashMap<String, ArrayList<GPS>> hgps = ParseGPS.getInstance().getHap();
		ArrayList<StayPoints> Lsp = new ArrayList<StayPoints>();
		if (!hgps.isEmpty()) {
			Set<?> set = hgps.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				
				ArrayList<GPS> array = GPSCalculations.getInstance().searchUser(me.getKey().toString(), startDate, endDate);
				ArrayList<StayPoints> temp = GPSCalculations.getInstance()
						.findStayPoints(array, Tmin, Tmax, Dmax);
				if (!temp.isEmpty()) {
					Lsp.addAll(temp);
				}
			}
		}
		if (option == 0) {
			generateDistanceMatrix(Lsp);
			return DBSCAN(Lsp);
		} else {
			
			return DBSCAN_apache(Lsp);
		}

	}

	public void generateDistanceMatrix(ArrayList<StayPoints> Lsp) {
		double[][] matrix = new double[Lsp.size()][Lsp.size()];
		for (int i = 0; i < Lsp.size(); i++) {
			for (int j = 0; j < Lsp.size(); j++) {
				matrix[i][j] = Distance(Lsp.get(i), Lsp.get(j));
			}
		}
		setMatrix(matrix);
	}

	/**
	 * Apache DBSCAN Implementation
	 * 
	 * 
	 */
	public ArrayList<PointsofInterest> DBSCAN_apache(ArrayList<StayPoints> Lsp) {
		ArrayList<PointsofInterest> poilist = new ArrayList<PointsofInterest>(); // cluster
		DBSCANClusterer<StayPoints> clusterer = new DBSCANClusterer<StayPoints>(
				esp, minPts);
		List<Cluster<StayPoints>> clusteredPoints = clusterer.cluster(Lsp);

		for (Cluster<StayPoints> iter : clusteredPoints) {
			List<StayPoints> points = iter.getPoints();
			double lat1, lon1, lat2, lon2;

			lat1 = lat2 = points.get(0).getLat();
			lon1 = lon2 = points.get(0).getLon();
			for (StayPoints iter2 : points) {
				if (iter2.getLat() < lat1)
					lat1 = iter2.getLat();
				if (iter2.getLat() > lat2)
					lat2 = iter2.getLat();
				if (iter2.getLon() < lon1)
					lon1 = iter2.getLon();
				if (iter2.getLon() > lon2)
					lon2 = iter2.getLon();
			}
			PointsofInterest poi = new PointsofInterest();
			poi.setAll(lat1, lon1, lat2, lon2, 0, 0);
			poilist.add(poi);
		}
		return poilist;
	}

	/**
	 * 
	 * Our DBSCAN Algorithm ( wikipedia's pseudocode )
	 * 
	 */
	
	public ArrayList<PointsofInterest> DBSCAN(ArrayList<StayPoints> Lsp) {
		// DBSCAN Algorithm
		ArrayList<PointsofInterest> poilist = new ArrayList<PointsofInterest>(); // cluster
		ArrayList<StayPoints> visited = new ArrayList<StayPoints>();
		final int size = Lsp.size();
		neighborsInit(size);
		int c = 0;
		for (int i = 0; i < size; i++) {
			if (visited.contains(Lsp.get(i))) {
				continue;
			}
			visited.add(Lsp.get(i));
			neighbors.get(i).addAll(regionQuery(Lsp, i));
			if (neighbors.get(i).size() > minPts) {
				poilist.add(new PointsofInterest());
				poilist.get(poilist.size() - 1).getPoints().add(Lsp.get(i));
				updatePoint(poilist.get(c), Lsp.get(i));
				expandCluster(poilist.get(poilist.size() - 1), Lsp, visited,
						neighbors.get(i));
				c++;
			}
		}
		return poilist;
	}

	double Distance(StayPoints sp1, StayPoints sp2) {
		// Find Pythagorean distance calculation between given variables
		double distance = 0, lat = 0, lon = 0;
		if (sp1.getLat() > sp2.getLat()) {
			lat = sp1.getLat() - sp2.getLat();
		} else {
			lat = sp2.getLat() - sp1.getLat();
		}
		lat = lat * lat;
		if (sp1.getLon() > sp2.getLon()) {
			lon = sp1.getLon() - sp2.getLon();
		} else {
			lon = sp2.getLon() - sp1.getLon();
		}
		lon = lon * lon;
		distance = Math.sqrt(lat + lon);
		return distance;
	}

	/*
	 * double Distance(StayPoints sp1, StayPoints sp2){ //Find distance
	 * calculation between given variables from
	 * http://www.movable-type.co.uk/scripts/latlong.html double
	 * glat=sp1.getLat()*Math.PI/180; double alat=sp2.getLat()*Math.PI/180;
	 * double glon=sp1.getLon()*Math.PI/180; double
	 * alon=sp2.getLon()*Math.PI/180; double R = 6371000; double df = alat -
	 * glat; double dl = alon - glon; double a =
	 * Math.sin(df/2)*Math.sin(df/2)+Math
	 * .cos(glat)*Math.cos(alat)*Math.sin(dl/2)*Math.sin(dl/2); double c =
	 * 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); double d=R*c; return
	 * Math.abs(d); }
	 */

	ArrayList<StayPoints> regionQuery(ArrayList<StayPoints> Lsp, int pos) {
		ArrayList<StayPoints> list = new ArrayList<StayPoints>();
		for (int j = 0; j < Lsp.size(); j++) {
			if (matrix[pos][j] <= esp) {
				list.add(Lsp.get(j));
			}
		}
		return list;
	}

	void expandCluster(PointsofInterest cluster, ArrayList<StayPoints> Lsp,
			ArrayList<StayPoints> visited, ArrayList<StayPoints> neighbours) {
		ArrayList<StayPoints> currentneighbours = new ArrayList<StayPoints>();
		currentneighbours.addAll(neighbours);
		for (int i = 0; i < neighbours.size(); i++) {
			if (visited.contains(neighbours.get(i))) {
				continue;
			}
			visited.add(neighbours.get(i));
			ArrayList<StayPoints> extendedneighbours = regionQuery(Lsp,
					Lsp.indexOf(neighbours.get(i)));
			currentneighbours.addAll(extendedneighbours);
			neighbours.addAll(extendedneighbours);
			cluster.getPoints().add(neighbours.get(i));
			updatePoint(cluster, neighbours.get(i));
		}
	}

	PointsofInterest updatePoint(PointsofInterest point, StayPoints spoint) {
		if (point.getNumofPoints() == 0) {
			point.setAll(spoint.getLat(), spoint.getLon(), spoint.getLat(),
					spoint.getLon(), 1, point.getNumofPoints() + 1);
		} else {
			if (point.getStartlat() > spoint.getLat()) {
				point.setStartlat(spoint.getLat());
			}
			if (point.getEndlat() < spoint.getLat()) {
				point.setEndlat(spoint.getLat());
			}
			if (point.getStartlon() > spoint.getLon()) {
				point.setStartlon(spoint.getLon());
			}
			if (point.getEndlon() < spoint.getLon()) {
				point.setEndlon(spoint.getLon());
			}
		}
		return point;
	}

}
