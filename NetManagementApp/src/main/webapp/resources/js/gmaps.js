var map = null;
var view = -1; // 0-> markers, 1->polyline, 2->cells

function Markers(data) {

		view = 0;
    	//location of marker
		var myLatlng=null;
 		sessionStorage.setItem('sent',JSON.stringify(data));
 		$.each(data,function(i,item){
 			
 			if(i==0){
 				myLatlng = new google.maps.LatLng(item.APlatitude, item.APlongtitude);
 		      	var mapOptions = {
 		    		zoom: 8,
 		    		center: myLatlng,
 		    		mapTypeId: google.maps.MapTypeId.TERRAIN
 		 		 };

 		 		 map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);
 			}
 			else {
 				var latLng = new google.maps.LatLng(item.APlatitude, item.APlongtitude);
 	 			var marker = new google.maps.Marker({
 	                position: latLng,
 	                map: map,
 	                animation: google.maps.Animation.DROP,
 	            });
 			}
 			
 	   })
 	   
 		/*var marker1 = new google.maps.Marker({
		      position: myLatlng,
		      map: map,
		      title: 'Hello World!'
		  });*/

/* 		 var flightPlanCoordinates = [
   		 	new google.maps.LatLng(37.772323, -122.214897),
    		new google.maps.LatLng(21.291982, -157.821856),
    		new google.maps.LatLng(-18.142599, 178.431),
    		new google.maps.LatLng(-27.46758, 153.027892)
  		];
 		 var flightPath = new google.maps.Polyline({
    		path: flightPlanCoordinates,
    		geodesic: true,
    		strokeColor: '#FF0000',
    		strokeOpacity: 1.0,
    		strokeWeight: 2
  		});*/

 		//map.panTo(myLatLng);
  		//flightPath.setMap(map);
		console.log('ending scipt');
                               
 
}


function Polyline(){
	view = 1;
	
	
	
	
	
}

function BatteryGraph() {
	
}

function Cells() {
	view = 2;
}

function fullscreenMode(){
	if(view == 0) {
		// open full screen for markers
		window.open('fullscreenMap', '_blank');
	}
	else if(view == 1) {
		// open full screen for polyline
	}
	
	else if(view == 2){
		// open full screen for cells
	}
}


