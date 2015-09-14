map = null;
var view = -1; // 0-> markers, 1->polyline, 2->cells
var flightPath=null;
var mapCreated = 0;
var markersAP = [];
var markersCells  = [];
var markersEco = [];

//mapSP = null;
function Markers(data) {
			
			view = 0;
			var latlng = null;
			sessionStorage.setItem('apdata',JSON.stringify(data));
	 		if(mapCreated == 1) {
	 			if(flightPath!=null){
	 				
	 				flightPath.setMap(null);
	 				if(markersEco.length != 0) {
	 					 for (var i = 0; i < markersEco.length; i++) {
	 						    markersEco[i].setMap(null);
	 						  }
	 				}
	 			}
	 			if(markersAP.length != 0) {
		 			deleteMarkers(0);
		 		}
	 			//if(MarkersCells.length !=0 ) {
	 				//deleteMarkers(1);
	 			//}
	 		}
	 		
	 		$.each(data,function(i,item){
	 			latlng = new google.maps.LatLng(item.APlatitude, item.APlongtitude);
	 			if (mapCreated == 1) {
	 				
	 				var marker = new google.maps.Marker({
		                position: latlng,
		                map: map,
		               title: item.ssid,
		                animation: google.maps.Animation.DROP,
		            });
	 				markersAP.push(marker);
			 		
			 		var content = "<p>" + 'SSID: ' + item.ssid + "<br />" + 'BSSID: ' + item.bssid + "<br />" + ' Mean RSSI: ' + item.rssi + '<br />' + ' frequency: ' + item.frequency + 
			 		 "<br />" + "latitude:  " + item.APlatitude + "<br />" + "longtitude:  " + item.APlongtitude + 
			 		"<br />" + 'Timestamp: ' + item.timestamp + "</p>";     

		 			var infowindow = new google.maps.InfoWindow();

		 			google.maps.event.addListener(marker,'click', (function(marker,content,infowindow){ 
		 			        return function() {
		 			           infowindow.setContent(content);
		 			           infowindow.open(map,marker);
		 			        };
		 			    })(marker,content,infowindow));
	 			}
	 			else {
	 				var mapOptions = {
		 		    		zoom: 8,
		 		    		center: latlng,
		 		    		mapTypeId: google.maps.MapTypeId.TERRAIN
		 		 		 };
		 		      	
		 		 		 map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);
		 		 		 mapCreated = 1;
	 			}
	 			
	 		});
			console.log('ending scipt Markers');
			
}

//Sets the map on all markers in the array.
function setAllMap(map,option) {
	if(option == 0) {
		if(markersAP.length != 0) {
			 for (var i = 0; i < markersAP.length; i++) {
				    markersAP[i].setMap(map);
				  }
		}
	}
	else if(option == 1) {
		if(markersCells.length != 0) {
			
			for (var i = 0; i < markersCells.length; i++) {
			    markersCells[i].setMap(map);
			  }
		}
	}
	else {
		if(markersAP.length != 0) {
			 for (var i = 0; i < markersAP.length; i++) {
				    markersAP[i].setMap(map);
				  }
		}
		if(markersCells.length != 0) {
			
			for (var i = 0; i < markersCells.length; i++) {
			    markersCells[i].setMap(map);
			  }
		}
		
	}
	
 
}

function clearMarkers(option) {
	  setAllMap(null,option);
	}
//Deletes all markers in the array by removing references to them.
function deleteMarkers(option) {
	if(option == 0){
		clearMarkers(0);	
	}
	else if(option == 1) {
		clearMarkers(1);
	}
	else {
		clearMarkers(2);
		
	}
	markersAP = [];
	markersCells = [];
  
}



function CenterControl(controlDiv, map) {

	  // Set CSS for the control border
	  var controlUI = document.createElement('div');
	  controlUI.style.backgroundColor = '#fff';
	  controlUI.style.border = '2px solid #fff';
	  controlUI.style.borderRadius = '3px';
	  controlUI.style.boxShadow = '0 2px 6px rgba(0,0,0,.3)';
	  controlUI.style.cursor = 'pointer';
	  controlUI.style.marginTop = '5px';
	  controlUI.style.textAlign = 'center';
	  controlUI.title = 'Click to Estimate Economic Route';
	  controlDiv.appendChild(controlUI);

	  // Set CSS for the control interior
	  var controlText = document.createElement('div');
	  controlText.style.color = 'rgb(25,25,25)';
	  controlText.style.fontFamily = 'Roboto,Arial,sans-serif';
	  controlText.style.fontSize = '16px';
	  controlText.style.lineHeight = '38px';
	  controlText.style.paddingLeft = '5px';
	  controlText.style.paddingRight = '5px';
	  controlText.innerHTML = 'Battery Economic Route';
	  controlUI.appendChild(controlText);

	  // Setup the click event listeners: simply set the map to
	  // Chicago
	  google.maps.event.addDomListener(controlUI, 'click', function() {
		  EconomicRoute();
	  });

	}


