
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
			   
			   $("#popupText").text("BSSID estimation process completed successfully. Do you want to show result ?");
			   $("#divpopup").dialog({
					title: "ESTIMATION PROCESS",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						YES: 
							function(){
							$(this).dialog('close');
							createTableEP(data);
							},
						NO:
							function(){
							$(this).dialog('close');
							
							}
					}
					}); 
		   
		   },
		   error: function(){
 			   alert('error loading response');
 		   }
 		});
}


function createTableEP(data) {

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
	 		
	 			   $.each(data,function(i,item){
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
	 			 clickableMenu(1);
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
		clickableMenu(0); 
	}
    


}


function clickableMenu(option){
	if(option == 1){
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
				   case 1: 
					   if(resp == 'wifi-import'){
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
										}
								}
								}); 
						   
					   } 
				   break;
				   case 2: 
					   if(resp == 'battery-import'){
						   $("#popupText").text("Battery dataSet imported correctly");
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
				   }
				   
				 
			   },
			   error: function(){
				  alert('Error getting response');
			   }
			});
	}
	
	
}


function getUsers() {
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


function getApInfo() {
	
	$.ajax({ 
		   type: "GET",
		   dataType: "json",
		   data:{userID:userID,startDate:startDate,endDate:endDate},
		   contentType: "application/json",
		   url: "/NetManagementApp/getApInfo",
		   success: function(data){
			   //console.log('success',data);
			   $("#popupText").text("Access point gathering comleted. Load Map ?");
			   $("#divpopup").dialog({
					title: "ACCESS POINTS",
					width: 430,
					height: 200,
					modal:true,
					buttons: {
						YES: 
							function(){
							$(this).dialog('close');
							loadMap(data);
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






