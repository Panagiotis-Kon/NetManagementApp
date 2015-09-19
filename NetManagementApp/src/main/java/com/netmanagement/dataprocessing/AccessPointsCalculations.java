package com.netmanagement.dataprocessing;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.netmanagement.csvdatasets.ParseAccessPoints;
import com.netmanagement.entities.AccessPoints;

/*
 * Calculations for data contained in wifi.csv
 */

public class AccessPointsCalculations {
	// private final double pi=Math.PI/180;
	private int EstimatedPointPosition = 0;
	private static AccessPointsCalculations AccessPointsCalculationsinstance = null;

	private AccessPointsCalculations() {
	}

	public static AccessPointsCalculations getInstance() {
		if (AccessPointsCalculationsinstance == null) {
			AccessPointsCalculationsinstance = new AccessPointsCalculations();
		}
		return AccessPointsCalculationsinstance;
	}

	public int getEstimatedPointPosition() {
		return EstimatedPointPosition;
	}

	public void setEstimatedPointPosition(int estimatedPointPosition) {
		EstimatedPointPosition = estimatedPointPosition;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int EstimatedPointPosition() {
		// Find average of rssid(=level) and calculate latitude and longitude.
		// Finally return a list of unique AccessPoints
		HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints
				.getInstance().getHap();
		if (!hap.isEmpty()) {
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {

				Map.Entry me = (Map.Entry) it.next();
				double totalweight = 0;
				double lat = 0;
				double lon = 0;
				int level = 0;
				
				ArrayList<AccessPoints> array = (ArrayList<AccessPoints>) me
						.getValue();
				if (array.size() == 1) {
					continue;
				}
				for (int i = 0; i < array.size(); i++) {
					AccessPoints tempap = array.get(i);
					double lat1 = tempap.getAPlatitude();// *pi;
					double lon1 = tempap.getAPlongtitude();// *pi;
					double w = 0.001 * Math.pow(10, tempap.getRssi() / 10);
					totalweight = totalweight + w;
					lat = lat + lat1 * w;
					lon = lon + lon1 * w;
					level = level + tempap.getRssi();
				}
				lat = lat / totalweight;
				lon = lon / totalweight;
				level = level / array.size();
				for (int i = 0; i < array.size(); i++) {
					array.get(i).setAPlatitude(lat);
					array.get(i).setAPlongtitude(lon);
					array.get(i).setRssi(level);
				}
			}
		}
		EstimatedPointPosition = 1;
		return EstimatedPointPosition;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<AccessPoints> searchUser(String userID, Date startDate,
			Date endDate) {
		// Search data for a specific user with the date contained between
		// startDate and endDate. Then return a list with these data.

		
		HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints
				.getInstance().getHap();
		ArrayList<AccessPoints> alist = new ArrayList<AccessPoints>();
		if (!hap.isEmpty()) {
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				ArrayList<AccessPoints> array = (ArrayList<AccessPoints>) me.getValue();
				for (int i = 0; i < array.size(); i++) {
					AccessPoints tempap = array.get(i);
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
			System.out.println("AccessPointsCalculations: alist is empty!!!");
		} else {
			Collections.sort(alist);
		}
		return alist;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<String> getUsers() {
		// Return all users from the data set

		HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints
				.getInstance().getHap();
		ArrayList<String> alist = new ArrayList<String>();
		if (!hap.isEmpty()) {
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				ArrayList<AccessPoints> array = (ArrayList<AccessPoints>) me
						.getValue();
				for (int i = 0; i < array.size(); i++) {
					AccessPoints tempap = array.get(i);
					if (!alist.contains(tempap.getUser())) {
						alist.add(tempap.getUser());
					}
				}
			}
		}
		if (alist.isEmpty()) {
			System.out.println("getUsers: alist is empty!!!");
		}
		return alist;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<Date> allTimestamps(String user) {
		// Find all dates of the user and return an array list with them.
		HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints
				.getInstance().getHap();
		ArrayList<Date> alist = new ArrayList<Date>();
		if (!hap.isEmpty()) {
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				ArrayList<AccessPoints> array = (ArrayList<AccessPoints>) me.getValue();
				for (int i = 0; i < array.size(); i++) {
					if (!array.get(i).getUser().equals(user)) {
						continue;
					}
					alist.add(array.get(i).getTimestamp());
				}
			}
		}
		Collections.sort(alist);
		return alist;
	}

}
