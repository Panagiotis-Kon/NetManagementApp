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
		
		private BatteryEcoRoute(){}
		
		public static BatteryEcoRoute getInstance(){
			if(BatteryEcoRouteinstance == null){
				BatteryEcoRouteinstance = new BatteryEcoRoute();
			}
			return BatteryEcoRouteinstance;
		}
	
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public ArrayList<AccessPoints> EconomicRoute(String userID, String startDate, String endDate){
			
			System.out.println("userID: " + userID + " startDate: " + startDate + " endDate: " + endDate);
			HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints.getInstance().getHap();
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
				System.out.println(alist.get(0).getUser()+" | "+alist.get(0).getTimestamp()+" ||| "+alist.get(alist.size()-1).getUser()+" | "+alist.get(alist.size()-1).getTimestamp());
			}
			
			
			return alist;
		}
	
}