function Polyline(){
	
		
	var sentdata = JSON.parse(sessionStorage.getItem('apdata'));
	view = 1;
		if(markersAP.length != 0) {
			
			var flightPlanCoordinates = [];
			$.each(sentdata,function(i,item){
				
				flightPlanCoordinates.push(new google.maps.LatLng(item.APlatitude, item.APlongtitude));
				
				
			});
			var centerControlDiv = document.createElement('div');
			centerControlDiv.setAttribute("id", "EcoRoute");
			if(!$('#EcoRoute').length) {
				var centerControl = new CenterControl(centerControlDiv, map);
			}
			

			centerControlDiv.index = 1;
			map.controls[google.maps.ControlPosition.TOP_CENTER].push(centerControlDiv);
			
			
			 flightPath = new google.maps.Polyline({
				path: flightPlanCoordinates,
				geodesic: true,
				strokeColor: '#FF0000',
				strokeOpacity: 1.0,
				strokeWeight: 2
				});
			flightPath.setMap(map);
	
			
		}	
			
			
	
}

function BatEcoRoute(data) {
	  flightPath.setMap(null);
	  deleteMarkers(2);
	  
	  var latlng = null;
	  //var pinColor = "FE6256";
	  //console.log(data);	
		//var split=data.split('#');
		var gpsRoute = data[0];
		var ecoMarkers = data[1];
		console.log('after split');

			
			var flightPlanCoordinates = [];
			$.each(gpsRoute,function(i,item){
				/*if(i==0) {
					
					var mapOptions = {
		 		    		zoom: 8,
		 		    		center: new google.maps.LatLng(item.Ulatitude, item.Ulongtitude),
		 		    		mapTypeId: google.maps.MapTypeId.TERRAIN
		 		 		 };
		 		      	
		 		 		 map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);
					
					
				}*/
				
				flightPlanCoordinates.push(new google.maps.LatLng(item.Ulatitude, item.Ulongtitude));
					
			});	
		 			
		 				flightPath = new google.maps.Polyline({
						path: flightPlanCoordinates,
						geodesic: true,
						strokeColor: '#0000FF',
						strokeOpacity: 1.0,
						strokeWeight: 2
						});
					flightPath.setMap(map);
			
		
			
		
		$.each(ecoMarkers,function(i,item){
		
			var pinImage = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld="+(i+1)+"|FF0000|000FFF",
			        new google.maps.Size(21, 40),
			        new google.maps.Point(0,0),
			        new google.maps.Point(10, 34));
			latlng = new google.maps.LatLng(item.APlatitude, item.APlongtitude);
 			
			var marker = new google.maps.Marker({
	                position: latlng,
	                map: map,
	                icon: pinImage,
	                title: item.ssid,
	                animation: google.maps.Animation.DROP,
	            });
			markersEco.push(marker);
			var content = "<p>" + 'Number: ' + (i+1) + "<br />" + 'SSID: ' + item.ssid + "<br />" + 'BSSID: ' + item.bssid + "<br />" + ' RSSI: ' + item.rssi + '<br />' + ' frequency: ' + item.frequency + 
	 		 "<br />" + "latitude:  " + item.APlatitude + "<br />" + "longtitude:  " + item.APlongtitude + "<br />"+'Timestamp: ' + item.timestamp + "</p>";     

			var infowindow = new google.maps.InfoWindow();

			google.maps.event.addListener(marker,'click', (function(marker,content,infowindow){ 
			        return function() {
			           infowindow.setContent(content);
			           infowindow.open(map,marker);
			        };
			    })(marker,content,infowindow)); 
		});
		
			
			
		
		console.log('Ending economy route');
	  
	}



