var map = null;
function loadMap(data) {

	
    	//location of marker
    	var myLatlng = new google.maps.LatLng(0, -180);
      	var mapOptions = {
    		zoom: 3,
    		center: myLatlng,
    		mapTypeId: google.maps.MapTypeId.TERRAIN
 		 };

 		 var map = new google.maps.Map(document.getElementById('map-canvas'),mapOptions);
 		 
 		$.each(data,function(i,item){
 			
 			var latLng = new google.maps.LatLng(item.APlatitude, item.APlongtitude);
 			var marker = new google.maps.Marker({
                position: latLng,
                map: map,
                animation: google.maps.Animation.DROP,
            });
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