package com.netmanagement.csvdatasets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import com.netmanagement.entities.GPS;

public class ParseGPS {

	private HashMap<String, ArrayList<GPS>> hap = null;
	private static ParseGPS ParseGPSinstance = null;
	
    private ParseGPS(){}
	
	public static ParseGPS getInstance(){
		if(ParseGPSinstance == null){
			ParseGPSinstance = new ParseGPS();
		}
		return ParseGPSinstance;
	}
	
	public HashMap<String, ArrayList<GPS>> getHap() {
		return hap;
	}

	public void setHap(HashMap<String, ArrayList<GPS>> hap) {
		this.hap = hap;
	}
	
	@SuppressWarnings("resource")
	public int LoadGPS() throws Exception {
		BufferedReader br = null;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("csvDatasets/gps.csv").getFile());
		br = new BufferedReader(new FileReader(file));
		String line = "";
		hap = new HashMap<String, ArrayList<GPS>>();
		int firstline = 1;
		while ((line = br.readLine()) != null) {
		   	  if (firstline==1){
		   		  firstline=0;
		   		  continue;
		  	  }
		   	  String parts[] = line.split("\t");
		   	  GPS point = new GPS();
			  point.setAll(parts);
			  System.out.println(point.getId()+" "+point.getUser()+" "+point.getUlatitude()+" "+point.getUlongtitude()+" "+point.getTimestamp());
			  if (hap.containsKey(parts[1])){
			  	 hap.get(parts[1]).add(point);
			  }
			  else{
			  	 ArrayList<GPS> ap = new ArrayList<GPS>();
			  	 ap.add(point);
			  	 hap.put(parts[1], ap);
			  }
		}
		System.out.println("Parse Completed...");
		return 0;
	}
}
