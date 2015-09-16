package com.netmanagement.csvdatasets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.netmanagement.entities.AccessPoints;

/**
 * 
 * Parsing wifi data from from wifi.csv
 * A hashMap is used for storing the data( key: bssid, value: ArrayList<AccessPoints> )
 *
 */

public class ParseAccessPoints {
	
	private HashMap<String, ArrayList<AccessPoints>> hap = null;
	private static ParseAccessPoints ParseAccessPointsinstance = null;
    private int loaded = 0;
	
    public int getLoaded() {
		return loaded;
	}

	public void setLoaded(int loaded) {
		this.loaded = loaded;
	}
	
    private ParseAccessPoints(){}
	
	public static ParseAccessPoints getInstance(){
		if(ParseAccessPointsinstance == null){
			ParseAccessPointsinstance = new ParseAccessPoints();
		}
		return ParseAccessPointsinstance;
	}
	
	public HashMap<String, ArrayList<AccessPoints>> getHap() {
		return hap;
	}

	public void setHap(HashMap<String, ArrayList<AccessPoints>> hap) {
		this.hap = hap;
	}
	
	@SuppressWarnings("resource")
	public int LoadAccessPoints() throws Exception {
		BufferedReader br = null;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("csvDatasets/wifi.csv").getFile());
		br = new BufferedReader(new FileReader(file));
		String line = "";
		hap = new HashMap<String, ArrayList<AccessPoints>>();
		int firstline = 1;
		while ((line = br.readLine()) != null) {
		   	  if (firstline==1){
		   		  firstline=0;
		   		  continue;
		  	  }
		   	  String parts[] = line.split("\t");
		  	  AccessPoints point = new AccessPoints();
			  point.setAll(parts);
			 // System.out.println(point.getId()+" "+point.getUser()+" "+point.getSsid()+" "+point.getBssid()+" "+point.getRssi()+" "+point.getFrequency()+" "+point.getAPlatitude()+" "+point.getAPlongtitude()+" "+point.getTimestamp());
			  if (hap.containsKey(parts[3])){
			  	 hap.get(parts[3]).add(point);
			  }
			  else{
			  	 ArrayList<AccessPoints> ap = new ArrayList<AccessPoints>();
			  	 ap.add(point);
			  	 hap.put(parts[3], ap);
			  }
		}
		//System.out.println("Parse Completed...");
		loaded=1;
		return 0;
	}

}
