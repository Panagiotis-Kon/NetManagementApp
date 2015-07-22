var map = null;
var view = -1; // 0-> markers, 1->polyline, 2->cells

function Markers(data) {

		view = 0;
    	//location of marker
		//var myLatlng=null;
		var latlng = null;
		var marker = null;
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


function Polyline(){
	if( ap == 1) {
		
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
		getCellsInfo();
		
	}
	else {
		
		requestsHandler('BaseStations');
	}
	

}

function DrawCells() {
	
	var bsdata = JSON.parse(sessionStorage.getItem('bs'));
	var latlng = null;
	var marker = null;
	
	$.each(data,function(i,item){
			latlng = new google.maps.LatLng(item.APlatitude, item.APlongtitude);
			
			var marker = new google.maps.Marker({
                position: latlng,
                map: map,
               title: item.ssid,
                animation: google.maps.Animation.DROP,
            });
			marker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png');
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
	
}

function fullscreenMode(){
	if(view == 0) {
		// open full screen for markers
		window.open('fullscreenMap', '_blank');
	}
	else if(view == 1) {
		// open full screen for polyline
		window.open('fullscreenMap', '_blank');
	}
	
	else if(view == 2){
		// open full screen for cells
	}
}


