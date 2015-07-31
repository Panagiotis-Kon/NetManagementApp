package com.netmanagement.dataprocessing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.netmanagement.csvdatasets.ParseAccessPoints;
import com.netmanagement.csvdatasets.ParseBattery;
import com.netmanagement.entities.AccessPoints;
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<String> LowBatterySearch(){
		
		String DATE[] = minmaxTimestamp().split("#");
		String min = DATE[0], max = DATE[1];
		ArrayList<BatteryInfo> blist = generateDateRange(min, max);
		
		HashMap<String, ArrayList<Battery>> hap = ParseBattery.getInstance().getHap();
		ArrayList<String> alist = new ArrayList<String>();
		if (!hap.isEmpty()){
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while(it.hasNext()){
				Map.Entry me = (Map.Entry)it.next();
				//System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<Battery> array = (ArrayList<Battery>) me.getValue();
				for (int i=0;i<array.size();i++){
					/*for (int j=0;j<blist.size()-1;j++){
						if (StringtoDate(array.get(i).getTimestamp()).equals(StringtoDate(blist.get(j).hour))){
							if (!blist.get(j).users.contains(array.get(i).getUser()) && array.get(i).getLevel()<=15){
								blist.get(j).numofUsers++;
								blist.get(j).users.add(array.get(i).getUser());
							}
							break;
						}
						else if (StringtoDate(array.get(i).getTimestamp()).after(StringtoDate(blist.get(j).hour)) && StringtoDate(array.get(i).getTimestamp()).before(StringtoDate(blist.get(j+1).hour))){
							if (!blist.get(j).users.contains(array.get(i).getUser()) && array.get(i).getLevel()<=15){
								blist.get(j).numofUsers++;
								blist.get(j).users.add(array.get(i).getUser());
							}
							break;
						}
						else if (StringtoDate(array.get(i).getTimestamp()).after(StringtoDate(blist.get(blist.size()-1).hour))){
							if (!blist.get(j).users.contains(array.get(i).getUser()) && array.get(i).getLevel()<=15){
								blist.get(j).numofUsers++;
								blist.get(j).users.add(array.get(i).getUser());
							}
							break;
						}
					}*/
					Date pdate = zeroMin_Sec(array.get(i).getTimestamp());
					long diff = pdate.getTime() - StringtoDate(min).getTime();
					long totalSecs = diff/1000;
					int diffHours = (int) (totalSecs / 3600);
					System.out.println(diffHours);
					if (!blist.get(diffHours).users.contains(array.get(i).getUser()) && array.get(i).getLevel()<=15){
						blist.get(diffHours).numofUsers++;
						blist.get(diffHours).users.add(array.get(i).getUser());
					}
					
					//System.out.println(pdate);
					/*for (int j=0;j<blist.size();j++){
						if (pdate.equals(StringtoDate(blist.get(j).hour))){
							if (!blist.get(j).users.contains(array.get(i).getUser()) && array.get(i).getLevel()<=15){
								blist.get(j).numofUsers++;
								blist.get(j).users.add(array.get(i).getUser());
							}
							break;
						}
					}*/
				}
			}
		}
		for (int i=0;i<blist.size();i++){
			alist.add(blist.get(i).hour+"#"+blist.get(i).numofUsers);
		}
		if(alist.isEmpty()){
			System.out.println("Low battery: alist is empty!!!");
		}
		return alist;
	}
	
	Date StringtoDate(String sdate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date ddate=null;
		try {
			ddate=sdf.parse(sdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ddate;
	}
	
	class BatteryInfo{
		String hour;
		int numofUsers=0;
		ArrayList<String> users = new ArrayList<String>();
	}
	
	public Date zeroMin_Sec(String sdate){
		//make zero the minutes and seconds of date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date ddate=null;
		try {
			ddate=sdf.parse(sdate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(ddate);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			ddate = calendar.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ddate;
	}
	
	public ArrayList<BatteryInfo> generateDateRange(String start, String end){
		//generateDateRange every hour
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ArrayList<BatteryInfo> list = new ArrayList<BatteryInfo>();
		Date d1 = null;
		Date d2 = null;
		try {
			Calendar calendar = Calendar.getInstance();
			
			d1 = format.parse(start);
			calendar.setTime(d1);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			d1 = calendar.getTime();
			
			d2 = format.parse(end);
			calendar.setTime(d2);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			d2 = calendar.getTime();
			System.out.println(d1+" | "+d2);
 
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
			//TimeDiff=diffDays+":"+diffHours+":"+diffMinutes+":"+diffSeconds;
			System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");
			int counter=0;
			diff = diffHours+24*diffDays;
			System.out.println("Hours :"+diff);
			for (long i=0;i<diff;i++){
				BatteryInfo temp = new BatteryInfo();
				calendar.setTime(d1);
				calendar.add(Calendar.HOUR_OF_DAY, 1);
				
				d1 = calendar.getTime();
				temp.hour = format.format(d1);
				list.add(temp);
				counter++;
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Size :"+list.size());
		//for (int k=0;k<list.size();k++)
			//System.out.println("List :"+list.get(k).hour);
		return list;
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String minmaxTimestamp(){
		//Find minimum and maximum Date of the user return MIN#MAX
		String DATE=null,MIN=null,MAX=null;
		HashMap<String, ArrayList<Battery>> hap = ParseBattery.getInstance().getHap();
		int first=1;
		if (!hap.isEmpty()){
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while(it.hasNext()){
				Map.Entry me = (Map.Entry)it.next();
				//System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<Battery> array = (ArrayList<Battery>) me.getValue();
				for (int i=0;i<array.size();i++){
					if (first==1){
						MIN=array.get(i).getTimestamp();
						MAX=array.get(i).getTimestamp();
						first=0;
					}
					else {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date datemin = sdf.parse(MIN);
							Date datemax = sdf.parse(MAX);
							Date datenow = sdf.parse(array.get(i).getTimestamp());
							//System.out.println(datemin+" | "+datemax+" | "+datenow);
							if (datemin.after(datenow)){
								MIN=array.get(i).getTimestamp();
							}
							if (datemax.before(datenow)){
								MAX=array.get(i).getTimestamp();
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		DATE=MIN+"#"+MAX;
		System.out.println("Date of all: " + DATE);
		return DATE;
	}

}
