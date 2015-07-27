package com.netmanagement.dataprocessing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.netmanagement.csvdatasets.ParseGPS;
import com.netmanagement.entities.GPS;
import com.netmanagement.entities.StayPoints;

public class GPSCalculations {
	
    private static GPSCalculations GPSCalculationsinstance = null;
	
	private GPSCalculations(){}
	
	public static GPSCalculations getInstance(){
		if(GPSCalculationsinstance == null){
			GPSCalculationsinstance = new GPSCalculations();
		}
		return GPSCalculationsinstance;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<GPS> searchUser(String userID, String startDate, String endDate){
		//For given variables find gps points
		System.out.println("userID: " + userID + " startDate: " + startDate + " endDate: " + endDate);
		HashMap<String, ArrayList<GPS>> hap = ParseGPS.getInstance().getHap();
		ArrayList<GPS> alist = new ArrayList<GPS>();
		if (!hap.isEmpty()){
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while(it.hasNext()){
				Map.Entry me = (Map.Entry)it.next();
				//System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<GPS> array = (ArrayList<GPS>) me.getValue();
				for (int i=0;i<array.size();i++){
					GPS tempap = array.get(i);
					if (tempap.getUser().equals(userID)){
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date date1 = sdf.parse(startDate);
							Date date2 = sdf.parse(endDate);
							Date dateu = sdf.parse(tempap.getTimestamp());
							//System.out.println(date1+" | "+date2+" | "+dateu);
							if (date1.equals(dateu) || date1.before(dateu)){
								if (date2.equals(dateu) || date2.after(dateu)){
									System.out.println(date1+" | "+date2+" | "+dateu);
									alist.add(tempap);
								}
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			}
		}
		if(alist.isEmpty()){
			System.out.println("GPSCalculations: alist is empty!!!");
		}
		return alist;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<StayPoints> findStayPoints(ArrayList<GPS> GPSPoints, String Tmin,
			String Tmax, Double Dmax) { // List of specific gps points
		// For given variables find stay points, Tmin and Tmax are in format dd:HH:mm:ss
		System.out.println("uTmin: " + Tmin + " Tmax: " + Tmax + " Dmax: "
				+ Dmax);
		ArrayList<StayPoints> Lsp = new ArrayList<StayPoints>();
        int i=0, j=0, numofStayPoints=0;
        String t=null;
        double d=0;
        while (i<GPSPoints.size()-1){
        	j=i+1;
        	while (j<=GPSPoints.size()-1){
        		t=TimeDifference(GPSPoints.get(j).getTimestamp(),GPSPoints.get(j-1).getTimestamp());
        		SimpleDateFormat sdf = new SimpleDateFormat("dd:HH:mm:ss");
        		Date datet=null;
        		Date dateTmax=null;
        		Date dateTmin=null;
        		try {
					datet=sdf.parse(t);
					dateTmax=sdf.parse(Tmax);
					dateTmin=sdf.parse(Tmin);
					System.out.println(datet+" | "+dateTmin+" | "+dateTmax);
				    if (datet.after(dateTmax)){
				      t=TimeDifference(GPSPoints.get(i).getTimestamp(),GPSPoints.get(j-1).getTimestamp());
				      datet=sdf.parse(t);
        			  if (datet.after(dateTmin)){
        				Lsp.add(estimateStayPoint(GPSPoints,i,j-1));
        			  }
        			  i=j;
        			  break;
        		    }
				    else if (SpaceDistance(GPSPoints.get(i),GPSPoints.get(j))>Dmax){
				    	if (datet.after(dateTmin)){
				    		Lsp.add(estimateStayPoint(GPSPoints, i, j-1));
				    		i=j;
				    		break;
				    	}
				    	i++;
				    	break;
				    }
				    else if (j==GPSPoints.size()-1){
				    	t=TimeDifference(GPSPoints.get(i).getTimestamp(),GPSPoints.get(j).getTimestamp());
				    	datet=sdf.parse(t);
				    	if (datet.after(dateTmin)){
				    		Lsp.add(estimateStayPoint(GPSPoints, i, j));
				    	}
				    	i=j;
				    	break;
				    }
				    j++;
        		
        		} catch (ParseException e) {
					e.printStackTrace();
				}
        	}
        }
		if (Lsp.isEmpty()) {
			System.out.println("Stay Points Calculations: Lsp is empty!!!");
		}
		else {
			System.out.println("Stay Points Calculations: Lsp has something!!!");
		}
		return Lsp;
	}
	
	StayPoints estimateStayPoint(ArrayList<GPS> list, int start, int end){
		StayPoints sp = new StayPoints();
		double lat=list.get(start).getUlatitude(),lon=list.get(start).getUlatitude();
		String Tstart = list.get(start).getTimestamp(), Tend = list.get(end).getTimestamp();
		for (int i=start+1;i<=end;i++){
			lat=estimateCentroid(lat, list.get(i).getUlatitude());
			lon=estimateCentroid(lon, list.get(i).getUlongtitude());
		}
		sp.setAll(lat, lon, Tstart, Tend);
		return sp;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<GPS> findStayPoints2(ArrayList<GPS> GPSPoints, String Tmin,
			String Tmax, Double Dmax) { // List of specific gps points
		// For given variables find stay points, Tmin and Tmax are in format dd:HH:mm:ss
		System.out.println("uTmin: " + Tmin + " Tmax: " + Tmax + " Dmax: "
				+ Dmax);
		ArrayList<GPS> Lsp = new ArrayList<GPS>();
        int i=1, j=0, numofStayPoints=0;
        String t=null;
        double d=0;
        while (i<GPSPoints.size()){
        	j=i+1;
        	while (j<GPSPoints.size()){
        		t=TimeDifference(GPSPoints.get(j).getTimestamp(),GPSPoints.get(j-1).getTimestamp());
        		SimpleDateFormat sdf = new SimpleDateFormat("dd:HH:mm:ss");
        		Date datet=null;
        		Date dateTmax=null;
        		Date dateTmin=null;
        		try {
					datet=sdf.parse(t);
					dateTmax=sdf.parse(Tmax);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		if (datet.after(dateTmax)){
        			i=j;
        			break;
        		}
        		d=SpaceDistance(GPSPoints.get(i),GPSPoints.get(j));
        		if (d>Dmax){
        			t=TimeDifference(GPSPoints.get(i).getTimestamp(),GPSPoints.get(j-1).getTimestamp());
        			try {
    					datet=sdf.parse(t);
    					dateTmin=sdf.parse(Tmin);
    				} catch (ParseException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
        			if (datet.after(dateTmin)){
        				//lat,lon from centroid
        				double lat=GPSPoints.get(i).getUlatitude();
        				double lon=GPSPoints.get(i).getUlongtitude();
        				for (int k=i+1;k<j-1;k++){
        					lat=estimateCentroid(GPSPoints.get(k).getUlatitude(), lat);
        					lon=estimateCentroid(GPSPoints.get(k).getUlongtitude(), lon);
        				}
        				//Tstart
        				String Tstart = GPSPoints.get(i).getTimestamp();
        				//Tend
        				String Tend = GPSPoints.get(j-1).getTimestamp();
        				//sp
        				GPS sp = new GPS();
        				numofStayPoints++;
        				String data[] = new String[]{Integer.toString(numofStayPoints),GPSPoints.get(i).getUser(),Double.toString(lat),Double.toString(lon),Tstart+" to "+Tend};
        				sp.setAll(data);
        				//Lsp
        				Lsp.add(sp);
        			}
        			i=j;
        			break;
        		}
        		j=j+1;
        	}
        }
		if (Lsp.isEmpty()) {
			System.out.println("GPSCalculations: alist is empty!!!");
		}
		return Lsp;
	}
	
	String TimeDifference(String string1, String string2){
		//Find time difference between given variables and return it to dd:HH:mm:ss format
		String TimeDiff=null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(string1);
			d2 = format.parse(string2);
 
			//in milliseconds
			long diff = 0;
			if (d2.getTime() >= d1.getTime())
				diff = d2.getTime() - d1.getTime();
			else
				diff = d1.getTime() - d2.getTime();
 
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			//TimeDiff=System.out.format("%02d", diffDays)+":"+System.out.format("%02d", diffHours)+":"+System.out.format("%02d", diffMinutes)+":"+System.out.format("%02d", diffSeconds);
			TimeDiff=diffDays+":"+diffHours+":"+diffMinutes+":"+diffSeconds;
			System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");
 
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Time Difference :"+TimeDiff);
		return TimeDiff;
	}
	
	double SpaceDistance(GPS gps1, GPS gps2){
		//Find Pythagorean distance calculation between given variables
		double distance=0,lat=0,lon=0;
		if (gps1.getUlatitude()>gps2.getUlatitude()){
			lat=gps1.getUlatitude()-gps2.getUlatitude();
		}
		else {
			lat=gps2.getUlatitude()-gps1.getUlatitude();
		}
		lat=lat*lat;
		if (gps1.getUlongtitude()>gps2.getUlongtitude()){
			lon=gps1.getUlongtitude()-gps2.getUlongtitude();
		}
		else {
			lon=gps2.getUlongtitude()-gps1.getUlongtitude();
		}
		lon=lon*lon;
		distance=Math.sqrt(lat+lon);
		return distance;
	}
	
	double estimateCentroid(double value1,double value2){
		double center=0;
		center=(value1+value2)/2;
		return center;
	}

}
