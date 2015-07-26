var completedEP = 0; // 0: estimation process is not complete, 1: is complete
/*varaibles to determine if the datasets ara imported */
var bat = 0;
var ap = 0;
var all = 0;
var gps = 0;
var bs = 0;
var analysisPage = 0;

var output = '';

function requestsHandler(arg)
{
	if(arg == "estimationProcess")
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
	else if(arg == "all")
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


function createTableEP() {
	
				var epdata = JSON.parse(sessionStorage.getItem('ep'));
	 			  var header = $('<h2 id="headerTagId"></h2>').text('Final Estimation Point');
	 			  $('#headerTag').append(header);
	 			  
	 			 var table =  $('<table id="tableID"></table>');
	 		     
	 		     head = $('<tr></tr>');
	 		     var headData1 = $('<th></th>').text('BSSID');
	 		     head.append(headData1); 
	 		     var headData2 = $('<th></th>').text('Latitude');
	 		     head.append(headData2);
	 		     var headData3 = $('<th></th>').text('Longtitude');
	 		     head.append(headData3);
	 		     table.append(head);
	 		
	 			   $.each(epdata,function(i,item){
	 				  // $orders.append('<li>bssid: ' + item.BSSID + 'lan: ' + item.APlatitude + 'lon: '+ item.APlongtitude + '</li>');
	 				  row = $('<tr></tr>');
	 	               
	 	              var rowData1 = $('<td></td>').text(item.BSSID);
	 	              var rowData2 = $('<td></td>').text(item.APlatitude);
	 	              var rowData3 = $('<td></td>').text(item.APlongtitude);
	 	                //rowData.id=i;
	 	                row.append(rowData1);
	 	                row.append(rowData2);
	 	                row.append(rowData3);
	 	                table.append(row);
	 			   })
	 			   
	 			  if ($('#tableID').length) {
				        $("#bssidTable tr:first").after(row);
				    
				   }
				   else {
					  // var table =  $('#tableID');
				       $('#bssidTable').append(table);
				   } 
	 			  console.log('table passed');  
	 			 clickableMenuProcess(0);
}



function removeTableEP() {
	
	//var closeBtn = document.querySelector('#removal');
	if(!document.getElementById("tableID") || !document.getElementById("headerTagId")){
		alert("Table does not exists to be removed");
	}else {
		
		var tab = document.querySelector('#tableID');
	 	var hTag = document.querySelector('#headerTagId');
	 	
	 	tab.parentNode.removeChild(tab); 
		hTag.parentNode.removeChild(hTag);
		document.getElementById("showEP").hide();
		document.getElementById("removalEP").hide();
		clickableMenuProcess(1); 
	}
    


}


function clickableMenuProcess(option){
	if(option == 0){
		var link = document.getElementById('starterEP');
		link.href="javascript:void(0);"
		link.style.color="grey";
	}
	else {
		var link = document.getElementById('starterEP');
		link.href="javascript:requestsHandler('estimationProcess');"
			link.style.color="#000";
			link.onmouseover= function(){this.style.color="red";}
			link.onmouseout = function(){this.style.color="#000";}
			
	}
}





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
						   all = 1;
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
					   if(resp == 'wifi-import'){
						   ap = 1;
						   $("#popupText").text("Wifi dataSet imported correctly");
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
						   bat = 1;
						   $("#popupText").text("Battery dataSet imported correctly.\n Please continue your process");
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
						   gps = 1;
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
					   bs = 1;
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
				   else {
					   $("#popupText").text("Base Stations NOT imported correctly");
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
		clickableMenuAnalysis(0, 1);
	}
	if(ap == 1) {
		$.ajax({ 
			type: "GET",
		    dataType: "json",
		    contentType: "application/json",
		    url: "/NetManagementApp/getUsers",
		   success: function(data){
			   options(data);
		   },
		   error:function(XMLHttpRequest, textStatus, errorThrown){
			   console.log('error',textStatus + " " + errorThrown);
				   alert('Get Users error loading response');
			   }
			});
	}
	else {
		
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
	
	
	
}


function getAvUserDates(userID) {
	
	console.log(userID);
	$.ajax({ 
		type: "GET",
	    dataType: "json",
	    data:{userID:userID},
	    contentType: "application/json",
	    url: "/NetManagementApp/getDates",
	   success: function(data){
		   datePicker(data);
	   },
	   
	   error:function(XMLHttpRequest, textStatus, errorThrown){
		   console.log('error',textStatus + " " + errorThrown);
			   alert('Get Users error loading response');
		   }
		});
	
	//return "success";
	
}


function getApInfo() {
	
	if(completedEP == 1) { // estimation process is completed
		
		$.ajax({ 
			   type: "GET",
			   dataType: "json",
			   data:{userID:userID,startDate:startDate,endDate:endDate},
			   contentType: "application/json",
			   url: "/NetManagementApp/getApInfo",
			   success: function(data){
				   //console.log('success',data);
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
								},
							NO:
								function(){
								$(this).dialog('close');
								
								}
						}
						}); 
			   
			   },
			   error: function(XMLHttpRequest, textStatus, errorThrown){
				   console.log('error',textStatus + " " + errorThrown);
	 			   alert('Get App Info error loading response');
	 		   }
	 		});

	}
	else {
			$("#popupText").text("Estimation Point is not calculated. \n Do you want to calculate it automatically \n or redirect to dataProcessing page ?");
		   $("#divpopup").dialog({
				title: "ACCESS POINTS",
				width: 430,
				height: 200,
				modal:true,
				buttons: {
					Automatically: 
						function(){
						$(this).dialog('close');
						estimationProcess();
						},
					Redirect:
						function(){
						$(this).dialog('close');
						toDataProcessing();
						}
				}
				}); 
			
		
	}
		
}




