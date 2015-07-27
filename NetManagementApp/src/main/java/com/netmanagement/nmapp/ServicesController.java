package com.netmanagement.nmapp;



import java.util.ArrayList;
import java.util.HashMap;

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
import com.netmanagement.dataprocessing.GPSCalculations;
import com.netmanagement.entities.APResults;
import com.netmanagement.entities.AccessPoints;
import com.netmanagement.entities.BaseStations;
import com.netmanagement.entities.Battery;
import com.netmanagement.entities.GPS;
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
	
	 @RequestMapping(value = "/login", method = RequestMethod.GET)
	   public String redirect() {
	     
	      return "static/login.html";
	   }
	 
	 @RequestMapping(value = "/fullscreenMap", method = RequestMethod.GET)
	   public String fullscreenMap() {
	     
	      return "static/fullscreenMap.html";
	   }
	 
	 @RequestMapping(value = "/BatteryGraph", method = RequestMethod.GET)
	   public String BatteryGraph() {
	     
	      return "static/BatteryGraph.html";
	   }
	 
	 @RequestMapping(value = "/dataProcessing", method = RequestMethod.GET)
	   public String dataProcessing() {
	     
	      return "static/dataProcessing.html";
	   }
	 
	 @RequestMapping(value = "/dataAnalysis", method = RequestMethod.GET)
	   public String dataAnalysis() {
	     
	      return "static/dataAnalysis.html";
	   }
	 
	 @RequestMapping(value = "/dataVisualization", method = RequestMethod.GET)
	   public String dataVisualization() {
	     
	      return "static/dataVisualization.html";
	   }
	 
	 
	 @RequestMapping(value = "/csvRequest", method = RequestMethod.GET)
	 public @ResponseBody String csvRequest(@RequestParam int option) {
		 int suc = -1;
		 String retstr = "";
		 if(option == 1){
			 ParseAccessPoints pcsv = ParseAccessPoints.getInstance();
			 if(pcsv.getLoaded() == 0) {
				 
				 try {
						suc = pcsv.LoadAccessPoints();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 if(suc == 0) {
						 retstr = "wifi-import"; 
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
		
	   public @ResponseBody String RetrieveUsers() {
		 ParseAccessPoints pcsv = ParseAccessPoints.getInstance();
		 if(pcsv.getLoaded() == 0){ // access points dataset is not imported
			return new Gson().toJson("ap-not-loaded");
		 }
		 else {
			 
			 AccessPointsCalculations apc = AccessPointsCalculations.getInstance();	
			 ArrayList<String> users = apc.getUsers();
			 String json = new Gson().toJson(users);
			 return json;
		 }
		 			
		 
		 
		 
	 }
	 
	 @RequestMapping(value = "/AccessPointEstimation", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
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
		
	   }
	 
	 @RequestMapping(value = "/getDates", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String getDates(@RequestParam String userID) {
		 
		 AccessPointsCalculations apc = AccessPointsCalculations.getInstance();	
		 String udates = apc.minmaxTimestamp(userID);
		 String json = new Gson().toJson(udates);
		 return json;
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
				 System.out.println("Stay Points 269");
				 if(!stayPoints.isEmpty()){
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
	 
	
}
