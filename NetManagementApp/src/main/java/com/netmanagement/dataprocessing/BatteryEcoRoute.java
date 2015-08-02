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
import com.netmanagement.entities.AccessPoints;

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
					for(int j=i+1; j<alist.size()-1;j++){
						
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
					}
					
					if(pos > -1){
						AccessPoints ap = alist.get(pos);
						if(ecoList.contains(alist.get(pos).getTimestamp())){
							continue;
						}else {
							ecoList.add(ap);
						}
						
					}
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

		
}
