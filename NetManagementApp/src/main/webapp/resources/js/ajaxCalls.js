
/*varaibles to determine if the datasets ara imported */
/*var bat = 0;
var ap = 0;
var all = 0;
var gps = 0;
var bs = 0;*/
var analysisPage = 0;

var output = '';

function requestsHandler(arg)
{
	/*if(arg == "estimationProcess")
	{
		estimationProcess();
		
	}
	else if(arg == "removeTableEP")
	{
		removeTableEP();
	}
	else if(arg == "createTableEP"){
		createTableEP();
	}
	*/ 
	if(arg == "All")
	{
		csvRequest(0);
		
	}
	else if(arg == "AccessPoints")
	{
		csvRequest(1);
	}
	else if(arg == "Battery")
	{
		csvRequest(2);
	}
	else if(arg == "GPS")
	{
		csvRequest(3);
	}
	else if(arg == "BaseStations")
	{
		csvRequest(4);
	}

}

/*
function estimationProcess() {
	
	$.ajax({ 
		   type: "GET",
		   dataType: "json",
		  contentType: "application/json",
		   url: "/NetManagementApp/AccessPointEstimation",
		   success: function(data){
			   completedEP = 1;
			   sessionStorage.setItem('ep',JSON.stringify(data));
			   
			   $("#popupText").text("BSSID estimation process completed successfully");
			   $("#divpopup").dialog({
					title: "ESTIMATION PROCESS",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						OK: 
							function(){
							$(this).dialog('close');
							$("#showEP").show();
					        $("#removalEP").show();
							},
						
						}
					}); 
		   
		   },
		   error: function(){
 			   alert('error loading response');
 		   }
 		});
}

*/





