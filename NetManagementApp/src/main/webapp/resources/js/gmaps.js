/**
 * 
 * This script is responsible for the google maps
 * 
 */

/**************************************** VARIABLES *********************************/

map = null;

var flightPath = null;
var mapCreated = 0; // shows if map is already created
var markersAP = [];
var markersCells = [];
var markersEco = [];
var markersPoly = [];

/*************************************** FUNCTIONS *********************************/

$( document ).ready(function() {
	CreateMap();
});


function CreateMap() {
	var latlng = new google.maps.LatLng('37.96863889357752', '23.76679449388328');
	var mapOptions = {
			zoom : 8,
			center : latlng,
			mapTypeId : google.maps.MapTypeId.TERRAIN
		};

		map = new google.maps.Map(document.getElementById('map-canvas'),
				mapOptions);
		mapCreated = 1;
}


function Markers(data) {

	
	var latlng = null;
	//sessionStorage.setItem('apdata', JSON.stringify(data));
	if (mapCreated == 1) {
		if (flightPath != null) {

			flightPath.setMap(null);
			if (markersEco.length != 0) {
				for (var i = 0; i < markersEco.length; i++) {
					markersEco[i].setMap(null);
				}
			}
		}
		if (markersAP.length != 0) {
			deleteMarkers(0);
		}
		//if(MarkersCells.length !=0 ) {
		//deleteMarkers(1);
		//}
		if($('#EcoRoute').length){
			$("#EcoRoute").remove();
		}
	}

	$.each(data, function(i, item) {
		latlng = new google.maps.LatLng(item.APlatitude, item.APlongtitude);
		if (mapCreated == 1) {

			var marker = new google.maps.Marker({
				position : latlng,
				map : map,
				title : item.ssid,
				
			});
			markersAP.push(marker);

			var content = "<p>" + 'SSID: ' + item.ssid + "<br />" + 'BSSID: '
					+ item.bssid + "<br />" + ' Mean RSSI: ' + item.rssi
					+ '<br />' + ' frequency: ' + item.frequency + "<br />"
					+ "latitude:  " + item.APlatitude + "<br />"
					+ "longtitude:  " + item.APlongtitude + "<br />"
					+ 'Timestamp: ' + item.timestamp + "</p>";

			var infowindow = new google.maps.InfoWindow();

			google.maps.event.addListener(marker, 'click', (function(marker,
					content, infowindow) {
				return function() {
					infowindow.setContent(content);
					infowindow.open(map, marker);
				};
			})(marker, content, infowindow));
		} else {
			CreateMap();
		}

	});
	

}

//Sets the map on all markers in the array.
function setAllMap(map, option) {
	if (option == 0) {
		if (markersAP.length != 0) {
			for (var i = 0; i < markersAP.length; i++) {
				markersAP[i].setMap(map);
			}
		}
	} else if (option == 1) {
		if (markersCells.length != 0) {

			for (var i = 0; i < markersCells.length; i++) {
				markersCells[i].setMap(map);
			}
		}
	} else {
		if (markersAP.length != 0) {
			for (var i = 0; i < markersAP.length; i++) {
				markersAP[i].setMap(map);
			}
		}
		if (markersCells.length != 0) {

			for (var i = 0; i < markersCells.length; i++) {
				markersCells[i].setMap(map);
			}
		}

	}

}

function clearMarkers(option) {
	setAllMap(null, option);
}
//Deletes all markers in the array by removing references to them.
function deleteMarkers(option) {
	if (option == 0) {
		clearMarkers(0);
	} else if (option == 1) {
		clearMarkers(1);
	} else {
		clearMarkers(2);

	}
	markersAP = [];
	markersCells = [];

}

/**
 *  CenterControl is responsivle for the button in gmap for the Battery Economic Route
 */

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

	google.maps.event
			.addDomListener(
					controlUI,
					'click',
					function() {

						$("#divpopupParam")
								.dialog(
										{
											title : "Economic Route Parameters",
											width : 450,
											height : 480,
											modal : true,
											buttons : {
												Submit : function() {

													if (document
															.getElementById('timeSlack').value == ''
															|| document
																	.getElementById('radius').value == '') {

														alert("Please fill the values or set it to Default")
													} else {

														var time_slack = parseFloat(document
																.getElementById('timeSlack').value);
														var radius = parseInt(document
																.getElementById('radius').value);
														console
																.log("time_slack: "
																		+ time_slack
																		+ " radius: "
																		+ radius);
														if (time_slack > 0
																|| radius > 0) {
															EconomicRoute(
																	time_slack,
																	radius);
														} else {
															alert("Negative values are not accepted")
														}

													}
													$(this).dialog('close');
													document
															.getElementById('timeSlack').value = '';
													document
															.getElementById('radius').value = '';

												}

												,
												Default : function() {
													/* Setting the default settings */
													EconomicRoute(-1, -1);
													$(this).dialog('close');

												}
											}
										});
					})

}