function getBatteryInfo() {
	
		
		$.ajax({ 
			   type: "GET",
			   dataType: "json",
			   data:{userID:userID,startDate:startDate,endDate:endDate},
			   contentType: "application/json",
			   url: "/NetManagementApp/BatteryInfo",
			   success: function(data){
				   //console.log('success',data);
				   $("#popupText").text("Battery Info gathering comleted. Load Graph ?");
				   $("#divpopup").dialog({
						title: "ACCESS POINTS",
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
									
								},
							NO:
								function(){
								$(this).dialog('close');
								
								}
						}
						}); 
			   
			   },
			   error: function(XMLHttpRequest, textStatus, errorThrown){
				   console.log('error',textStatus + " " + errorThrown);
	 			   alert('Get App Info error loading response');
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
				   else {
					 //console.log('success in getting cell info');
					   $("#popupText").text("Base Stations Info gathering comleted. Load Graph ?");
					   $("#divpopup").dialog({
							title: "ACCESS POINTS",
							width: 430,
							height: 200,
							modal:true,
							buttons: {
								YES: 
									function(){
									$(this).dialog('close');
									// call for new popup window with graph
										sessionStorage.setItem('bs',JSON.stringify(data));
										DrawCells();
										//window.open('BatteryGraph',"width=400, height=400");
										
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
	
	if(gps == 1) {
		
		$.ajax({ 
			   type: "GET",
			   dataType: "json",
			   data:{userID:userID,startDate:startDate,endDate:endDate,Dmax:Dmax,Tmin:Tmin,Tmax:Tmax},
			   contentType: "application/json",
			   url: "/NetManagementApp/Stay-Points",
			   success: function(data){
				   //console.log('success',data);
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
									//sessionStorage.setItem('stay-points',JSON.stringify(data));
									DrawStayPoints(data);
									
								},
							NO:
								function(){
								$(this).dialog('close');
								sessionStorage.setItem('stay-points',JSON.stringify(data));
								}
						}
						}); 
			   
			   },
			   error: function(XMLHttpRequest, textStatus, errorThrown){
				   console.log('error',textStatus + " " + errorThrown);
	 			   alert('Get App Info error loading response');
	 		   }
	 		});
		//clickableMenuAnalysis(1, 2);
		
	}
	else {
		
		requestsHandler('GPS');
	}
	
}



