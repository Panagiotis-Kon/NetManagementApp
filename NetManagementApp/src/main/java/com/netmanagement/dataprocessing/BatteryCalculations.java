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

import com.netmanagement.csvdatasets.ParseBattery;
import com.netmanagement.entities.Battery;

public class BatteryCalculations {

	private static BatteryCalculations BatteryCalculationsinstance = null;

	static final int percent = 15; // From 0 to 100

	private BatteryCalculations() {
	}

	public static BatteryCalculations getInstance() {
		if (BatteryCalculationsinstance == null) {
			BatteryCalculationsinstance = new BatteryCalculations();
		}
		return BatteryCalculationsinstance;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<Battery> searchUser(String userID, Date startDate,
			Date endDate) {
		// Search data for a specific user with the date contained between
		// startDate and endDate. Then return a list with these data.
		
		HashMap<String, ArrayList<Battery>> hap = ParseBattery.getInstance().getHap();
		ArrayList<Battery> alist = new ArrayList<Battery>();
		if (!hap.isEmpty()) {
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				
				ArrayList<Battery> array = (ArrayList<Battery>) me.getValue();
				for (int i = 0; i < array.size(); i++) {
					Battery tempap = array.get(i);
					if (tempap.getUser().equals(userID)) {
						
						if (startDate.equals(tempap.getTimestamp()) || startDate.before(tempap.getTimestamp())) {
							if (endDate.equals(tempap.getTimestamp()) || endDate.after(tempap.getTimestamp())) {
								alist.add(tempap);
							}
						}
						
						
					}
				}
			}
		}
		if (alist.isEmpty()) {
			System.out.println("BatteryCalculations: alist is empty!!!");
		}
		return alist;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<String> LowBatterySearch() {
		// Search for users with battery level below a percentage in a given
		// time. Return an array list with
		// hour_of_the_day#number_of_unique_users_with_low battery
		String DATE[] = minmaxTimestamp().split("#");
		
		Date min = StringtoDate(DATE[0]);
		Date max = StringtoDate(DATE[1]);
		ArrayList<BatteryInfo> blist = generateDateRange(min, max);

		HashMap<String, ArrayList<Battery>> hap = ParseBattery.getInstance().getHap();
		ArrayList<String> alist = new ArrayList<String>();
		if (!hap.isEmpty()) {
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				
				ArrayList<Battery> array = (ArrayList<Battery>) me.getValue();
				for (int i = 0; i < array.size(); i++) {

					Date pdate = zeroMin_Sec(array.get(i).getTimestamp());
					long diff = pdate.getTime() - min.getTime();
					long totalSecs = diff / 1000;
					int diffHours = (int) (totalSecs / 3600);
					
					if (!blist.get(diffHours).users.contains(array.get(i)
							.getUser()) && array.get(i).getLevel() <= percent) {
						blist.get(diffHours).numofUsers++;
						blist.get(diffHours).users.add(array.get(i).getUser());
					}
				}
			}
		}
		for (int i = 0; i < blist.size(); i++) {
			alist.add(blist.get(i).hour + "#" + blist.get(i).numofUsers);
		}
		if (alist.isEmpty()) {
			System.out.println("Low battery: alist is empty!!!");
		}
		return alist;
	}

public Date StringtoDate(String sdate) { // Change String to Date format :
										// yyyy-MM-dd HH:mm:ss
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date ddate = null;
		try {
			ddate = sdf.parse(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ddate;
	}

	class BatteryInfo {
		String hour;
		int numofUsers = 0;
		ArrayList<String> users = new ArrayList<String>();
	}

	public Date zeroMin_Sec(Date sdate) {
		// Make zero the minutes and seconds of date

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdate);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			sdate = calendar.getTime();
		
		return sdate;
	}

	public ArrayList<BatteryInfo> generateDateRange(Date d1, Date d2) {
		// GenerateDateRange every hour
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ArrayList<BatteryInfo> list = new ArrayList<BatteryInfo>();
		
			Calendar calendar = Calendar.getInstance();

		
			calendar.setTime(d1);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			d1 = calendar.getTime();

			calendar.setTime(d2);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			d2 = calendar.getTime();
			

			// in milliseconds
			long diff = 0;
			if (d2.getTime() >= d1.getTime())
				diff = d2.getTime() - d1.getTime();
			else
				diff = d1.getTime() - d2.getTime();

			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
		
			@SuppressWarnings("unused")
			int counter = 0;
			diff = diffHours + 24 * diffDays;
		
			for (long i = 0; i < diff; i++) {
				BatteryInfo temp = new BatteryInfo();
				calendar.setTime(d1);
				calendar.add(Calendar.HOUR_OF_DAY, 1);

				d1 = calendar.getTime();
				temp.hour = format.format(d1);
				list.add(temp);
				counter++;
			}

		
			
		return list;
	}

	String TimeDifference(String string1, String string2) {
		// Find time difference between given variables and return it to
		// dd:HH:mm:ss format
		String TimeDiff = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(string1);
			d2 = format.parse(string2);

			// in milliseconds
			long diff = 0;
			if (d2.getTime() >= d1.getTime())
				diff = d2.getTime() - d1.getTime();
			else
				diff = d1.getTime() - d2.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			
			TimeDiff = diffDays + ":" + diffHours + ":" + diffMinutes + ":" + diffSeconds;
			/*System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");*/

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return TimeDiff;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String minmaxTimestamp() {
		// Find minimum and maximum Date of the user return MIN#MAX
		String DATE = null; 
		Date MIN = null, MAX = null;
		HashMap<String, ArrayList<Battery>> hap = ParseBattery.getInstance()
				.getHap();
		int first = 1;
		if (!hap.isEmpty()) {
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				ArrayList<Battery> array = (ArrayList<Battery>) me.getValue();
				for (int i = 0; i < array.size(); i++) {
					if (first == 1) {
						MIN = array.get(i).getTimestamp();
						MAX = array.get(i).getTimestamp();
						first = 0;
					} else {
						
						if (MIN.after(array.get(i).getTimestamp())) {
							MIN = array.get(i).getTimestamp();
						}
						if (MAX.before(array.get(i).getTimestamp())) {
							MAX = array.get(i).getTimestamp();
						}
						
						
					}
				}
			}
		}
		DATE = MIN.toString() + "#" + MAX.toString();
		System.out.println("Date of all: " + DATE);
		return DATE;
	}

}