function csvRequest(option)
{
	var resp = '';
	if(option>=0 && option<5){
	
		$.ajax({
			 
			   type: "GET",
			   dataType: "text",
			   data:{option:option},
			   url: "/NetManagementApp/csvRequest",
			  
			        
			   
			   success: function(data){ 
				  
				   resp = data;
				   switch(option) {
				   case 0: 
					   if(resp == 'All-import'){
						   //all = 1;
						   $("#popupText").text("All dataSets imported correctly");
						   $("#divpopup").dialog({
								title: "DATASET IMPORT",
								width: 430,
								height: 200,
								modal:true,
								buttons: {
									OK: 
										function(){
										$(this).dialog('close');
										
										}
								}
								}); 
					   }
					   else if(resp == "all-already-imported"){
						   $("#popupText").text("All dataSets already imported");
						   $("#divpopup").dialog({
								title: "DATASET IMPORT",
								width: 430,
								height: 200,
								modal:true,
								buttons: {
									OK: 
										function(){
										$(this).dialog('close');
										//callback();
										}
								}
								}); 
						    
						   
					   }
					   else {
						   $("#popupText").text("All dataSets NOT imported correctly");
						   $("#divpopup").dialog({
								title: "DATASET IMPORT",
								width: 430,
								height: 200,
								modal:true,
								buttons: {
									OK: 
										function(){
										$(this).dialog('close');
										}
								}
								}); 
						   
					   } 
				   break;
				   case 1: 
					   if(resp == 'wifi-import-ep-ok'){
						  // ap = 1;
						   $("#popupText").html("Wifi dataSet imported correctly and Estimation Point calculated.<br/>Please Continue your process");
						   $("#divpopup").dialog({
								title: "DATASET IMPORT",
								width: 430,
								height: 300,
								modal:true,
								buttons: {
									OK: 
										function(){
										$(this).dialog('close');
										
										}
								}
								}); 
					   }
					   else if(resp == "ap-already-imported"){
						   $("#popupText").text("Wifi dataSet already imported");
						   $("#divpopup").dialog({
								title: "DATASET IMPORT",
								width: 430,
								height: 200,
								modal:true,
								buttons: {
									OK: 
										function(){
										$(this).dialog('close');
										//callback();
										}
								}
								}); 
						    
						   
					   }
					   else if(resp == "ep-problem"){
						   $("#popupText").html("A problem occured in estimation process.<br />Please try again or check the server for errors");
						   $("#divpopup").dialog({
								title: "ESTIMATION PROCESS",
								width: 430,
								height: 300,
								modal:true,
								buttons: {
									OK: 
										function(){
										$(this).dialog('close');
										//callback();
										}
								}
								}); 
						    
					   }
					   else {
						   $("#popupText").text("Wifi dataSet NOT imported correctly");
						   $("#divpopup").dialog({
								title: "DATASET IMPORT",
								width: 430,
								height: 200,
								modal:true,
								buttons: {
									OK: 
										function(){
										$(this).dialog('close');
										//callback();
										}
								}
								}); 
						   
					   } 
				   break;
				   case 2: 
					   if(resp == 'battery-import'){
						   //bat = 1;
						   $("#popupText").html("Battery dataSet imported correctly.<br/> Please continue your process");
						   $("#divpopup").dialog({
								title: "DATASET IMPORT",
								width: 430,
								height: 200,
								modal:true,
								buttons: {
									OK: 
										function(){
										$(this).dialog('close');
										
										}
								}
								}); 
					   }
					   else if(resp == 'bat-already-imported'){
						   $("#popupText").text("battery dataSet already imported");
						   $("#divpopup").dialog({
								title: "DATASET IMPORT",
								width: 430,
								height: 200,
								modal:true,
								buttons: {
									OK: 
										function(){
										$(this).dialog('close');
										}
								}
								}); 
						   
					   }
					   else {
						   $("#popupText").text("battery dataSet NOT imported correctly");
						   $("#divpopup").dialog({
								title: "DATASET IMPORT",
								width: 430,
								height: 200,
								modal:true,
								buttons: {
									OK: 
										function(){
										$(this).dialog('close');
										}
								}
								}); 
						   
					   } 
				   break;
				   case 3:
					   if(resp == 'gps-import'){
						  // gps = 1;
						   $("#popupText").text("GPS dataSet imported correctly");
						   $("#divpopup").dialog({
								title: "DATASET IMPORT",
								width: 430,
								height: 200,
								modal:true,
								buttons: {
									OK: 
										function(){
										$(this).dialog('close');
										
										}
								}
								}); 
					   }
					   else if(resp == 'gps-already-imported') {
						   $("#popupText").text("GPS dataSet already imported");
						   $("#divpopup").dialog({
								title: "DATASET IMPORT",
								width: 430,
								height: 200,
								modal:true,
								buttons: {
									OK: 
										function(){
										$(this).dialog('close');
										}
								}
								}); 
						   
					   }
					   else {
						   $("#popupText").text("GPS dataSet NOT imported correctly");
						   $("#divpopup").dialog({
								title: "DATASET IMPORT",
								width: 430,
								height: 200,
								modal:true,
								buttons: {
									OK: 
										function(){
										$(this).dialog('close');
										}
								}
								}); 
						   
					   } 
				   break;
				   case 4:
				   if(resp == 'bs-import'){
					  // bs = 1;
					   $("#popupText").text("Base Stations dataSet imported correctly");
					   $("#divpopup").dialog({
							title: "DATASET IMPORT",
							width: 430,
							height: 200,
							modal:true,
							buttons: {
								OK: 
									function(){
									$(this).dialog('close');
									
									}
							}
							}); 
				   }
				   else if(resp == 'bs-already-imported')
				   {
					   $("#popupText").text("Base Stations dataSet already imported");
					   $("#divpopup").dialog({
							title: "DATASET IMPORT",
							width: 430,
							height: 200,
							modal:true,
							buttons: {
								OK: 
									function(){
									$(this).dialog('close');
									
									}
							}
							}); 
				   }
				   else{
					   $("#popupText").html("Base Stations NOT imported correctly.<br/> Please Try again or check the server");
					   $("#divpopup").dialog({
							title: "DATASET IMPORT",
							width: 430,
							height: 200,
							modal:true,
							buttons: {
								OK: 
									function(){
									$(this).dialog('close');
									}
							}
							}); 
					   
				   } 
			       break;
				   }
				   
				 
			   },
			   error: function(){
				  alert('Error getting response');
			   }
			});
	}
	
	
}


