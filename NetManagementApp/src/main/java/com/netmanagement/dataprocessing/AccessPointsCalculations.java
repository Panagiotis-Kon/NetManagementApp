package com.netmanagement.dataprocessing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.netmanagement.csvdatasets.ParseAccessPoints;
import com.netmanagement.entities.APResults;
import com.netmanagement.entities.AccessPoints;

public class AccessPointsCalculations {
	//private final double pi=Math.PI/180;
    private static AccessPointsCalculations AccessPointsCalculationsinstance = null;
	
	private AccessPointsCalculations(){}
	
	public static AccessPointsCalculations getInstance(){
		if(AccessPointsCalculationsinstance == null){
			AccessPointsCalculationsinstance = new AccessPointsCalculations();
		}
		return AccessPointsCalculationsinstance;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<APResults> EstimatedPointPosition(){
		//Find average of rssid(=level) and calculate latitude and longitude. Finally return a list of unique AccessPoints
		HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints.getInstance().getHap();
		ArrayList<APResults> alist = new ArrayList<APResults>();
		if (!hap.isEmpty()){
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while(it.hasNext()){
			
				Map.Entry me = (Map.Entry)it.next();
				double totalweight = 0;
				double lat = 0;
				double lon = 0;
				int level = 0;
				System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<AccessPoints> array = (ArrayList<AccessPoints>) me.getValue();
				if (array.size() == 1){
					continue;
				}
				for (int i=0;i<array.size();i++){
					AccessPoints tempap = array.get(i);
					double lat1 = tempap.getAPlatitude();//*pi;
					double lon1 = tempap.getAPlongtitude();//*pi;
					double w = 0.001*Math.pow(10,tempap.getRssi()/10);
					totalweight=totalweight+w;
					lat=lat+lat1*w;
					lon=lon+lon1*w;
					level=level+tempap.getRssi();
				}
				lat=lat/totalweight;
				lon=lon/totalweight;
				//lat=lat*pi;
				//lon=lon*pi;
				level=level/array.size();
				for (int i=0;i<array.size();i++){
					array.get(i).setAPlatitude(lat);
					array.get(i).setAPlongtitude(lon);
					array.get(i).setRssi(level);
				}
				APResults temp = new APResults();
				temp.setAPlatitude(lat);
				temp.setAPlongtitude(lon);
				temp.setBSSID(array.get(0).getBssid());
				if (!alist.contains(temp)){
					alist.add(temp);
				}
				//System.out.println(alist.toString());
			}
		}
		for (int i=0;i<alist.size();i++)
		System.out.println(alist.get(i).getBSSID()+" "+alist.get(i).getAPlatitude()+" "+alist.get(i).getAPlongtitude());
		return alist;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<AccessPoints> searchUser(String userID, String startDate, String endDate){
		
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
			System.out.println("AccessPointsCalculations: alist is empty!!!");
		}
		return alist;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<String> getUsers(){
		
		HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints.getInstance().getHap();
		ArrayList<String> alist = new ArrayList<String>();
		if (!hap.isEmpty()){
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while(it.hasNext()){
				Map.Entry me = (Map.Entry)it.next();
				//System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<AccessPoints> array = (ArrayList<AccessPoints>) me.getValue();
				for (int i=0;i<array.size();i++){
					AccessPoints tempap = array.get(i);
					if (!alist.contains(tempap.getUser())){
						alist.add(tempap.getUser());
					}
				}
			}
		}
		if(alist.isEmpty()){
			System.out.println("getUsers: alist is empty!!!");
		}
		return alist;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String minTimestamp(String user){
		//Find minimum Date of the user
		String DATE=null;
		HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints.getInstance().getHap();
		int first=1;
		if (!hap.isEmpty()){
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while(it.hasNext()){
				Map.Entry me = (Map.Entry)it.next();
				System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<AccessPoints> array = (ArrayList<AccessPoints>) me.getValue();
				for (int i=0;i<array.size();i++){
					if (first==1){
						DATE=array.get(i).getTimestamp();
						first=0;
					}
					else {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date1 = sdf.parse(DATE);
							Date date2 = sdf.parse(array.get(i).getTimestamp());
							System.out.println(date1+" | "+date2);
							if (date2.before(date1)){
								DATE=array.get(i).getTimestamp();
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return DATE;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String maxTimestamp(String user){
		//Find minimum Date of the user
		String DATE=null;
		HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints.getInstance().getHap();
		int first=1;
		if (!hap.isEmpty()){
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while(it.hasNext()){
				Map.Entry me = (Map.Entry)it.next();
				System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<AccessPoints> array = (ArrayList<AccessPoints>) me.getValue();
				for (int i=0;i<array.size();i++){
					if (first==1){
						DATE=array.get(i).getTimestamp();
						first=0;
					}
					else {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date1 = sdf.parse(DATE);
							Date date2 = sdf.parse(array.get(i).getTimestamp());
							System.out.println(date1+" | "+date2);
							if (date1.before(date2)){
								DATE=array.get(i).getTimestamp();
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return DATE;
	}

}