/* 
 * Polyline creates the a line between the markers
 * if there are no markers in the map an alert is showed
 * 
 * */

function Polyline(data) {

	
	if(mapCreated != 1) {
		CreateMap();
		
	}
	else {
		if (flightPath != null) {

			flightPath.setMap(null);
			if (markersEco.length != 0) {
				for (var i = 0; i < markersEco.length; i++) {
					markersEco[i].setMap(null);
				}
			}
		}
		
		var flightPlanCoordinates = [];
		
		$.each(data, function(i, item) {
			
			if(i==0 || i==data.length-1) {
				if(i == 0) {
					var pinImage = new google.maps.MarkerImage(
							"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld="
									+ "S" + "|0066FF|0066FF", new google.maps.Size(80,
									130), new google.maps.Point(0, 0),
							new google.maps.Point(10, 34));
					
				}
				if( i == data.length-1) {
					var pinImage = new google.maps.MarkerImage(
							"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld="
									+ "E" + "|33CC33|33CC33", new google.maps.Size(80,
									130), new google.maps.Point(0, 0),
							new google.maps.Point(10, 34));
				}
				
				var marker = new google.maps.Marker({
					position : new google.maps.LatLng(item.Ulatitude, item.Ulongtitude),
					map : map,
					icon : pinImage,
					
				});
				markersPoly.push(marker);
				var content = "<p>" + 'Number: ' + (i + 1) + "<br />" + "latitude:  " + item.Ulatitude
				+ "<br />" + "longtitude:  " + item.Ulongtitude + "<br />"
				+ 'Timestamp: ' + item.timestamp + "</p>";

				var infowindow = new google.maps.InfoWindow();

				google.maps.event.addListener(marker, 'click', (function(marker,
						content, infowindow) {
					return function() {
						infowindow.setContent(content);
						infowindow.open(map, marker);
					};
				})(marker, content, infowindow));
				
			}
			
			flightPlanCoordinates.push(new google.maps.LatLng(item.Ulatitude,
					item.Ulongtitude));

		});
		
		var centerControlDiv = document.createElement('div');
		centerControlDiv.setAttribute("id", "EcoRoute");
		if (!$('#EcoRoute').length) {
			var centerControl = new CenterControl(centerControlDiv, map);
		}

		centerControlDiv.index = 1;
		map.controls[google.maps.ControlPosition.TOP_CENTER]
				.push(centerControlDiv);

		flightPath = new google.maps.Polyline({
			path : flightPlanCoordinates,
			geodesic : true,
			strokeColor : '#FF3300',
			strokeOpacity : 1.0,
			strokeWeight : 2
		});
		flightPath.setMap(map);
		
		
	}
	
	

}

/**
 * BatEcoRoute is responsible for making the economic route across the wifi markers
 * 
 */

function BatEcoRoute(data) {
	flightPath.setMap(null);
	deleteMarkers(2);
	if (mapCreated == 1) {
		if (flightPath != null) {

			flightPath.setMap(null);
			if (markersEco.length != 0) {
				for (var i = 0; i < markersEco.length; i++) {
					markersEco[i].setMap(null);
				}
			}
			if(markersPoly.length != 0) {
				for (var i = 0; i < markersPoly.length; i++) {
					markersPoly[i].setMap(null);
				}
			}
		}
	}
	var latlng = null;
	
	var gpsRoute = data[0];
	var ecoMarkers = data[1];
	

	var flightPlanCoordinates = [];
	$.each(gpsRoute, function(i, item) {

		flightPlanCoordinates.push(new google.maps.LatLng(item.Ulatitude,
				item.Ulongtitude));

	});

	flightPath = new google.maps.Polyline({
		path : flightPlanCoordinates,
		geodesic : true,
		strokeColor : '#33CC33',
		strokeOpacity : 1.0,
		strokeWeight : 2
	});
	flightPath.setMap(map);

	$.each(ecoMarkers, function(i, item) {

		var pinImage = new google.maps.MarkerImage(
				"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld="
						+ (i + 1) + "|FF0000|000FFF", new google.maps.Size(21,
						40), new google.maps.Point(0, 0),
				new google.maps.Point(10, 34));
		latlng = new google.maps.LatLng(item.APlatitude, item.APlongtitude);

		var marker = new google.maps.Marker({
			position : latlng,
			map : map,
			icon : pinImage,
			title : item.ssid,
			animation : google.maps.Animation.DROP,
		});
		markersEco.push(marker);
		var content = "<p>" + 'Number: ' + (i + 1) + "<br />" + 'SSID: '
				+ item.ssid + "<br />" + 'BSSID: ' + item.bssid + "<br />"
				+ ' RSSI: ' + item.rssi + '<br />' + ' frequency: '
				+ item.frequency + "<br />" + "latitude:  " + item.APlatitude
				+ "<br />" + "longtitude:  " + item.APlongtitude + "<br />"
				+ 'Timestamp: ' + item.timestamp + "</p>";

		var infowindow = new google.maps.InfoWindow();

		google.maps.event.addListener(marker, 'click', (function(marker,
				content, infowindow) {
			return function() {
				infowindow.setContent(content);
				infowindow.open(map, marker);
			};
		})(marker, content, infowindow));
	});



}

