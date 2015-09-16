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
import com.netmanagement.csvdatasets.ParseGPS;
import com.netmanagement.entities.AccessPoints;
import com.netmanagement.entities.GPS;

public class BatteryEcoRoute {
	private static BatteryEcoRoute BatteryEcoRouteinstance = null;
	private int currentMaxRSSI;
	private int pos = -1;
	static final int RADIUS = 30; /* Default value for the radius */
	static final float TIMESLACK = 3; /* Default value for the time slack */

	private BatteryEcoRoute() {
	}

	public static BatteryEcoRoute getInstance() {
		if (BatteryEcoRouteinstance == null) {
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

	public static BatteryEcoRoute getBatteryEcoRouteinstance() {
		return BatteryEcoRouteinstance;
	}

	public static void setBatteryEcoRouteinstance(
			BatteryEcoRoute batteryEcoRouteinstance) {
		BatteryEcoRouteinstance = batteryEcoRouteinstance;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<GPS> UserRoute(String userID, String startDate,
			String endDate) {
		// Return an array list with given user gps coordinates between
		// startDate and endDate
		HashMap<String, ArrayList<GPS>> hgps = ParseGPS.getInstance().getHap();
		ArrayList<GPS> gpsList = new ArrayList<GPS>();
		if (!hgps.isEmpty()) {
			Set<?> set = hgps.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				// System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<GPS> array = (ArrayList<GPS>) me.getValue();
				for (int i = 0; i < array.size(); i++) {
					GPS tempgps = array.get(i);
					if (tempgps.getUser().equals(userID)) {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd");

							Date date1 = sdf.parse(startDate);
							Date date2 = sdf.parse(endDate);
							Date dateu = sdf.parse(tempgps.getTimestamp());

							// System.out.println(date1+" | "+date2+" | "+dateu);
							if (date1.equals(dateu) || date1.before(dateu)) {
								if (date2.equals(dateu) || date2.after(dateu)) {
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
	public ArrayList<AccessPoints> EcoRoute(String userID, String startDate,
			String endDate, float time_slack, int radius) {

		HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints
				.getInstance().getHap();
		ArrayList<AccessPoints> ecoList = new ArrayList<AccessPoints>();
		ArrayList<AccessPoints> APList = new ArrayList<AccessPoints>();

		/* Get a gps list for the specific user, for the specific timeline */
		ArrayList<GPS> gpsList = UserRoute(userID, startDate, endDate);
		/* Do the same for the access points */
		if (!hap.isEmpty()) {
			Set<?> set = hap.entrySet();
			Iterator<?> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				// System.out.println("Key : "+me.getKey()+" Value : "+me.getValue());
				ArrayList<AccessPoints> array = (ArrayList<AccessPoints>) me
						.getValue();
				for (int i = 0; i < array.size(); i++) {
					AccessPoints tempap = array.get(i);
					if (tempap.getUser().equals(userID)) {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date date1 = sdf.parse(startDate);
							Date date2 = sdf.parse(endDate);
							Date dateu = sdf.parse(tempap.getTimestamp());
							// System.out.println(date1+" | "+date2+" | "+dateu);
							if (date1.equals(dateu) || date1.before(dateu)) {
								if (date2.equals(dateu) || date2.after(dateu)) {
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
		if (time_slack == -1) {
			time_slack = TIMESLACK;
		}
		if (radius == -1) {
			radius = RADIUS;
		}

		AccessPoints tempap = null;
		for (int i = 0; i < gpsList.size(); i++) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date gpsdate = sdf.parse(gpsList.get(i).getTimestamp());
				tempap = null;
				for (int j = 0; j < APList.size(); j++) {
					Date apdate = sdf.parse(APList.get(j).getTimestamp());
					System.out.println("Time Diff: "
							+ Math.abs(apdate.getTime() - gpsdate.getTime()));
					System.out.println("Space Dist: "
							+ SpaceDistance(gpsList.get(i), APList.get(j)));
					if (Math.abs(apdate.getTime() - gpsdate.getTime()) <= time_slack * 1000) {

						if (SpaceDistance(gpsList.get(i), APList.get(j)) <= radius) {

							if (tempap == null) {
								tempap = new AccessPoints();
								tempap.setBssid(APList.get(j).getBssid());
								tempap.setAPlatitude(APList.get(j)
										.getAPlatitude());
								tempap.setAPlongtitude(APList.get(j)
										.getAPlongtitude());
								tempap.setFrequency(APList.get(j)
										.getFrequency());
								tempap.setRssi(APList.get(j).getRssi());
								tempap.setSsid(APList.get(j).getSsid());
								tempap.setTimestamp(APList.get(j)
										.getTimestamp());
								tempap.setUser(APList.get(j).getUser());
								tempap.setId(APList.get(j).getId());
							} else {
								if (tempap.getBssid() != APList.get(j)
										.getBssid()
										&& tempap.getRssi() < APList.get(j)
												.getRssi()) {
									tempap = new AccessPoints();
									tempap.setBssid(APList.get(j).getBssid());
									tempap.setAPlatitude(APList.get(j)
											.getAPlatitude());
									tempap.setAPlongtitude(APList.get(j)
											.getAPlongtitude());
									tempap.setFrequency(APList.get(j)
											.getFrequency());
									tempap.setRssi(APList.get(j).getRssi());
									tempap.setSsid(APList.get(j).getSsid());
									tempap.setTimestamp(APList.get(j)
											.getTimestamp());
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
			if (tempap != null)
				ecoList.add(tempap);
		}
		System.out.println(ecoList.size());
		return ecoList;
	}

	/*
	 * class BSSIDDist implements Comparable<BSSIDDist>{ double dist; String
	 * bssid;
	 * 
	 * public double getDist() { return dist; }
	 * 
	 * public void setDist(double dist) { this.dist = dist; }
	 * 
	 * public String getBssid() { return bssid; }
	 * 
	 * public void setBssid(String bssid) { this.bssid = bssid; }
	 * 
	 * @Override public int compareTo(BSSIDDist o) { return
	 * Double.compare(getDist(), o.getDist());
	 * 
	 * }
	 * 
	 * }
	 */

	double SpaceDistance(GPS gps, AccessPoints ap) {
		// Find distance calculation between given variables from
		// http://www.movable-type.co.uk/scripts/latlong.html
		double glat = gps.getUlatitude() * Math.PI / 180;
		double alat = ap.getAPlatitude() * Math.PI / 180;
		double glon = gps.getUlongtitude() * Math.PI / 180;
		double alon = ap.getAPlongtitude() * Math.PI / 180;
		double R = 6371000;
		double df = alat - glat;
		double dl = alon - glon;
		double a = Math.sin(df / 2) * Math.sin(df / 2) + Math.cos(glat)
				* Math.cos(alat) * Math.sin(dl / 2) * Math.sin(dl / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c;
		return Math.abs(d);
	}

	/*
	 * double SpaceDistance(GPS gps, AccessPoints ap){ //Find Pythagorean
	 * distance calculation between given variables double
	 * distance=0,lat=0,lon=0; if (gps.getUlatitude()>ap.getAPlatitude()){
	 * lat=gps.getUlatitude()-ap.getAPlatitude(); } else {
	 * lat=ap.getAPlatitude()-gps.getUlatitude(); } lat=lat*lat; if
	 * (gps.getUlongtitude()>ap.getAPlongtitude()){
	 * lon=gps.getUlongtitude()-ap.getAPlongtitude(); } else {
	 * lon=ap.getAPlongtitude()-gps.getUlongtitude(); } lon=lon*lon;
	 * distance=Math.sqrt(lat+lon); return distance; }
	 */

}