function getUsers(arg) {
	if(arg == 1){
		analysisPage = 1;
		
	}
	console.log('arg: ', arg);
		$.ajax({ 
			type: "GET",
		    dataType: "json",
		    data:{arg:arg},
		    contentType: "application/json",
		    url: "/NetManagementApp/getUsers",
		   success: function(data){
			   if(data == "ap-not-loaded"){
				   clickableMenuVisual(1, 1);
				   $("#popupText").text("Sorry. First you must import Access Points Dataset");
				   $("#divpopup").dialog({
						title: "IMPORT PORBLEM",
						width: 430,
						height: 200,
						modal:true,
						buttons: {
							OK: 
								function(){
								$(this).dialog('close');
								
							}
						}
					}); 
			   }
			   else if(data == "gps-not-loaded"){
				   clickableMenuAnalysis(1, 1);
				   $("#popupText").text("Sorry. First you must import GPS Dataset");
				   $("#divpopup").dialog({
						title: "IMPORT PORBLEM",
						width: 430,
						height: 200,
						modal:true,
						buttons: {
							OK: 
								function(){
								$(this).dialog('close');
								
							}
						}
					}); 
			   }
			   else {
				   clickableMenuAnalysis(0, 1);
				   clickableMenuVisual(0, 1);
				   //options(data);
				   SelectUser(data);
			   }
			   
		   },
		   error:function(XMLHttpRequest, textStatus, errorThrown){
			   console.log('error',textStatus + " " + errorThrown);
				   alert('Get Users error loading response');
			   }
			});

}


function getAvUserDates(userID,arg) {
	
	console.log(userID);
	$.ajax({ 
		type: "GET",
	    dataType: "json",
	    data:{userID:userID,arg:arg},
	    contentType: "application/json",
	    url: "/NetManagementApp/getDates",
	   success: function(data){
		   
		   if(data == "ap-not-loaded"){
			   clickableMenuVisual(1, 1);
			   $("#popupText").text("Sorry. First you must import Access Points Dataset");
			   $("#divpopup").dialog({
					title: "IMPORT PORBLEM",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						OK: 
							function(){
							$(this).dialog('close');
							
						}
					}
				}); 
		   }
		   else if(data == "gps-not-loaded"){
			   clickableMenuAnalysis(1, 1);
			   $("#popupText").text("Sorry. First you must import GPS Dataset");
			   $("#divpopup").dialog({
					title: "IMPORT PORBLEM",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						OK: 
							function(){
							$(this).dialog('close');
							
						}
					}
				}); 
		   }
		   else {
			   Pickerdate(data);
		   }
		   
		   //console.log(data);
	   },
	   
	   error:function(XMLHttpRequest, textStatus, errorThrown){
		   console.log('error',textStatus + " " + errorThrown);
			   alert('Get Users Dates error loading response');
		   }
		});
	
	//return "success";
	
}