/**
 * DrawCells creates the base station markers
 * if there is already the battery economic route it delete's it
 * 
 * 
 * */

function DrawCells(bsdata) {
	
	if (mapCreated == 1) {
		
		
			if (markersEco.length != 0) {
				for (var i = 0; i < markersEco.length; i++) {
					markersEco[i].setMap(null);
				}
			}
	
	}

	var pinImage = new google.maps.MarkerImage("resources/images/bs.png",
			new google.maps.Size(35, 40), new google.maps.Point(0, 0), null);

	$.each(bsdata,
			function(i, item) {
				var latlng = new google.maps.LatLng(item.BSlatitude,
						item.BSlongtitude);

				if (mapCreated == 1) {

					var marker = new google.maps.Marker({
						position : latlng,
						map : map,
						icon : pinImage,
						title : item.Operator,
						animation : google.maps.Animation.DROP,
					});
					markersCells.push(marker);

					var content = "<p>" + "Operator: " + item.Operator
							+ "<br />" + " MCC: " + item.mcc + "<br />"
							+ " MNC: " + item.mnc + "<br />" + " cid: "
							+ item.cid + "<br />" + " LAC: " + item.lac
							+ "</p>";

					var infowindow = new google.maps.InfoWindow();

					google.maps.event.addListener(marker, 'click', (function(
							marker, content, infowindow) {
						return function() {
							infowindow.setContent(content);
							infowindow.open(map, marker);
						};
					})(marker, content, infowindow));

				} else {
					CreateMap();
				}

			});
	

}

/**
 * 
 * Creates the stay points in the map
 * 
 */

function DrawStayPoints(data) {
	
	
	if (data == null) {
		alert('Data in Stay Points is null')
	} else {

		if(mapCreated == 1) {
			var pinImage = new google.maps.MarkerImage("resources/images/sp1.png",
					new google.maps.Size(35, 40), new google.maps.Point(0, 0), null);
			$.each(data, function(i, item) {

				var latlng = new google.maps.LatLng(item.lat, item.lon);
				var marker = new google.maps.Marker({
					position : latlng,
					map : map,
					icon : pinImage,
					title : 'Stay Point',
					animation : google.maps.Animation.DROP,
				});

				var content = "<p>" + "Lat: " + item.lat + "<br />" + " Lon: "
						+ item.lon + "<br />" + " Tstart: " + item.Tstart
						+ "<br />" + " Tend: " + item.Tend + "</p>";

				var infowindow = new google.maps.InfoWindow();

				google.maps.event.addListener(marker, 'click', (function(marker,
						content, infowindow) {
					return function() {
						infowindow.setContent(content);
						infowindow.open(map, marker);
					};
				})(marker, content, infowindow));

			});
			
		} else {
			CreateMap();
		}
		

	}
	

}




/**
 * 
 * Creates the POI inside a rectangle
 * 
 */
function DrawPOI(data) {

	
	var mapPOI = null;
	if (data == null) {
		alert('Null data in POI');
	} else {
		if(mapCreated == 1) {
			
			
		}
		var cluster = 0;
		$.each(data, function(i, item) {

			cluster++;
			if (i == 0) {
				var latlng = new google.maps.LatLng(item.startlat,
						item.startlon);

				var mapOptions = {
					zoom : 8,
					center : latlng,
					mapTypeId : google.maps.MapTypeId.TERRAIN
				};

				mapPOI = new google.maps.Map(document
						.getElementById('map-canvas2'), mapOptions);

			}
			console.log('startlat: ', item.startlat);
			console.log('endlat: ', item.endlat);
			console.log('startlon: ', item.startlon);
			console.log('endlon: ', item.endlon);

			var rectangle = new google.maps.Rectangle({
				strokeColor : '#FF0000',
				strokeOpacity : 0.8,
				strokeWeight : 2,
				fillColor : '#FF0000',
				fillOpacity : 0.35,
				map : mapPOI,
				bounds : new google.maps.LatLngBounds(new google.maps.LatLng(
						item.startlat, item.startlon), new google.maps.LatLng(
						item.endlat, item.endlon))
			});

			var ne = rectangle.getBounds().getNorthEast();
			var content = "<p>" + "Cluster: " + cluster + "</p>";

			var infowindow = new google.maps.InfoWindow();

			google.maps.event.addListener(rectangle, 'click', (function(
					rectangle, content, infowindow) {
				return function() {
					infowindow.setContent(content);
					infowindow.setPosition(ne);
					infowindow.open(map, rectangle);
				};
			})(rectangle, content, infowindow));

		});
	}

	//console.log('ending Draw Points')

}
