package com.netmanagement.dataprocessing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.netmanagement.csvdatasets.ParseAccessPoints;
import com.netmanagement.csvdatasets.ParseGPS;
import com.netmanagement.entities.AccessPoints;
import com.netmanagement.entities.GPS;

public class BatteryEcoRoute {
	 private static BatteryEcoRoute BatteryEcoRouteinstance = null;
	 private int currentMaxRSSI;
	 private int pos=-1;
		private BatteryEcoRoute(){}
		
		public static BatteryEcoRoute getInstance(){
			if(BatteryEcoRouteinstance == null){
				BatteryEcoRouteinstance = new BatteryEcoRoute();
			}
			return BatteryEcoRouteinstance;
		}
		
		public int getCurrentMaxRSSI() {
			return currentMaxRSSI;
		}

		public void setCurrentMaxRSSI(int currentMaxRSSI) {
			this.currentMaxRSSI = currentMaxRSSI;
		}

		public int getPos() {
			return pos;
		}

		public void setPos(int pos) {
			this.pos = pos;
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public ArrayList<GPS> UserRoute(String userID, String startDate, String endDate){
			HashMap<String, ArrayList<GPS>> hgps = ParseGPS.getInstance().getHap();
			ArrayList<GPS> gpsList = new ArrayList<GPS>();
			if (!hgps.isEmpty()){
				Set<?> set = hgps.entrySet();
				Iterator<?> it = set.iterator();
				while(it.hasNext()){
					Map.Entry me = (Map.Entry)it.next();
					//System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
					ArrayList<GPS> array = (ArrayList<GPS>) me.getValue();
					for (int i=0;i<array.size();i++){
						GPS tempgps = array.get(i);
						if (tempgps.getUser().equals(userID)){
							try {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								
								Date date1 = sdf.parse(startDate);
								Date date2 = sdf.parse(endDate);
								Date dateu = sdf.parse(tempgps.getTimestamp());
								
								//System.out.println(date1+" | "+date2+" | "+dateu);
								if (date1.equals(dateu) || date1.before(dateu)){
									if (date2.equals(dateu) || date2.after(dateu)){
										gpsList.add(tempgps);
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
			return gpsList;
		}
	
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public ArrayList<AccessPoints> EcoMarkers(String userID, String startDate, String endDate){
			HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints.getInstance().getHap();
			ArrayList<AccessPoints> ecoList = new ArrayList<AccessPoints>();
			ArrayList<AccessPoints> APList = new ArrayList<AccessPoints>();
			ArrayList<GPS> gpsList = UserRoute(userID, startDate, endDate);
			
			if (!hap.isEmpty()){
				Set<?> set = hap.entrySet();
				Iterator<?> it = set.iterator();
				while(it.hasNext()){
					Map.Entry me = (Map.Entry)it.next();
					//System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
					ArrayList<AccessPoints> array = (ArrayList<AccessPoints>) me.getValue();
					for (int i=0;i<array.size();i++){
						AccessPoints tempap = array.get(i);
						if (tempap.getUser().equals(userID)){
							try {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								
								Date date1 = sdf.parse(startDate);
								Date date2 = sdf.parse(endDate);
								Date dateu = sdf.parse(tempap.getTimestamp());
								
								//System.out.println(date1+" | "+date2+" | "+dateu);
								if (date1.equals(dateu) || date1.before(dateu)){
									if (date2.equals(dateu) || date2.after(dateu)){
										APList.add(tempap);
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
			int time_slack=5;
			int radius=39;
			AccessPoints tempap =null;
			for (int i=0;i<gpsList.size();i++){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
				    Date gpsdate = sdf.parse(gpsList.get(i).getTimestamp());
				    tempap = null;
				    for (int j=0;j<APList.size();j++){
					   Date apdate = sdf.parse(APList.get(j).getTimestamp());
					   System.out.println("Time Diff: "+Math.abs(apdate.getTime()-gpsdate.getTime()));
					   System.out.println("Space Dist: "+SpaceDistance(gpsList.get(i), APList.get(j)));
                       if (Math.abs(apdate.getTime()-gpsdate.getTime()) <= time_slack*1000){
                    	   
                    	   if (SpaceDistance(gpsList.get(i), APList.get(j)) <= radius){
                    		   
                    		   if (tempap==null){
                    			   tempap = new AccessPoints();
                    			   tempap.setBssid(APList.get(j).getBssid());
                    			   tempap.setAPlatitude(APList.get(j).getAPlatitude());
                    			   tempap.setAPlongtitude(APList.get(j).getAPlongtitude());
                    			   tempap.setFrequency(APList.get(j).getFrequency());
                    			   tempap.setRssi(APList.get(j).getRssi());
                    			   tempap.setSsid(APList.get(j).getSsid());
                    			   tempap.setTimestamp(APList.get(j).getTimestamp());
                    			   tempap.setUser(APList.get(j).getUser());
                    			   tempap.setId(APList.get(j).getId());
                    		   }
                    		   else {
                    			   if (tempap.getBssid() != APList.get(j).getBssid() && tempap.getRssi() < APList.get(j).getRssi()){
                    				   tempap = new AccessPoints();
                        			   tempap.setBssid(APList.get(j).getBssid());
                        			   tempap.setAPlatitude(APList.get(j).getAPlatitude());
                        			   tempap.setAPlongtitude(APList.get(j).getAPlongtitude());
                        			   tempap.setFrequency(APList.get(j).getFrequency());
                        			   tempap.setRssi(APList.get(j).getRssi());
                        			   tempap.setSsid(APList.get(j).getSsid());
                        			   tempap.setTimestamp(APList.get(j).getTimestamp());
                        			   tempap.setUser(APList.get(j).getUser());
                        			   tempap.setId(APList.get(j).getId());
                    			   }
                    		   }
                    	   }
					   }
				    }
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (tempap!=null) ecoList.add(tempap);
			}
			System.out.println(ecoList.size());
			return ecoList;
		}
	
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public ArrayList<AccessPoints> EconomicRoute(String userID, String startDate, String endDate){
			
			System.out.println("userID: " + userID + " startDate: " + startDate + " endDate: " + endDate);
			HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints.getInstance().getHap();
			ArrayList<AccessPoints> ecoList = new ArrayList<AccessPoints>();
			ArrayList<AccessPoints> alist = new ArrayList<AccessPoints>();
			if (!hap.isEmpty()){
				Set<?> set = hap.entrySet();
				Iterator<?> it = set.iterator();
				while(it.hasNext()){
					Map.Entry me = (Map.Entry)it.next();
					//System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
					ArrayList<AccessPoints> array = (ArrayList<AccessPoints>) me.getValue();
					for (int i=0;i<array.size();i++){
						AccessPoints tempap = array.get(i);
						if (tempap.getUser().equals(userID)){
							try {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								
								Date date1 = sdf.parse(startDate);
								Date date2 = sdf.parse(endDate);
								Date dateu = sdf.parse(tempap.getTimestamp());
								
								//System.out.println(date1+" | "+date2+" | "+dateu);
								if (date1.equals(dateu) || date1.before(dateu)){
									if (date2.equals(dateu) || date2.after(dateu)){
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
				System.out.println("AccessPointsCalculations: alist is empty!!!");
			}
			else {
				
				Collections.sort(alist);
				System.out.println("alist size: " + alist.size());
				System.out.println(alist.get(0).getUser()+" | "+alist.get(0).getTimestamp()+" ||| "+alist.get(alist.size()-1).getUser()+" | "+alist.get(alist.size()-1).getTimestamp());
				
				ArrayList<String> visited = new ArrayList<String>();
				
				for(int i=0; i<alist.size();i++){
					setCurrentMaxRSSI(alist.get(i).getRssi());
					pos = i;
					if(visited.contains(alist.get(i).getTimestamp())){
						continue;
					}
					else {
						visited.add(alist.get(i).getTimestamp());
					}
					int j=0;
					for(j=i+1; j<alist.size()-1;j++){
						
						//System.out.println("alist time: " + alist.get(i).getTimestamp() + " --- " + alist.get(j).getTimestamp());
						if(alist.get(i).getTimestamp().equals(alist.get(j).getTimestamp())){
							
							//System.out.println("Hii" + i);
							if(!visited.contains(alist.get(j).getTimestamp())){
								visited.add(alist.get(j).getTimestamp()); // mark as visited
							}
							
							if(alist.get(j).getRssi() > currentMaxRSSI ){
								currentMaxRSSI = alist.get(j).getRssi();
								setPos(j);
								System.out.println("Found bigger RSSI in pos: " + pos+ " for time: " + alist.get(j).getTimestamp());
							}
							else{
								
								System.out.println("Current RSSI is bigger");
							}
							
						}
						else {
							break;
						}
					}
					
					if(pos > -1){
						AccessPoints ap = alist.get(pos);
						if(ecoList.contains(alist.get(pos).getTimestamp())){
							continue;
						}else {
							ecoList.add(ap);
						}
						
					}
					i=j-1;
					//setPos(0);
					//currentMaxRSSI=0;
				}
			}
			Collections.sort(ecoList);
			
			for(int i=0; i<ecoList.size();i++){
				System.out.println("Time: " + ecoList.get(i).getTimestamp() + " RSSI: " + ecoList.get(i).getRssi() + " SSID: " + ecoList.get(i).getSsid());
			}
			return ecoList;
		}
		
		/*
		public ArrayList<AccessPoints> EcoMinRoute(String userID, String startDate, String endDate ){
			
			ArrayList<AccessPoints> ecoList = new ArrayList<AccessPoints>();
			ArrayList<AccessPoints> alist = AccessPointsCalculations.getInstance().searchUser(userID, startDate, endDate); 
			ArrayList<GPS> GPSList = GPSCalculations.getInstance().searchUser(userID, startDate, endDate);
			Collections.sort(GPSList);
			ArrayList<String> visited = new ArrayList<String>();
			
			
			for(int i=0; i<alist.size(); i++)
			{
				ArrayList<AccessPoints> rbList = new ArrayList<AccessPoints>();
				ArrayList<BSSIDDist> BDList = new ArrayList<BSSIDDist>();
				int j=0;
				for(j=i+1; j<alist.size()-1;j++){
					if(alist.get(i).getTimestamp().equals(alist.get(j).getTimestamp())){
						AccessPoints apObj  = new AccessPoints();
						apObj = alist.get(i);
						if(!rbList.contains(apObj)){
							rbList.add(apObj);
						}
						
					}
				}

				for(int k=0; k<GPSList.size();k++){
					
					if(alist.get(i).getTimestamp().equals(GPSList.get(k).getTimestamp())){
						BSSIDDist bsd = new BSSIDDist();
						bsd.bssid = alist.get(i).getBssid();
						double distance = SpaceDistance(GPSList.get(k), alist.get(i));
						bsd.dist = distance;
						BDList.add(bsd);
						
					}
				}
				int minSum=0;
				int posIndex=-1;
				Collections.sort(BDList);
				Collections.sort(rbList,AccessPoints.compareRssi());
				
				for(int k=0; k<BDList.size(); k++){
					for(int m=0;m<rbList.size();m++){
						if(BDList.get(k).bssid.equals(rbList.get(m).getBssid())){
							if(minSum > k+m)
							{
								minSum = k+m;
								posIndex = m;
							}
							
						}
						
					}
					
				}
				
				ecoList.add(rbList.get(posIndex));
				
			}
			Collections.sort(ecoList);
			System.out.println("Eco Route size: " + ecoList.size());
			for(int i=0; i<ecoList.size();i++){
				System.out.println("Time: " + ecoList.get(i).getTimestamp() + " RSSI: " + ecoList.get(i).getRssi() + " BSSID: " + ecoList.get(i).getBssid());
			}
			return ecoList;
			
		}
		*/
		
		class BSSIDDist implements Comparable<BSSIDDist>{
			double dist;
			String bssid;
			
			public double getDist() {
				return dist;
			}

			public void setDist(double dist) {
				this.dist = dist;
			}

			public String getBssid() {
				return bssid;
			}

			public void setBssid(String bssid) {
				this.bssid = bssid;
			}

			@Override
			public int compareTo(BSSIDDist o) {
				return Double.compare(getDist(), o.getDist());
				
			}
			
		}
		
		double SpaceDistance(GPS gps, AccessPoints ap){
			//Find distance calculation between given variables from http://www.movable-type.co.uk/scripts/latlong.html
			double glat=gps.getUlatitude()*Math.PI/180;
			double alat=ap.getAPlatitude()*Math.PI/180;
			double glon=gps.getUlongtitude()*Math.PI/180;
			double alon=ap.getAPlongtitude()*Math.PI/180;
			double R = 6371000;
			double df = alat - glat;
			double dl = alon - glon;
			double a = Math.sin(df/2)*Math.sin(df/2)+Math.cos(glat)*Math.cos(alat)*Math.sin(dl/2)*Math.sin(dl/2);
			double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
			double d=R*c;
			return Math.abs(d);
		}
		
		/*double SpaceDistance(GPS gps, AccessPoints ap){
			//Find Pythagorean distance calculation between given variables
			double distance=0,lat=0,lon=0;
			if (gps.getUlatitude()>ap.getAPlatitude()){
				lat=gps.getUlatitude()-ap.getAPlatitude();
			}
			else {
				lat=ap.getAPlatitude()-gps.getUlatitude();
			}
			lat=lat*lat;
			if (gps.getUlongtitude()>ap.getAPlongtitude()){
				lon=gps.getUlongtitude()-ap.getAPlongtitude();
			}
			else {
				lon=ap.getAPlongtitude()-gps.getUlongtitude();
			}
			lon=lon*lon;
			distance=Math.sqrt(lat+lon);
			return distance;
		}*/

}