function getApInfo() {
	
		
		$.ajax({ 
			   type: "GET",
			   dataType: "json",
			   data:{userID:userID,startDate:startDate,endDate:endDate},
			   contentType: "application/json",
			   url: "/NetManagementApp/getApInfo",
			   success: function(data){
				   //console.log('success',data);
				   if(data == "ap-not-loaded"){
					   $("#popupText").text("Sorry. First you must import Access Points Dataset");
					   $("#divpopup").dialog({
							title: "IMPORT PORBLEM",
							width: 430,
							height: 200,
							modal:true,
							buttons: {
								OK: 
									function(){
									$(this).dialog('close');
									clickableMenuAnalysis(1, 1);
								}
							}
						}); 
				   }
				   else {
					   $("#popupText").text("Access point gathering comleted. Load Markers on Map ?");
					   $("#divpopup").dialog({
							title: "ACCESS POINTS",
							width: 430,
							height: 200,
							modal:true,
							buttons: {
								YES: 
									function(){
									$(this).dialog('close');
									 $("#map-fullscreen").show();
									 Markers(data);
									 getBatteryInfo();
									},
								NO:
									function(){
									$(this).dialog('close');
									
									}
							}
							});  
				   }
				  
			   
			   },
			   error: function(XMLHttpRequest, textStatus, errorThrown){
				   console.log('error',textStatus + " " + errorThrown);
	 			   alert('Get App Info error loading response');
	 		   }
	 		});
	
		
}




function getBatteryInfo() {
	
		console.log("getBatteryInfo Ajax");
		$.ajax({ 
			   type: "GET",
			   dataType: "json",
			   data:{userID:userID,startDate:startDate,endDate:endDate},
			   contentType: "application/json",
			   url: "/NetManagementApp/BatteryInfo",
			   success: function(data){
				   //console.log('success',data);
				   if(data == "bat-not-loaded"){
					   $("#popupText").text("Sorry. First you must import Battery Dataset");
					   $("#divpopup").dialog({
							title: "IMPORT PORBLEM",
							width: 430,
							height: 200,
							modal:true,
							buttons: {
								OK: 
									function(){
									$(this).dialog('close');
									clickableMenuAnalysis(1, 1);
								}
							}
						}); 
					   
				   }
				   else {
					  graph(data);
					  // BatteryUserChart(data);
					   /*$("#popupText").text("Battery Info gathering comleted. Load Graph ?");
					   $("#divpopup").dialog({
							title: "BATTERY",
							width: 430,
							height: 200,
							modal:true,
							buttons: {
								YES: 
									function(){
										$(this).dialog('close');
									// call for new popup window with graph
										sessionStorage.setItem('battery',JSON.stringify(data));
										window.open('BatteryGraph',"width=400, height=400");
										//BatteryChart(data);
										//BatteryUserChart(data);
									},
								NO:
									function(){
									$(this).dialog('close');
									
									}
							}
							}); */
				   }
				   
			   
			   },
			   error: function(XMLHttpRequest, textStatus, errorThrown){
				   console.log('error',textStatus + " " + errorThrown);
	 			   alert('Get Battery Info error loading response');
	 		   }
	 		});

	
}




function getCellsInfo() {
	
		
		$.ajax({ 
			   type: "GET",
			   dataType: "json",
			   data:{userID:userID,startDate:startDate,endDate:endDate},
			   contentType: "application/json",
			   url: "/NetManagementApp/CellsInfo",
			   success: function(data){
				   if(data == "No info"){
					   $("#popupText").text("No data for this period. Try other period");
					   $("#divpopup").dialog({
							title: "CELLS",
							width: 430,
							height: 200,
							modal:true,
							buttons: {
								OK: 
									function(){
									$(this).dialog('close');
									
									}
							}
						}); 
					   
				   }
				   else if(data == "bs-not-loaded"){
					   $("#popupText").text("Base stations dataset is not imported");
					   $("#divpopup").dialog({
							title: "CELLS",
							width: 430,
							height: 200,
							modal:true,
							buttons: {
								OK: 
									function(){
									$(this).dialog('close');
									
									}
							}
						}); 
				   }
				   else {
					 //console.log('success in getting cell info');
					   $("#popupText").text("Base Stations Info gathering comleted. Load Graph ?");
					   $("#divpopup").dialog({
							title: "CELLS",
							width: 430,
							height: 200,
							modal:true,
							buttons: {
								YES: 
									function(){
									$(this).dialog('close');
								
										//sessionStorage.setItem('bs',JSON.stringify(data));
										DrawCells(data);
										
										
									},
								NO:
									function(){
									$(this).dialog('close');
									
									}
							}
							}); 
				   
				   }
				   
			   },
			   error: function(XMLHttpRequest, textStatus, errorThrown){
				   console.log('error',textStatus + " " + errorThrown);
	 			   alert('Cells error loading response');
	 		   }
	 		});
		//sessionStorage.setItem('bs',JSON.stringify(data));
		//DrawCells();

}


