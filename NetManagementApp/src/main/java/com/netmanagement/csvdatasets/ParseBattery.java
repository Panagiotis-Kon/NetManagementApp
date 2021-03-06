package com.netmanagement.csvdatasets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import com.netmanagement.entities.Battery;

/**
 * 
 * Parsing battery data from from battery.csv
 * A hashMap is used for storing the data( key: user, value: ArrayList<Battery> )
 *
 */

public class ParseBattery {
	
	private HashMap<String, ArrayList<Battery>> hap = null;
	private static ParseBattery ParseBatteryinstance = null;
    private int loaded = 0;
	
    public int getLoaded() {
		return loaded;
	}

	public void setLoaded(int loaded) {
		this.loaded = loaded;
	}
	
    private ParseBattery(){}
	
	public static ParseBattery getInstance(){
		if(ParseBatteryinstance == null){
			ParseBatteryinstance = new ParseBattery();
		}
		return ParseBatteryinstance;
	}
	
	public HashMap<String, ArrayList<Battery>> getHap() {
		return hap;
	}

	public void setHap(HashMap<String, ArrayList<Battery>> hap) {
		this.hap = hap;
	}
	
	@SuppressWarnings("resource")
	public int LoadBattery() throws Exception {
		BufferedReader br = null;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("csvDatasets/battery.csv").getFile());
		br = new BufferedReader(new FileReader(file));
		String line = "";
		hap = new HashMap<String, ArrayList<Battery>>();
		int firstline = 1;
		while ((line = br.readLine()) != null) {
		   	  if (firstline==1){
		   		  firstline=0;
		   		  continue;
		  	  }
		   	  String parts[] = line.split("\t");
		   	  Battery point = new Battery();
			  point.setAll(parts);
			  //System.out.println(point.getId()+" "+point.getUser()+" "+point.getLevel()+" "+point.getPlugged()+" "+point.getTemperature()+" "+point.getVoltage()+" "+point.getTimestamp());
			  if (hap.containsKey(parts[1])){
			  	 hap.get(parts[1]).add(point);
			  }
			  else{
			  	 ArrayList<Battery> ap = new ArrayList<Battery>();
			  	 ap.add(point);
			  	 hap.put(parts[1], ap);
			  }
		}
		//System.out.println("Parse Completed...");
		loaded=1;
		return 0;
	}
	
}