function DrawCells(bsdata) {
	view = 2;
	if(mapCreated == 1) {
		if(flightPath!=null) {
			flightPath.setMap(null);
			if(markersEco.length != 0) {
				 for (var i = 0; i < markersEco.length; i++) {
					    markersEco[i].setMap(null);
					  }
			}
		}
		//if(MarkersCells.length !=0 ) {
			//	deleteMarkers(1);
		//}
	}
	var pinColor = "52bdb0";
	var pinImage = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + pinColor,
	        new google.maps.Size(21, 40),
	        new google.maps.Point(0,0),
	        new google.maps.Point(10, 34));
	$.each(bsdata,function(i,item){
		var latlng = new google.maps.LatLng(item.BSlatitude, item.BSlongtitude);
		
		if(mapCreated == 1) {
			
			 var marker = new google.maps.Marker({
	                position: latlng,
	                map: map,
	                icon: pinImage,
	                title: item.Operator,
	                animation: google.maps.Animation.DROP,
	            });
			markersCells.push(marker);	   
				   
				 
		 		var content = "<p>" + "Operator: " + item.Operator + "<br />" + " MCC: " + item.mcc + "<br />"  + " MNC: " + item.mnc + "<br />" 
		 		+  " cid: " + item.cid + "<br />" + " LAC: " + item.lac + "</p>";     

	 			var infowindow = new google.maps.InfoWindow();

	 			google.maps.event.addListener(marker,'click', (function(marker,content,infowindow){ 
	 			        return function() {
	 			           infowindow.setContent(content);
	 			           infowindow.open(map,marker);
	 			        };
	 			    })(marker,content,infowindow)); 
				
			
			
		}
		else {
			var mapOptions = {
		    		zoom: 8,
		    		center: latlng,
		    		mapTypeId: google.maps.MapTypeId.TERRAIN
		 		 };
		      	
		 		 map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);
		 		 mapCreated = 1;
		}
			
	
			  
			
		});
	console.log('Ending DrawCells');
	
}


function DrawStayPoints(data) {
	//var spdata = null;
	//var latlng = null;
	//var marker = null;
	view = 3;
	if(data == null){
		console.log('Data is null');
		 data = JSON.parse(sessionStorage.getItem('stay-points'));
	}
	else {
		
		var pinColor = "0000FF";
		var pinImage = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + pinColor,
		        new google.maps.Size(21, 40),
		        new google.maps.Point(0,0),
		        new google.maps.Point(10, 34));
		
		$.each(data,function(i,item){
			
			var latlng = new google.maps.LatLng(item.lat, item.lon);
			if(i==0){
 				
 				
 		      	var mapOptions = {
 		    		zoom: 8,
 		    		center: latlng,
 		    		mapTypeId: google.maps.MapTypeId.TERRAIN
 		 		 };
 		      	
 		 		 map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);
 	
 				}
			    var marker = new google.maps.Marker({
                position: latlng,
                map: map,
               icon: pinImage,
               title: 'Stay Point',
                animation: google.maps.Animation.DROP,
            });
		//console.log('mapSP: ', mapSP);
		

		 		var content = "<p>" + "Lat: " + item.lat + "<br />" + " Lon: " + item.lon + "<br />" + " Tstart: " + item.Tstart + "<br />" + " Tend: " + item.Tend + "</p>";  

	 			var infowindow = new google.maps.InfoWindow();

	 			google.maps.event.addListener(marker,'click', (function(marker,content,infowindow){ 
	 			        return function() {
	 			           infowindow.setContent(content);
	 			           infowindow.open(map,marker);
	 			        };
	 			    })(marker,content,infowindow)); 
				
				
		});
		

		
	}
	console.log('Ending STAY stay points');
	
}


function DrawPOI(data) {
	
	view = 4;
	var mapPOI=null;
	if(data == null){
		alert('Null data in POI');
	}
	else {
		
		var cluster = 0;
		$.each(data,function(i,item){
			
			cluster++;
			if(i==0)
			{
				var latlng = new google.maps.LatLng(item.startlat, item.startlon);
					
			      	var mapOptions = {
			    		zoom: 8,
			    		center: latlng,
			    		mapTypeId: google.maps.MapTypeId.TERRAIN
			 		 };
			      	
			      	mapPOI = new google.maps.Map(document.getElementById('map-canvas2'),mapOptions);
		
			}
			console.log('startlat: ', item.startlat);
			console.log('endlat: ', item.endlat);
			console.log('startlon: ', item.startlon);
			console.log('endlon: ', item.endlon);
			
			var rectangle = new google.maps.Rectangle({
			    strokeColor: '#FF0000',
			    strokeOpacity: 0.8,
			    strokeWeight: 2,
			    fillColor: '#FF0000',
			    fillOpacity: 0.35,
			    map: mapPOI,
			    bounds: new google.maps.LatLngBounds(
			      new google.maps.LatLng(item.startlat, item.startlon),
			      new google.maps.LatLng(item.endlat, item.endlon))
			  });
			
			var ne = rectangle.getBounds().getNorthEast();
			var content = "<p>" + "Cluster: " + cluster + "</p>";  

 			var infowindow = new google.maps.InfoWindow();

 			google.maps.event.addListener(rectangle,'click', (function(rectangle,content,infowindow){ 
 			        return function() {
 			           infowindow.setContent(content);
 			           infowindow.setPosition(ne);
 			           infowindow.open(map,rectangle);
 			        };
 			    })(rectangle,content,infowindow)); 
		
		
		});
	}
	
	console.log('ending Draw Points')
	
}



/*
function fullscreenMode(){
	
		window.open('fullscreenMap', '_blank');
	
}
*/

