package com.netmanagement.nmapp;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.netmanagement.csvdatasets.ParseAccessPoints;
import com.netmanagement.csvdatasets.ParseBaseStations;
import com.netmanagement.csvdatasets.ParseBattery;
import com.netmanagement.csvdatasets.ParseGPS;
import com.netmanagement.dataprocessing.AccessPointsCalculations;
import com.netmanagement.dataprocessing.BaseStationsCalculations;
import com.netmanagement.dataprocessing.BatteryCalculations;
import com.netmanagement.dataprocessing.BatteryEcoRoute;
import com.netmanagement.dataprocessing.GPSCalculations;
import com.netmanagement.dataprocessing.PoICalculations;
import com.netmanagement.entities.AccessPoints;
import com.netmanagement.entities.BaseStations;
import com.netmanagement.entities.Battery;
import com.netmanagement.entities.GPS;
import com.netmanagement.entities.PointsofInterest;
import com.netmanagement.entities.StayPoints;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ServicesController {
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping({"/","/index"})
	   public String index() {
		   return "forward:static/index.html";
	   }
	
	 
	 @RequestMapping(value = "/fullscreenMap", method = RequestMethod.GET)
	   public String fullscreenMap() {
	     
	      return "static/fullscreenMap.html";
	   }
	 
	 @RequestMapping(value = "/BatteryGraph", method = RequestMethod.GET)
	   public String BatteryGraph() {
	     
	      return "static/BatteryGraph.html";
	   }
	
	 
	 @RequestMapping(value = "/dataAnalysis", method = RequestMethod.GET)
	   public String dataAnalysis() {
	     
	      return "static/dataAnalysis.html";
	   }
	 
	 @RequestMapping(value = "/dataVisualization", method = RequestMethod.GET)
	   public String dataVisualization() {
	     
	      return "static/dataVisualization.html";
	   }
	 
	 @RequestMapping(value = "/Bar-Diagrams", method = RequestMethod.GET)
	   public String BarDiagrams() {
	     
	      return "static/BarDiagrams.html";
	   }
	 
	 
	 @RequestMapping(value = "/csvRequest", method = RequestMethod.GET)
	 public @ResponseBody String csvRequest(@RequestParam int option) {
		 int suc = -1; // suc = 0 for success
		 String retstr = "";
		 if(option == 0) {
			 ParseAccessPoints pcsv = ParseAccessPoints.getInstance();
			 ParseBattery pb = ParseBattery.getInstance();
			 ParseGPS GPS = ParseGPS.getInstance();
			 ParseBaseStations pbs = ParseBaseStations.getInstance();
			 if(pcsv.getLoaded() == 0 && pb.getLoaded() == 0 && GPS.getLoaded() == 0 && pbs.getLoaded() == 0) {
				 
				 try {
						if((pcsv.LoadAccessPoints() == 0) && (pb.LoadBattery() == 0 ) && (GPS.LoadGPS() == 0) && (pbs.LoadBaseStations() == 0)){
							suc = 0;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 	if(suc == 0) {
				 		retstr = "All-import";
				 	}
				 	else {
				 		retstr = "error-import";
				 	}
				
					
			 }
			 else {
				 retstr = "all-already-imported";
			 }
		 }
		 else if(option == 1){
			 ParseAccessPoints pcsv = ParseAccessPoints.getInstance();
			 if(pcsv.getLoaded() == 0) {
				 
				 try {
						suc = pcsv.LoadAccessPoints();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 if(suc == 0) {
						 
						 HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints.getInstance().getHap();
						 if (!hap.isEmpty())
						 {
								if(AccessPointsCalculations.getInstance().EstimatedPointPosition() == 1){
									retstr = "wifi-import-ep-ok"; 
								}
								
						 }
						else 
						{
							retstr = "ep-problem"; 
						}
						 
					 }
					
			 }
			 else {
				 retstr = "ap-already-imported";
			 }
			 
		 }
		 else if(option == 2){
			 
			 ParseBattery pb = ParseBattery.getInstance();
			 if(pb.getLoaded() == 1){
				 retstr = "bat-already-imported";
			 }
			 else {
				 try {
						suc = pb.LoadBattery();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 if(suc == 0){
						 System.out.println("battery: " + suc);
						 retstr = "battery-import";
					 }
			 }
				 
			 
			 
			 
		 } else if(option == 3){
			 
			 ParseGPS GPS = ParseGPS.getInstance();
			 if(GPS.getLoaded() == 1){
				 retstr = "gps-already-imported";	 
			 }
			 else {
				 try {
						suc = GPS.LoadGPS();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 if(suc == 0){
						 //System.out.println("battery: " + suc);
						 retstr = "gps-import";
					 }
			 }
			 
			 
			 
		 }
		 else if(option == 4){
			 
			 ParseBaseStations pbs = ParseBaseStations.getInstance();
			 if(pbs.getLoaded() == 1) {
				 retstr = "bs-already-imported";
				 
			 }
			 else 
			 {
				 try {
						suc = pbs.LoadBaseStations();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 if(suc == 0){
						// System.out.println("battery: " + suc);
						 retstr = "bs-import";
					 }
					 
			 }
			
			 
		 } 
		 else {
			 retstr =  "problem";
		 }
		 return retstr;
	 }
	 
	 
	 @RequestMapping(value = "/getUsers", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String RetrieveUsers(@RequestParam int arg) {
		 
		 if(arg == 0){
			 ParseAccessPoints pcsv = ParseAccessPoints.getInstance();
			 if(pcsv.getLoaded() == 0){ // access points dataset is not imported
				return new Gson().toJson("ap-not-loaded");
			 }
			 else {
			
				 ArrayList<String> users = AccessPointsCalculations.getInstance().getUsers();
				 String json = new Gson().toJson(users);
				 return json;
			 }
			 			
		 }
		 else {
			 ParseGPS pgps = ParseGPS.getInstance();
			 if(pgps.getLoaded() == 0){
				 return new Gson().toJson("gps-not-loaded");
			 }
			 else {
				 ArrayList<String> users = GPSCalculations.getInstance().getUsers();
				 String json = new Gson().toJson(users);
				 return json;
			 }
		 }
		
		 
		 
		 
	 }
	 
	 /*@RequestMapping(value = "/AccessPointEstimation", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String getEstimation() {
		 HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints.getInstance().getHap();
		 ParseAccessPoints pcsv = ParseAccessPoints.getInstance();
		 if(pcsv.getLoaded() == 0){ // access points dataset is not imported
				return new Gson().toJson("ap-not-loaded");
		 }
		 else {
				if (!hap.isEmpty()){
					ArrayList<APResults> retList = AccessPointsCalculations.getInstance().EstimatedPointPosition();
					String json = new Gson().toJson(retList);
					
					return json;
				}
				else {
					return "hashMap is empty";
				}
		 }
		
	   }*/
	 
	 @RequestMapping(value = "/getDates", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String getDates(@RequestParam String userID,@RequestParam int arg) {
		 if(arg == 0) {
			 if(ParseAccessPoints.getInstance().getLoaded() == 0){
				 return new Gson().toJson("ap-not-loaded");
			 }
			 else {
				 AccessPointsCalculations apc = AccessPointsCalculations.getInstance();	
				 ArrayList<String> dates = apc.allTimestamps(userID);
				 String json = new Gson().toJson(dates);
				 return json;
			 }
		 }
		 else {
			 if(ParseGPS.getInstance().getLoaded() == 0){
				 return new Gson().toJson("gps-not-loaded");
			 }
			 else {
				 	
				 ArrayList<String> dates = GPSCalculations.getInstance().allTimestamps(userID);
				 String json = new Gson().toJson(dates);
				 return json;
			 }
		 }
		 
		
	 }
	 
	 
	 @RequestMapping(value = "/getApInfo", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String getApInfo(@RequestParam String userID, @RequestParam String startDate, @RequestParam String endDate) {
		 
		 ParseAccessPoints pcsv = ParseAccessPoints.getInstance();
		 if(pcsv.getLoaded() == 0){
			 return new Gson().toJson("ap-not-loaded");
		 }
		 else {
			 AccessPointsCalculations apc = AccessPointsCalculations.getInstance(); 	
			 ArrayList<AccessPoints> apInfo = apc.searchUser(userID, startDate, endDate);
			 System.out.println("returning from searchUser");
			 if(!apInfo.isEmpty()){
				 
				 String json = new Gson().toJson(apInfo);
				 System.out.println("List size: " + apInfo.size());
				 System.out.println("json string: " + json);
			     return json;	
			 }
			 else {
				 System.out.println("ApInfo is empty");
				 return "Uncreated List";
			 }
		 }
		 
		
		
	 }
	 
	 
	 @RequestMapping(value = "/BatteryInfo", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String getBatteryInfo(@RequestParam String userID, @RequestParam String startDate, @RequestParam String endDate) {
		 
		 ParseBattery pb = ParseBattery.getInstance();
		 if(pb.getLoaded() == 0){
			 return new Gson().toJson("bat-not-loaded");
		 }
		 else {
			 BatteryCalculations bc = BatteryCalculations.getInstance();
			 ArrayList<Battery> batlist = bc.searchUser(userID, startDate, endDate);
			 if(!batlist.isEmpty()){
				 String json = new Gson().toJson(batlist);
				 System.out.println("json string: " + json);
			     return json;	 
			 }
			 else {
				 System.out.println("bat is empty");
				 return "Uncreated List";
			 }
			 
		 }
		
		 
	 }
	 
	 
	 
	 
	 @RequestMapping(value = "/CellsInfo", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String getCellsInfo(@RequestParam String userID, @RequestParam String startDate, @RequestParam String endDate) {
		 
		 if(ParseBaseStations.getInstance().getLoaded() == 0){
			 return new Gson().toJson("bs-not-loaded");
		 }
		 else {
			 BaseStationsCalculations bsc = BaseStationsCalculations.getInstance();
			 ArrayList<BaseStations> bslist = bsc.searchUser(userID, startDate, endDate);
			 if(!bslist.isEmpty()){
				 String json = new Gson().toJson(bslist);
				 System.out.println("json string: " + json);
			     return json;	 
			 }
			 else {
				 System.out.println("bslist is empty");
				 return new Gson().toJson("No info");
			 }
		 }
		
		 
	 }
	 
	 @RequestMapping(value = "/Stay-Points", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String StayPoints(@RequestParam String userID, @RequestParam String startDate, @RequestParam String endDate,@RequestParam String Dmax, 
			   @RequestParam String Tmin,  @RequestParam String Tmax ) {
		 
		 System.out.println("user: " + userID + " startDate: " + startDate + " endDate: " + endDate + " Dmax: " + Dmax + " Tmin: " + Tmin
				 + " Tmax: " + Tmax);
		 
		 if(ParseGPS.getInstance().getLoaded() == 0){
			 return new Gson().toJson("gps-not-loaded");
		 }
		 else {
			 GPSCalculations gps = GPSCalculations.getInstance();
			 ArrayList<GPS> GPSPoints = gps.searchUser(userID, startDate, endDate);
			 if(!GPSPoints.isEmpty()) {
				 String json = null;
				 Double Dmaxd = Double.parseDouble(Dmax);
				 ArrayList<StayPoints> stayPoints = gps.findStayPoints(GPSPoints, Tmin, Tmax, Dmaxd);
				 if(!stayPoints.isEmpty()){
					 System.out.println("Stay points: " +stayPoints.size());
					 json = new Gson().toJson(stayPoints);
					 System.out.println("json string: " + json);
				     return json;
				 }
				 else {
					 System.out.println("stayPoints is empty");
					 return new Gson().toJson("No info stayPoints");
				 }
			 }
			 else {
				 System.out.println("GPSPoints is empty");
				 return new Gson().toJson("No info GPSPoints");
			 }
			 
		 }

	 }
	 
	 
	 @RequestMapping(value = "/POI", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String CalculatePOI(@RequestParam String startDate, @RequestParam String endDate,@RequestParam String Dmax, 
			   @RequestParam String Tmin,  @RequestParam String Tmax, @RequestParam String eps, @RequestParam String minPts ) {
		 
		 System.out.println(" startDate: " + startDate + " endDate: " + endDate + " Dmax: " + Dmax + " Tmin: " + Tmin
				 + " Tmax: " + Tmax +" eps: " + eps + " minPts: " + minPts  );
		 
		 
		 if(ParseGPS.getInstance().getLoaded() == 0){
			 return new Gson().toJson("gps-not-loaded");
		 }
		 else {
			 PoICalculations poi = PoICalculations.getInstance();
			 double Dmaxd = Double.parseDouble(Dmax);
			 double epsd = Double.parseDouble(eps);
			 int minPtsd = Integer.parseInt(minPts);
			 poi.setAll(startDate,endDate,Tmin,Tmax,Dmaxd,epsd,minPtsd);
			 ArrayList<PointsofInterest> poiArray = poi.CalculatePoI();
			 if(!poiArray.isEmpty()) {
				 
			 	String json = new Gson().toJson(poiArray);
				System.out.println("json string: " + json);
			    return json;
			 
			 }
			 else {
			 	return new Gson().toJson("poi-problem");
			 
			 
			 }
		 }
		
		 
		
	 }
	 
	 /* ----------------------------------------- Bar Diagrams ------------------------------------------ */
	 /* Bar Diagram 1 */
	 @RequestMapping(value = "/Low-Battery-Graph", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String BarDiagram1(){
		if(ParseBattery.getInstance().getLoaded() == 0){
			 return new Gson().toJson("bat-not-loaded");
		}
		else{
			ArrayList<String> blist = BatteryCalculations.getInstance().LowBatterySearch();
			if(!blist.isEmpty()){
				String json = new Gson().toJson(blist);
				System.out.println("json string: " + json);
			    return json;
			}
			else {
				System.out.println("low battery list is empty");
				String json = new Gson().toJson("bar-bat-problem");
			    return json;
			}
		}
		 

	 }
	 
	 /* Bar Diagram 2 */
	 @RequestMapping(value = "/Operators-Users-Graph", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String BarDiagram2(){
		 if(ParseBaseStations.getInstance().getLoaded() == 0) {
			 return new Gson().toJson("bs-not-loaded");
		 }
		 else {
			ArrayList<String> op_users = BaseStationsCalculations.getInstance().Operators_numofUsers(); 
			if(!op_users.isEmpty()){
				String json = new Gson().toJson(op_users);
				System.out.println("json string: " + json);
			    return json;
			}
			else {
				System.out.println("Operators-Users is empty");
				String json = new Gson().toJson("bar-users-problem");
			    return json;
			}
		 }
		 
	 }
	 
	 
	 /*--------------------------------------------- Battery Economic Route ---------------------------------------*/
	 @RequestMapping(value = "/Battery-Economic-Route", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String EcoRoute(@RequestParam String userID, @RequestParam String startDate, @RequestParam String endDate){
		 
	     ArrayList<GPS> gpsList = BatteryEcoRoute.getInstance().UserRoute(userID, startDate, endDate);
		 ArrayList<AccessPoints> ecoList = BatteryEcoRoute.getInstance().EcoMarkers(userID, startDate, endDate);
		 //ArrayList<AccessPoints> ecoList = BatteryEcoRoute.getInstance().EcoMinRoute(userID, startDate, endDate);
		 if(!ecoList.isEmpty()){
			
			 	String json1 = new Gson().toJson(gpsList);
			 	
			 	String json2 = new Gson().toJson(ecoList);
			 	String bothjson = "["+json1+","+json2+"]";
				System.out.println("json string: " + bothjson);
				System.out.println("Eco Route length: " + ecoList.size());
			    return bothjson;
		 }
		 else
		 {
			 	System.out.println("EcoList is empty");
				String json = new Gson().toJson("Economic-route-problem");
			    return json;
		 }
		 
	 }
	
}