function StayPoints() {
	
	
		
		$.ajax({ 
			   type: "GET",
			   dataType: "json",
			   data:{userID:userID,startDate:startDate,endDate:endDate,Dmax:Dmax,Tmin:Tmin,Tmax:Tmax},
			   contentType: "application/json",
			   url: "/NetManagementApp/Stay-Points",
			   success: function(data){
				   //console.log('success',data);
				   if(data == "gps-not-loaded"){
					   $("#popupText").text("GPS dataset is not imported. Please import dataset");
					   $("#divpopup").dialog({
							title: "GPS",
							width: 430,
							height: 200,
							modal:true,
							buttons: {
								OK: 
									function(){
									$(this).dialog('close');
									
									}
							}
						}); 
				   }
				   else {
					   $("#popupText").text("Stay Points created. Load on Map?");
					   $("#divpopup").dialog({
							title: "STAY POINTS",
							width: 430,
							height: 200,
							modal:true,
							buttons: {
								YES: 
									function(){
										$(this).dialog('close');
										 $("#map-fullscreen").show();
										 DrawStayPoints(data);
										
									},
								NO:
									function(){
									$(this).dialog('close');
									sessionStorage.setItem('stay-points',JSON.stringify(data));
									}
							}
							}); 
				   }
				   
				  
			   
			   },
			   error: function(XMLHttpRequest, textStatus, errorThrown){
				   console.log('error',textStatus + " " + errorThrown);
	 			   alert('Get Stay Points error loading response');
	 		   }
	 		});
		//clickableMenuAnalysis(1, 2);
		
	
	
}


function CalculatePOI(dataArray){
	
	$.ajax({ 
		type: "GET",
	    dataType: "json",
	    data:{startDate:dataArray[0],endDate:dataArray[1],Dmax:dataArray[2],Tmin:dataArray[3],Tmax:dataArray[4],eps:dataArray[5],minPts:dataArray[6]},
	    contentType: "application/json",
	    url: "/NetManagementApp/POI",
	   success: function(data){
		   if(data == "poi-problem"){
			   
			   $("#popupText").text("POI problem detected. Check the server");
			   $("#divpopup").dialog({
					title: "POI'S",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						OK: 
							function(){
								$(this).dialog('close');
								
							}
						
					}
					}); 
			   
			   
		   }
		   else if(data == "gps-not-loaded"){
			   $("#popupText").text("GPS dataset is not imported. Please import dataset");
			   $("#divpopup").dialog({
					title: "GPS",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						OK: 
							function(){
							$(this).dialog('close');
							
							}
					}
				}); 
		   }
		   else {
			   $("#popupText").text("POI's have been calculated. Load on Map?");
			   $("#divpopup").dialog({
					title: "POI'S",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						YES: 
							function(){
								$(this).dialog('close');
								// draw the pois
								
								DrawPOI(data);
								
							},
						NO:
							function(){
							$(this).dialog('close');
							
							}
					}
					}); 
			   
		   }
	   },
	   
	   error:function(XMLHttpRequest, textStatus, errorThrown){
		   console.log('error',textStatus + " " + errorThrown);
			   alert('POI error loading response');
		   }
		});
	
}

/*----------------------------------------- Bar Diagrams ---------------------------------------*/



