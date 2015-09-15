package com.netmanagement.csvdatasets;

public class ParseAll {
	int setAll() throws Exception{
		int all=0;
		all=all+ParseAccessPoints.getInstance().LoadAccessPoints();
		all=all+ParseBaseStations.getInstance().LoadBaseStations();
		all=all+ParseBattery.getInstance().LoadBattery();
		all=all+ParseGPS.getInstance().LoadGPS();
		return all;
	}
	
	int Loaded(){
		int all=1;
		all=all+ParseAccessPoints.getInstance().getLoaded();
		all=all+ParseBaseStations.getInstance().getLoaded();
		all=all+ParseBattery.getInstance().getLoaded();
		all=all+ParseGPS.getInstance().getLoaded();
		return all;
	}

}
