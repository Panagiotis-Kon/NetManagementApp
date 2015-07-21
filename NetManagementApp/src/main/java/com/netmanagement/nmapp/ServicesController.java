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
import com.netmanagement.dataprocessing.AccessPointsCalculations;
import com.netmanagement.entities.APResults;
import com.netmanagement.entities.AccessPoints;

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
	 @RequestMapping(value = "/dataProcessing", method = RequestMethod.GET)
	   public String dataProcessing() {
	     
	      return "static/dataProcessing.html";
	   }
	 
	 @RequestMapping(value = "/dataVisualization", method = RequestMethod.GET)
	   public String dataVisualization() {
	     
	      return "static/dataVisualization.html";
	   }
	 
	 @RequestMapping(value = "/dataAnalysis", method = RequestMethod.GET)
	   public String dataAnalysis() {
	     
	      return "static/dataAnalysis.html";
	   }
	 
	 @RequestMapping(value = "/csvRequest", method = RequestMethod.GET)
	 public @ResponseBody String csvRequest(@RequestParam int option) {
		 int suc = -1;
		 if(option == 1){
			 ParseAccessPoints pcsv = ParseAccessPoints.getInstance();
			 
			 try {
				suc = pcsv.LoadAccessPoints();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		 }
		 System.out.println(suc);
		 if(suc == 1) {
			
			 return "wifi-import";
		 }
		 else {
			 return "Problem";
		 }
		
	 }
	 
	 
	 @RequestMapping(value = "/getUsers", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String RetrieveUsers() {
		 
		 AccessPointsCalculations apc = AccessPointsCalculations.getInstance();	
		 ArrayList<String> users = apc.getUsers();
		 String json = new Gson().toJson(users);
			
		return json;
		 
		 
	 }
	 
	 @RequestMapping(value = "/AccessPointEstimation", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String getEstimation() {
		 HashMap<String, ArrayList<AccessPoints>> hap = ParseAccessPoints.getInstance().getHap();
			if (!hap.isEmpty()){
				ArrayList<APResults> retList = AccessPointsCalculations.getInstance().EstimatedPointPosition();
				String json = new Gson().toJson(retList);
				
				return json;
			}
			else {
				return "hashMap is empty";
			}
	   }
	 
	 @RequestMapping(value = "/getApInfo", method = RequestMethod.GET,consumes="application/json",produces="application/json")
		
	   public @ResponseBody String getApInfo(@RequestParam String userID, @RequestParam String startDate, @RequestParam String endDate) {
		 	
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