function graphBatUsers() {
	$.ajax({ 
		type: "GET",
	    dataType: "json",
	    contentType: "application/json",
	    url: "/NetManagementApp/Low-Battery-Graph",
	   success: function(data){
		   if(data == "bar-bat-problem"){
			   
			   $("#popupText").text("Bar Diagram 1 problem detected. Check the server");
			   $("#divpopup").dialog({
					title: "Bar Diagram 1",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						OK: 
							function(){
								$(this).dialog('close');
								
							}
						
					}
					}); 
			   
			   
		   }
		   else if(data == "bat-not-loaded"){
			   $("#popupText").text("Battery dataSet is not loaded!");
			   $("#divpopup").dialog({
					title: "DATASET IMPORT",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						OK: 
							function(){
							$(this).dialog('close');
							
							}
					}
					}); 
		   }
		   else {
			   $("#popupText").html("Bar Diagram 1 has been generated.<br/> Load Diagram?");
			   $("#divpopup").dialog({
					title: "Bar Diagram 1",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						YES: 
							function(){
								$(this).dialog('close');
								DrawDiagram1(data);
								
							},
						NO:
							function(){
							$(this).dialog('close');
							
							}
					}
					}); 
			   
		   }
	   },
	   
	   error:function(XMLHttpRequest, textStatus, errorThrown){
		   console.log('error',textStatus + " " + errorThrown);
			   alert('Bar Diagram 1 error loading response');
		   }
		});
	
	
	
}



function graphOpUsers() {
	
	$.ajax({ 
		type: "GET",
	    dataType: "json",
	    contentType: "application/json",
	    url: "/NetManagementApp/Operators-Users-Graph",
	   success: function(data){
		   if(data == "bar-users-problem"){
			   
			   $("#popupText").html("Bar Diagram 2 problem detected.<br/> Check the server");
			   $("#divpopup").dialog({
					title: "Bar Diagram 2",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						OK: 
							function(){
								$(this).dialog('close');
								DrawDiagram2(data);
							}
						
					}
					}); 
			   
			   
		   }
		   else if(data == "bs-not-loaded"){
			   $("#popupText").text("Base Stations dataSet is not loaded!");
			   $("#divpopup").dialog({
					title: "DATASET IMPORT",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						OK: 
							function(){
							$(this).dialog('close');
							
							}
					}
					}); 
		   }
		   else {
			   $("#popupText").html("Bar Diagram 2 has been generated.<br/> Load Diagram?");
			   $("#divpopup").dialog({
					title: "Bar Diagram 2",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						YES: 
							function(){
								$(this).dialog('close');
								 DrawDiagram2(data);
								
							},
						NO:
							function(){
							$(this).dialog('close');
							
							}
					}
					}); 
			   
		   }
	   },
	   
	   error:function(XMLHttpRequest, textStatus, errorThrown){
		   console.log('error',textStatus + " " + errorThrown);
			   alert('Bar Diagram 2 error loading response');
		   }
		});
	
}



function EconomicRoute() {
	
	$.ajax({ 
		type: "GET",
	    dataType: "json",
	    data:{userID:userID,startDate:startDate,endDate:endDate},
	    contentType: "application/json",
	    url: "/NetManagementApp/Battery-Economic-Route",
	   success: function(data){
		   if(data == "Economic-route-problem"){
			   
			   $("#popupText").html("Economic-route problem Detected.<br/> Check the server");
			   $("#divpopup").dialog({
					title: "Economic Route",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						OK: 
							function(){
								$(this).dialog('close');
								
							}
						
					}
					}); 
			   
			   
		   }
		   else {
			   $("#popupText").html("Economic-route calculated.Load on Map?");
			   $("#divpopup").dialog({
					title: "Economic Route",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						OK: 
							function(){
								$(this).dialog('close');
								BatEcoRoute(data);
							}
						
					}
					}); 
		   }
	   },
	   
	   error:function(XMLHttpRequest, textStatus, errorThrown){
		   console.log('error',textStatus + " " + errorThrown);
			   alert('Economic Route error loading response');
		   }
		});
	
}

