package com.netmanagement.dataprocessing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.netmanagement.csvdatasets.ParseBattery;
import com.netmanagement.entities.Battery;

public class BatteryCalculations {
	
    private static BatteryCalculations BatteryCalculationsinstance = null;
	
	private BatteryCalculations(){}
	
	public static BatteryCalculations getInstance(){
		if(BatteryCalculationsinstance == null){
			BatteryCalculationsinstance = new BatteryCalculations();
		}
		return BatteryCalculationsinstance;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<Battery> searchUser(String userID, String startDate, String endDate){
		
		System.out.println(" Battery!! userID: " + userID + " startDate: " + startDate + " endDate: " + endDate);
		HashMap<String, ArrayList<Battery>> hap = ParseBattery.getInstance().getHap();
		ArrayList<Battery> alist = new ArrayList<Battery>();
		if (!hap.isEmpty()){
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while(it.hasNext()){
				Map.Entry me = (Map.Entry)it.next();
				//System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<Battery> array = (ArrayList<Battery>) me.getValue();
				for (int i=0;i<array.size();i++){
					Battery tempap = array.get(i);
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
			System.out.println("BatteryCalculations: alist is empty!!!");
		}
		return alist;
	}

}
