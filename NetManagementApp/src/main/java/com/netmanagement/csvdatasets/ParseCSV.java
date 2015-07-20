package com.netmanagement.csvdatasets;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.netmanagement.entities.AccessPoints;


public class ParseCSV {
	
	private HashMap<String, ArrayList<AccessPoints>> hap = null;
	private static ParseCSV ParseCSVinstance = null;
	
	private ParseCSV(){}
	
	public static ParseCSV getInstance(){
		if(ParseCSVinstance == null){
			ParseCSVinstance = new ParseCSV();
		}
		return ParseCSVinstance;
	}
	
	public HashMap<String, ArrayList<AccessPoints>> getHap() {
		return hap;
	}

	public void setHap(HashMap<String, ArrayList<AccessPoints>> hap) {
		this.hap = hap;
	}
	
	public static String StringCleanUp(String s){
		
		String cleanString = "";
		if(s.contains("["))
  	  	{  
  		  cleanString = s.replace("[", "");
  	  	}
		if(s.contains("]"))
  	  	{  
  		  cleanString = s.replace("]", "");
  	  	}
		if(s.contains(" "))
  	  	{  
  		  cleanString = s.replace(" ", "");
  	  	}
		return cleanString;
	}
	
	public static String[] StringSplitter(String s){
		
		 String[] parts = null;
	     if(s.contains(","))
	     {
	    	  parts = s.split(",");
	     }
	     return parts;
	}
	
	@SuppressWarnings("resource")
	public int LpLCSV(int option) throws Exception {
		BufferedReader br = null;
		
		
		if(option == 0) /* Every dataset is being read */
		{
			ClassLoader classLoader = getClass().getClassLoader();
			File fileWifi = new File(classLoader.getResource("csvDatasets/wifi.csv").getFile());
			File fileBat = new File(classLoader.getResource("csvDatasets/battery.csv").getFile());
			File fileGPS = new File(classLoader.getResource("csvDatasets/gps.csv").getFile());
			File fileBS = new File(classLoader.getResource("csvDatasets/base_station.csv").getFile());
		      
		      
		}
		else if(option == 1) { /* wifi(Access Points) csv option */
			
			
			
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("csvDatasets/wifi.csv").getFile());
			br = new BufferedReader(new FileReader(file));
			String line = "";
			//readerWifi = new CSVReader(new FileReader(file), '\t' , '"' , 1);
			/* add to the struct */
			//String[] nextLine;
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
			        	 System.out.println(point.getId()+" "+point.getUser()+" "+point.getSsid()+" "+point.getBssid()+" "+point.getRssi()+" "+point.getFrequency()+" "+point.getAPlatitude()+" "+point.getAPlongtitude()+" "+point.getTimestamp());
			        	 if (hap.containsKey(parts[3])){
			        		 hap.get(parts[3]).add(point);
			        	 }
			        	 else{
			        		 ArrayList<AccessPoints> ap = new ArrayList<AccessPoints>();
			        		 ap.add(point);
			        		 hap.put(parts[3], ap);
			        	 }
			  }
			System.out.println("ParseCSV OPTION = 1");
			return 1;
			
		}
		else if(option == 2) { /* battery csv option */
			
			
		}
		else if(option == 3) { /* GPS csv option*/
			
			
		}
		else if(option == 4) { /* Base Stations csv option*/
			
			
		}
		else {
			
				/* Error No option */
			return -1;
		}
		return 1;
			
		
		
	      
	}
	
	
	
	
}
