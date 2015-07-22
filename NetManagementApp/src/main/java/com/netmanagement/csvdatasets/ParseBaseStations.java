package com.netmanagement.csvdatasets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import com.netmanagement.entities.BaseStations;

public class ParseBaseStations {
	private HashMap<String, ArrayList<BaseStations>> hap = null;
	private static ParseBaseStations ParseBaseStationsinstance = null;
	
    private ParseBaseStations(){}
	
	public static ParseBaseStations getInstance(){
		if(ParseBaseStationsinstance == null){
			ParseBaseStationsinstance = new ParseBaseStations();
		}
		return ParseBaseStationsinstance;
	}
	
	public HashMap<String, ArrayList<BaseStations>> getHap() {
		return hap;
	}

	public void setHap(HashMap<String, ArrayList<BaseStations>> hap) {
		this.hap = hap;
	}
	
	@SuppressWarnings("resource")
	public int LoadBaseStations() throws Exception {
		BufferedReader br = null;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("csvDatasets/base_station.csv").getFile());
		br = new BufferedReader(new FileReader(file));
		String line = "";
		hap = new HashMap<String, ArrayList<BaseStations>>();
		int firstline = 1;
		while ((line = br.readLine()) != null) {
		   	  if (firstline==1){
		   		  firstline=0;
		   		  continue;
		  	  }
		   	  String parts[] = line.split("\t");
		   	  BaseStations point = new BaseStations();
			  point.setAll(parts);
			  System.out.println(point.getId()+" "+point.getUser()+" "+point.getOperator()+" "+point.getMcc()+" "+point.getMnc()+" "+point.getCid()+" "+point.getLac()+" "+point.getBSlatitude()+" "+point.getBSlongtitude()+" "+point.getTimestamp());
			  if (hap.containsKey(parts[5])){
			  	 hap.get(parts[5]).add(point);
			  }
			  else{
			  	 ArrayList<BaseStations> ap = new ArrayList<BaseStations>();
			  	 ap.add(point);
			  	 hap.put(parts[5], ap);
			  }
		}
		System.out.println("Parse Completed...");
		return 0;
	}

}
