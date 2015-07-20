package com.netmanagement.dataprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.netmanagement.csvdatasets.ParseCSV;
import com.netmanagement.entities.APResults;
import com.netmanagement.entities.AccessPoints;

public class AccessPointPosition {
	//private final double pi=Math.PI/180;
    private static AccessPointPosition AccessPointPositioninstance = null;
	
	private AccessPointPosition(){}
	
	public static AccessPointPosition getInstance(){
		if(AccessPointPositioninstance == null){
			AccessPointPositioninstance = new AccessPointPosition();
		}
		return AccessPointPositioninstance;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<APResults> EstimatedPointPosition(){
		HashMap<String, ArrayList<AccessPoints>> hap = ParseCSV.getInstance().getHap();
		ArrayList<APResults> alist = new ArrayList<APResults>();
		if (!hap.isEmpty()){
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while(it.hasNext()){
			
				Map.Entry me = (Map.Entry)it.next();
				double totalweight = 0;
				double lat = 0;
				double lon = 0;
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
				}
				lat=lat/totalweight;
				lon=lon/totalweight;
				//lat=lat*pi;
				//lon=lon*pi;
				for (int i=0;i<array.size();i++){
					AccessPoints tempap = array.get(i);
					tempap.setAPlatitude(lat);
					tempap.setAPlongtitude(lon);
				}
				APResults temp = new APResults();
				temp.setAPlatitude(lat);
				temp.setAPlongtitude(lon);
				temp.setBSSID(array.get(0).getBssid());
				alist.add(temp);
				//System.out.println(alist.toString());
			}
		}
		for (int i=0;i<alist.size();i++)
		System.out.println(alist.get(i).getBSSID()+" "+alist.get(i).getAPlatitude()+" "+alist.get(i).getAPlongtitude());
		return alist;
	}

}
