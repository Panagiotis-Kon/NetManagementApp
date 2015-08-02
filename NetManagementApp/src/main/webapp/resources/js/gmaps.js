map = null;
var view = -1; // 0-> markers, 1->polyline, 2->cells
var flightPath;

var markers = [];

//mapSP = null;
function Markers(data) {

			view = 0;
			var latlng = null;
			sessionStorage.setItem('apdata',JSON.stringify(data));
	 		
	 		$.each(data,function(i,item){
	 			latlng = new google.maps.LatLng(item.APlatitude, item.APlongtitude);
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
		               title: item.ssid,
		                animation: google.maps.Animation.DROP,
		            });
	 			markers.push(marker);
			 		
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
	 			
	 			
	 		});
			console.log('ending scipt');

}

//Sets the map on all markers in the array.
function setAllMap(map) {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(map);
  }
}

function clearMarkers() {
	  setAllMap(null);
	}
//Deletes all markers in the array by removing references to them.
function deleteMarkers() {
  clearMarkers();
  markers = [];
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
			//Markers(sentdata);
		
		
			view = 1;
			
			var flightPlanCoordinates = [];
			$.each(sentdata,function(i,item){
				
				flightPlanCoordinates.push(new google.maps.LatLng(item.APlatitude, item.APlongtitude));
				
				
			});
			var centerControlDiv = document.createElement('div');
			var centerControl = new CenterControl(centerControlDiv, map);

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

function BatEcoRoute(data) {
	  flightPath.setMap(null);
	  deleteMarkers();
	  var latlng = null;
	  	
		
		$.each(data,function(i,item){
		
			latlng = new google.maps.LatLng(item.APlatitude, item.APlongtitude);
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
	               title: item.ssid,
	                animation: google.maps.Animation.DROP,
	            });
			markers.push(marker);
			var content = "<p>" + 'SSID: ' + item.ssid + "<br />" + 'BSSID: ' + item.bssid + "<br />" + ' RSSI: ' + item.rssi + '<br />' + ' frequency: ' + item.frequency + 
	 		 "<br />" + "latitude:  " + item.APlatitude + "<br />" + "longtitude:  " + item.APlongtitude + "<br />"+'Timestamp: ' + item.timestamp + "</p>";     

			var infowindow = new google.maps.InfoWindow();

			google.maps.event.addListener(marker,'click', (function(marker,content,infowindow){ 
			        return function() {
			           infowindow.setContent(content);
			           infowindow.open(map,marker);
			        };
			    })(marker,content,infowindow)); 
		});
		var flightPlanCoordinates = [];
		$.each(data,function(i,item){
			flightPlanCoordinates.push(new google.maps.LatLng(item.APlatitude, item.APlongtitude));
				
		});	
	 			
	 			var flightPath1 = new google.maps.Polyline({
					path: flightPlanCoordinates,
					geodesic: true,
					strokeColor: '#0000FF',
					strokeOpacity: 1.0,
					strokeWeight: 2
					});
				flightPath1.setMap(map);
			
			
		
		console.log('Ending economy route');
	  
	}

/*
function BatteryGraph() {
	getBatteryInfo(); 
	
}*/

/*function Cells() {

		
		//console.log("Trying to gather cell info")
		getCellsInfo();
	
}*/

function DrawCells(bsdata) {
	view = 2;
	//var bsdata = JSON.parse(sessionStorage.getItem('bs'));

	var pinColor = "52bdb0";
	var pinImage = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + pinColor,
	        new google.maps.Size(21, 40),
	        new google.maps.Point(0,0),
	        new google.maps.Point(10, 34));
	$.each(bsdata,function(i,item){
		var latlng = new google.maps.LatLng(item.BSlatitude, item.BSlongtitude);
			if(i==0)
			{

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
                title: item.Operator,
                animation: google.maps.Animation.DROP,
            });
			   
			   
			 
	 		var content = "<p>" + "Operator: " + item.Operator + "<br />" + " MCC: " + item.mcc + "<br />"  + " MNC: " + item.mnc + "<br />" 
	 		+  " cid: " + item.cid + "<br />" + " LAC: " + item.lac + "</p>";     

 			var infowindow = new google.maps.InfoWindow();

 			google.maps.event.addListener(marker,'click', (function(marker,content,infowindow){ 
 			        return function() {
 			           infowindow.setContent(content);
 			           infowindow.open(map,marker);
 			        };
 			    })(marker,content,infowindow)); 
			
			
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
		

		 		var content = "<p>" + "Tstart: " + item.Tstart + "<br />" + " Tend: " + item.Tend + "</p>";  

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




function fullscreenMode(){
	
		window.open('fullscreenMap', '_blank');
	
}


