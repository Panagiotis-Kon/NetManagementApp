package com.netmanagement.dataprocessing;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.netmanagement.csvdatasets.ParseGPS;
import com.netmanagement.entities.GPS;
import com.netmanagement.entities.StayPoints;

public class GPSCalculations {

	private static GPSCalculations GPSCalculationsinstance = null;

	private GPSCalculations() {
	}

	public static GPSCalculations getInstance() {
		if (GPSCalculationsinstance == null) {
			GPSCalculationsinstance = new GPSCalculations();
		}
		return GPSCalculationsinstance;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<String> getUsers() {
		// Return an array list of all unique users
		HashMap<String, ArrayList<GPS>> hap = ParseGPS.getInstance().getHap();
		ArrayList<String> alist = new ArrayList<String>();
		if (!hap.isEmpty()) {
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				
				ArrayList<GPS> array = (ArrayList<GPS>) me.getValue();
				for (int i = 0; i < array.size(); i++) {
					GPS tempap = array.get(i);
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
	public ArrayList<GPS> searchUser(String userID, Date startDate,
			Date endDate) {
		// For given variables find gps points (Specific user between startDate
		// and endDate)
		
		HashMap<String, ArrayList<GPS>> hap = ParseGPS.getInstance().getHap();
		ArrayList<GPS> alist = new ArrayList<GPS>();
		if (!hap.isEmpty()) {
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				ArrayList<GPS> array = (ArrayList<GPS>) me.getValue();
				for (int i = 0; i < array.size(); i++) {
					GPS tempap = array.get(i);
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
			System.out.println("GPSCalculations: alist is empty!!!");
		}
		return alist;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<Date> allTimestamps(String user) {
		// Find minimum and maximum Date of the user return MIN#MAX
		HashMap<String, ArrayList<GPS>> hap = ParseGPS.getInstance().getHap();
		ArrayList<Date> alist = new ArrayList<Date>();
		if (!hap.isEmpty()) {
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				ArrayList<GPS> array = (ArrayList<GPS>) me.getValue();
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

	
public long ConvertToMilli(String t) {

		String[] parts = t.split(":");
		
		
		int d = parts[0].indexOf('0');
		int h = parts[1].indexOf('0');
		int mi = parts[2].indexOf('0');
		int s = parts[3].indexOf('0');
		if (h == 0) {
			parts[1] = parts[1].substring(1);	
		}
		if (mi == 0) {
			parts[2] = parts[2].substring(1);
		}
		if (s == 0) {
			parts[3] = parts[3].substring(1);	
		}
		if(d == 0) {
			parts[0] = parts[0].substring(1);	
		}

		long hours = Long.parseLong(parts[1]);
		long mins = Long.parseLong(parts[2]);
		long secs = Long.parseLong(parts[3]);
		long days = Long.parseLong(parts[0]);
		
		
		long m = TimeUnit.MILLISECONDS.convert(days, TimeUnit.DAYS) + TimeUnit.MILLISECONDS.convert(hours, TimeUnit.HOURS) + 
				TimeUnit.MILLISECONDS.convert(mins, TimeUnit.MINUTES) +
				TimeUnit.MILLISECONDS.convert(secs, TimeUnit.SECONDS);
		
		return m;
		
	}
	
	
	public ArrayList<StayPoints> findStayPoints(ArrayList<GPS> GPSPoints,
			String Tmin, String Tmax, Double Dmax) {

		// List of specific gps points
		// For given variables find stay points, Tmin and Tmax are in format
		// dd:HH:mm:ss
		
		ArrayList<StayPoints> Lsp = new ArrayList<StayPoints>();
		int i = 0, j = 0;
		long t = 0;

		long tmin = ConvertToMilli(Tmin);
		long tmax = ConvertToMilli(Tmax);
		
		while (i < GPSPoints.size() - 1) {
			j = i + 1;
			while (j <= GPSPoints.size() - 1) {
				t = TimeDifference(GPSPoints.get(j).getTimestamp(), GPSPoints.get(j - 1).getTimestamp());
			
					if (t > tmax) {
						t = TimeDifference(GPSPoints.get(i).getTimestamp(),GPSPoints.get(j - 1).getTimestamp());
						
						if (t > tmin) {
							Lsp.add(estimateStayPoint(GPSPoints, i, j - 1));
						}
						i = j;
						break;
					} else if (SpaceDistance(GPSPoints.get(i), GPSPoints.get(j)) > Dmax) {
						t = TimeDifference(GPSPoints.get(i).getTimestamp(),
								GPSPoints.get(j - 1).getTimestamp());
						
						if (t > tmin) {
							Lsp.add(estimateStayPoint(GPSPoints, i, j - 1));
							i = j;
							break;
						}
						i++;
						break;
					} else if (j == GPSPoints.size() - 1) {
						t = TimeDifference(GPSPoints.get(i).getTimestamp(),
								GPSPoints.get(j).getTimestamp());
						
						if (t > tmin) {
							Lsp.add(estimateStayPoint(GPSPoints, i, j));
						}
						i = j;
						break;
					}
					j++;

				
			}
		}
		/*if (Lsp.isEmpty()) {
			System.out.println("Stay Points Calculations: Lsp is empty!!!");
		} else {
			System.out.println("Stay Points Calculations: Lsp has something!!!");
		}*/
		return Lsp;
	}

	StayPoints estimateStayPoint(ArrayList<GPS> list, int start, int end) {
		StayPoints sp = new StayPoints();
		double lat = list.get(start).getUlatitude();
		double lon = list.get(end).getUlongtitude();
		Date Tstart = list.get(start).getTimestamp(); 
		Date Tend = list.get(end).getTimestamp();
		/*
		 * for (int i=start+1;i<=end;i++){ lat=estimateCentroid(lat,
		 * list.get(i).getUlatitude()); lon=estimateCentroid(lon,
		 * list.get(i).getUlongtitude()); }
		 */
		double a[] = estimateCentroid(list, start, end);
		lat = a[0];
		lon = a[1];
		sp.setAll(lat, lon, Tstart, Tend);
		return sp;
	}

	long TimeDifference(Date d1, Date d2) {
		// Find time difference between given variables and return it to
		// dd:HH:mm:ss format
			// in milliseconds
			long diff = 0;
			if (d2.getTime() >= d1.getTime())
				diff = d2.getTime() - d1.getTime();
			else
				diff = d1.getTime() - d2.getTime();
			
		// System.out.println("Time Difference :"+TimeDiff);
		return diff;
	}

	/*
	 * double SpaceDistance(GPS gps1, GPS gps2){ //Find Pythagorean distance
	 * calculation between given variables double distance=0,lat=0,lon=0; if
	 * (gps1.getUlatitude()>gps2.getUlatitude()){
	 * lat=gps1.getUlatitude()-gps2.getUlatitude(); } else {
	 * lat=gps2.getUlatitude()-gps1.getUlatitude(); } lat=lat*lat; if
	 * (gps1.getUlongtitude()>gps2.getUlongtitude()){
	 * lon=gps1.getUlongtitude()-gps2.getUlongtitude(); } else {
	 * lon=gps2.getUlongtitude()-gps1.getUlongtitude(); } lon=lon*lon;
	 * distance=Math.sqrt(lat+lon); return distance; }
	 */

	double SpaceDistance(GPS gps1, GPS gps2) {
		// Find distance calculation between given variables from
		// http://www.movable-type.co.uk/scripts/latlong.html
		double glat = gps1.getUlatitude() * Math.PI / 180;
		double alat = gps2.getUlatitude() * Math.PI / 180;
		double glon = gps1.getUlongtitude() * Math.PI / 180;
		double alon = gps2.getUlongtitude() * Math.PI / 180;
		double R = 6371000;
		double df = alat - glat;
		double dl = alon - glon;
		double a = Math.sin(df / 2) * Math.sin(df / 2) + Math.cos(glat)
				* Math.cos(alat) * Math.sin(dl / 2) * Math.sin(dl / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		return Math.abs(d);
	}

	double[] estimateCentroid(ArrayList<GPS> points, int start, int end) {
		double centerx = 0, centery = 0;
		int counter = 0;
		for (int i = start; i <= end; i++) {
			centerx = centerx + points.get(i).getUlatitude();
			centery = centery + points.get(i).getUlongtitude();
			counter++;
		}
		centerx = centerx / counter;
		centery = centery / counter;
		double a[] = { centerx, centery };
		return a;
	}

}
