var map = null;
var view = -1; // 0-> markers, 1->polyline, 2->cells

function Markers(data) {

		if(ap == 1) {
			
			view = 0;
			var latlng = null;
			sessionStorage.setItem('sent',JSON.stringify(data));
	 		
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
			 		
			 		var content = "SSID: " + item.ssid + "\n" + " Mean RSSI: " + item.rssi + "\n" + '</h3>' + " frequency: " + item.frequency;     

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
		else {
			alert('Please import wifi dataset first');
		}
		
                               
 
}


function Polyline(){
	if( ap == 1) {
		
		if(map == null) {
			Markers(data);
		}
		else {
			view = 1;
			var sentdata = JSON.parse(sessionStorage.getItem('sent'));
			var flightPlanCoordinates = [];
			$.each(sentdata,function(i,item){
				
				flightPlanCoordinates.push(new google.maps.LatLng(item.APlatitude, item.APlongtitude));
				
				
			});
			
			var flightPath = new google.maps.Polyline({
				path: flightPlanCoordinates,
				geodesic: true,
				strokeColor: '#FF0000',
				strokeOpacity: 1.0,
				strokeWeight: 2
				});
			flightPath.setMap(map);
	
		}
		
	}
	else {
		alert('Please import wifi dataset first');
	}
	
	
}

function BatteryGraph() {
	if(bat == 1){ // battery dataset is imported
		getBatteryInfo(); 
	}
	else {
		requestsHandler('Battery');
	}
	
}

function Cells() {
	
	if(bs == 1){
		view = 2;
		//console.log("Trying to gather cell info")
		getCellsInfo();
		
	}
	else {
		
		requestsHandler('BaseStations');
	}
	

}

function DrawCells() {
	//console.log("DrawCells function");
	var latlng = null;
	latlng = new google.maps.LatLng('37.58','23.46');
	var bsdata = JSON.parse(sessionStorage.getItem('bs'));
	if(map == null) { // if a map does not exists create it
		
		for(var item in bsdata) {
			latlng = new google.maps.LatLng(item.BSlatitude, item.BSlongtitude);
			break;
		}
		var mapOptions = {
		    		zoom: 8,
		    		center: latlng,
		    		mapTypeId: google.maps.MapTypeId.TERRAIN
		 		 };
		      	
		  map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);
	}
	
	
	
	
	var pinColor = "52bdb0";
	var pinImage = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + pinColor,
	        new google.maps.Size(21, 40),
	        new google.maps.Point(0,0),
	        new google.maps.Point(10, 34));
	$.each(bsdata,function(i,item){
			latlng = new google.maps.LatLng(item.BSlatitude, item.BSlongtitude);
	
			   var marker = new google.maps.Marker({
                position: latlng,
                map: map,
                icon: pinImage,
                title: item.Operator,
                animation: google.maps.Animation.DROP,
            });
			   
			   
			 
	 		var content = "Operator: " + item.Operator + "\n" + " MCC: " + item.mcc + "\n"  + " MNC: " + item.mnc + "\n" 
	 		+  " cid: " + item.cid + "\n" + " LAC: " + item.lac;     

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

function fullscreenMode(){
	
		window.open('fullscreenMap', '_blank');
	
}


