/**
 * 
 * This script is responsible for the communication with the server
 * It makes use of jQuery ajax calls 
 * Every function checks if the datasets are imported
 */

var analysisPage = 0; /* Indicates if we are in data analysis page */

/**
 * 
 * Ask's to import all the csv datasets
 * 
 */

function importCSV() {
	var resp = '';

	$.ajax({

				type : "GET",
				dataType : "text",
				url : "/NetManagementApp/importCSVs",

				success : function(data) {

					resp = data;

					if (resp == 'All-import') {

						$("#popupText")
								.html(
										"All dataSets imported correctly </br><b>Estimation Point</b> calculated");
						$("#divpopup").dialog({
							title : "DATASET IMPORT",
							width : 430,
							height : 200,
							modal : true,
							buttons : {
								OK : function() {
									$(this).dialog('close');

								}
							}
						});
					} else if (resp == "all-already-imported") {
						$("#popupText").text("All dataSets already imported");
						$("#divpopup").dialog({
							title : "DATASET IMPORT",
							width : 430,
							height : 200,
							modal : true,
							buttons : {
								OK : function() {
									$(this).dialog('close');
									// callback();
								}
							}
						});
					} else {
						$("#popupText")
								.html(
										"A Problem Occurred in Datasets.</br><b>Please Check the Server</b>");
						$("#divpopup").dialog({
							title : "DATASET IMPORT",
							width : 430,
							height : 200,
							modal : true,
							buttons : {
								OK : function() {
									$(this).dialog('close');
								}
							}
						});
					}

				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					console.log('error', textStatus + " " + errorThrown);
					alert('csv Datasets error loading response');
				}
			});

}

/*
 * Gets a list of users
 * 
 */

function getUsers(arg) {
	if (arg == 1) {
		analysisPage = 1;

	}
	console.log('arg: ', arg);
	$.ajax({
		type : "GET",
		dataType : "json",
		data : {
			arg : arg
		},
		contentType : "application/json",
		url : "/NetManagementApp/getUsers",
		success : function(data) {
			if (data == "ap-not-loaded") {
				clickableMenuVisual(1, 1);
				$("#popupText").text(
						"Sorry. First you must import Access Points Dataset");
				$("#divpopup").dialog({
					title : "IMPORT PORBLEM",
					width : 430,
					height : 200,
					modal : true,
					buttons : {
						OK : function() {
							$(this).dialog('close');

						}
					}
				});
			} else if (data == "gps-not-loaded") {
				clickableMenuAnalysis(1, 1);
				$("#popupText")
						.text("Sorry. First you must import GPS Dataset");
				$("#divpopup").dialog({
					title : "IMPORT PORBLEM",
					width : 430,
					height : 200,
					modal : true,
					buttons : {
						OK : function() {
							$(this).dialog('close');

						}
					}
				});
			} else {
				clickableMenuAnalysis(0, 1);
				clickableMenuVisual(0, 1);
				// options(data);
				SelectUser(data);
			}

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log('error', textStatus + " " + errorThrown);
			alert('Get Users error loading response');
		}
	});

}

/**
 * Given the userID, it gets the available dates
 * 
 * @param userID
 * @param arg: 0 is for access point and gps, 1 is only for gps
 */

function getAvUserDates(userID, arg) {

	console.log(userID);
	$.ajax({
		type : "GET",
		dataType : "json",
		data : {
			userID : userID,
			arg : arg
		},
		contentType : "application/json",
		url : "/NetManagementApp/getDates",
		success : function(data) {

			if (data == "ap-not-loaded and gps-not-loaded") {
				clickableMenuVisual(1, 1);
				$("#popupText").text(
						"Sorry. First you must import Access Points Dataset");
				$("#divpopup").dialog({
					title : "IMPORT PORBLEM",
					width : 430,
					height : 200,
					modal : true,
					buttons : {
						OK : function() {
							$(this).dialog('close');

						}
					}
				});
			} else if (data == "gps-not-loaded") {
				clickableMenuAnalysis(1, 1);
				$("#popupText")
						.text("Sorry. First you must import GPS Dataset");
				$("#divpopup").dialog({
					title : "IMPORT PORBLEM",
					width : 430,
					height : 200,
					modal : true,
					buttons : {
						OK : function() {
							$(this).dialog('close');

						}
					}
				});
			} else {
				Pickerdate(data);
			}

		},

		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log('error', textStatus + " " + errorThrown);
			alert('Get Users Dates error loading response');
		}
	});

	

}

/**
 * 
 * Gets access point information in order to show theis positions on the map
 * 
 */

function getApInfo() {

	$.ajax({
				type : "GET",
				dataType : "json",
				data : {
					userID : userID,
					startDate : startDate,
					endDate : endDate
				},
				contentType : "application/json",
				url : "/NetManagementApp/getApInfo",
				success : function(data) {
					// console.log('success',data);
					if (data == "ap-not-loaded") {
						$("#popupText")
								.text(
										"Sorry. First you must import Access Points Dataset");
						$("#divpopup").dialog({
							title : "IMPORT PORBLEM",
							width : 430,
							height : 200,
							modal : true,
							buttons : {
								OK : function() {
									$(this).dialog('close');
									clickableMenuAnalysis(1, 1);
								}
							}
						});
					} else {
						$("#popupText")
								.text(
										"Access point gathering comleted. Load Markers on Map ?");
						$("#divpopup").dialog({
							title : "ACCESS POINTS",
							width : 430,
							height : 200,
							modal : true,
							buttons : {
								YES : function() {
									$(this).dialog('close');
									$("#map-fullscreen").show();
									Markers(data);
									getBatteryInfo();
								},
								NO : function() {
									$(this).dialog('close');

								}
							}
						});
					}

				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					console.log('error', textStatus + " " + errorThrown);
					alert('Get App Info error loading response');
				}
			});

}

/**
 * 
 * Get Battery information in order to create a diagram
 * 
 */

function getBatteryInfo() {

	console.log("getBatteryInfo Ajax");
	$.ajax({
		type : "GET",
		dataType : "json",
		data : {
			userID : userID,
			startDate : startDate,
			endDate : endDate
		},
		contentType : "application/json",
		url : "/NetManagementApp/BatteryInfo",
		success : function(data) {
			// console.log('success',data);
			if (data == "bat-not-loaded") {
				$("#popupText").text(
						"Sorry. First you must import Battery Dataset");
				$("#divpopup").dialog({
					title : "IMPORT PORBLEM",
					width : 430,
					height : 200,
					modal : true,
					buttons : {
						OK : function() {
							$(this).dialog('close');
							clickableMenuAnalysis(1, 1);
						}
					}
				});

			} else {
				BatteryGraph(data);

			}

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log('error', textStatus + " " + errorThrown);
			alert('Get Battery Info error loading response');
		}
	});

}

/**
 * 
 * Get's base stations information in order to show their position on the map
 * 
 */

function getCellsInfo() {

	$.ajax({
		type : "GET",
		dataType : "json",
		data : {
			userID : userID,
			startDate : startDate,
			endDate : endDate
		},
		contentType : "application/json",
		url : "/NetManagementApp/CellsInfo",
		success : function(data) {
			if (data == "No info") {
				$("#popupText").text(
						"No data for this period. Try other period");
				$("#divpopup").dialog({
					title : "CELLS",
					width : 430,
					height : 200,
					modal : true,
					buttons : {
						OK : function() {
							$(this).dialog('close');

						}
					}
				});

			} else if (data == "bs-not-loaded") {
				$("#popupText").text("Base stations dataset is not imported");
				$("#divpopup").dialog({
					title : "CELLS",
					width : 430,
					height : 200,
					modal : true,
					buttons : {
						OK : function() {
							$(this).dialog('close');

						}
					}
				});
			} else {
				// console.log('success in getting cell info');
				$("#popupText").text(
						"Base Stations Info gathering comleted. Load Graph ?");
				$("#divpopup").dialog({
					title : "CELLS",
					width : 430,
					height : 200,
					modal : true,
					buttons : {
						YES : function() {
							$(this).dialog('close');
							DrawCells(data);

						},
						NO : function() {
							$(this).dialog('close');

						}
					}
				});

			}

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log('error', textStatus + " " + errorThrown);
			alert('Cells error loading response');
		}
	});
	

}

/**
 * 
 * Get's the stay points for the parameters given
 * 
 */

function StayPoints() {

	$.ajax({
		type : "GET",
		dataType : "json",
		data : {
			userID : userID,
			startDate : startDate,
			endDate : endDate,
			Dmax : Dmax,
			Tmin : Tmin,
			Tmax : Tmax
		},
		contentType : "application/json",
		url : "/NetManagementApp/Stay-Points",
		success : function(data) {
			// console.log('success',data);
			if (data == "gps-not-loaded") {
				$("#popupText").text(
						"GPS dataset is not imported. Please import dataset");
				$("#divpopup").dialog({
					title : "GPS",
					width : 430,
					height : 200,
					modal : true,
					buttons : {
						OK : function() {
							$(this).dialog('close');

						}
					}
				});
			} else {
				$("#popupText").text("Stay Points created. Load on Map?");
				$("#divpopup").dialog({
					title : "STAY POINTS",
					width : 430,
					height : 200,
					modal : true,
					buttons : {
						YES : function() {
							$(this).dialog('close');

							DrawStayPoints(data);

						},
						NO : function() {
							$(this).dialog('close');
							// sessionStorage.setItem('stay-points',JSON.stringify(data));
						}
					}
				});
			}

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log('error', textStatus + " " + errorThrown);
			alert('Get Stay Points error loading response');
		}
	});
	// clickableMenuAnalysis(1, 2);

}


/**
 * 
 * Calculates POI given an array of parameters
 * @param dataArray
 */

function CalculatePOI(dataArray) {

	
	$.ajax({
		type : "GET",
		dataType : "json",
		data : {
			startDate : dataArray[0],
			endDate : dataArray[1],
			Dmax : dataArray[2],
			Tmin : dataArray[3],
			Tmax : dataArray[4],
			eps : dataArray[5],
			minPts : dataArray[6],
			option : dataArray[7]
		},
		contentType : "application/json",
		url : "/NetManagementApp/POI",
		success : function(data) {
			if (data == "poi-problem") {

				$("#popupText").text("POI problem detected. Check the server");
				$("#divpopup").dialog({
					title : "POI'S",
					width : 430,
					height : 200,
					modal : true,
					buttons : {
						OK : function() {
							$(this).dialog('close');

						}

					}
				});

			} else if (data == "gps-not-loaded") {
				$("#popupText").text(
						"GPS dataset is not imported. Please import dataset");
				$("#divpopup").dialog({
					title : "GPS",
					width : 430,
					height : 200,
					modal : true,
					buttons : {
						OK : function() {
							$(this).dialog('close');

						}
					}
				});
			} else {
				$("#popupText")
						.text("POI's have been calculated. Load on Map?");
				$("#divpopup").dialog({
					title : "POI'S",
					width : 430,
					height : 200,
					modal : true,
					buttons : {
						YES : function() {
							$(this).dialog('close');
							// draw the pois

							DrawPOI(data);

						},
						NO : function() {
							$(this).dialog('close');

						}
					}
				});

			}
		},

		error : function(XMLHttpRequest, textStatus, errorThrown) {
			console.log('error', textStatus + " " + errorThrown);
			alert('POI error loading response');
		}
	});

}

/*----------------------------------------- Bar Diagrams ---------------------------------------*/

/**
 * 
 * Ask's for the amount of user's that have battery level <= 15% 
 * Then calls a function to draw the result in a diagram
 * 
 */

function graphBatUsers() {
	$
			.ajax({
				type : "GET",
				dataType : "json",
				contentType : "application/json",
				url : "/NetManagementApp/Low-Battery-Graph",
				success : function(data) {
					if (data == "bar-bat-problem") {

						$("#popupText")
								.text(
										"Bar Diagram 1 problem detected. Check the server");
						$("#divpopup").dialog({
							title : "Bar Diagram 1",
							width : 430,
							height : 200,
							modal : true,
							buttons : {
								OK : function() {
									$(this).dialog('close');

								}

							}
						});

					} else if (data == "bat-not-loaded") {
						$("#popupText").text("Battery dataSet is not loaded!");
						$("#divpopup").dialog({
							title : "DATASET IMPORT",
							width : 430,
							height : 200,
							modal : true,
							buttons : {
								OK : function() {
									$(this).dialog('close');

								}
							}
						});
					} else {
						$("#popupText")
								.html(
										"Bar Diagram 1 has been generated.<br/> Load Diagram?");
						$("#divpopup").dialog({
							title : "Bar Diagram 1",
							width : 430,
							height : 200,
							modal : true,
							buttons : {
								YES : function() {
									$(this).dialog('close');
									DrawDiagram1(data);

								},
								NO : function() {
									$(this).dialog('close');

								}
							}
						});

					}
				},

				error : function(XMLHttpRequest, textStatus, errorThrown) {
					console.log('error', textStatus + " " + errorThrown);
					alert('Bar Diagram 1 error loading response');
				}
			});

}

/**
 * 
 * Ask's for the operators 
 * Then calls a function to draw the result in a diagram
 * 
 */

function graphOpUsers() {

	$
			.ajax({
				type : "GET",
				dataType : "json",
				contentType : "application/json",
				url : "/NetManagementApp/Operators-Users-Graph",
				success : function(data) {
					if (data == "bar-users-problem") {

						$("#popupText")
								.html(
										"Bar Diagram 2 problem detected.<br/> Check the server");
						$("#divpopup").dialog({
							title : "Bar Diagram 2",
							width : 430,
							height : 200,
							modal : true,
							buttons : {
								OK : function() {
									$(this).dialog('close');
									DrawDiagram2(data);
								}

							}
						});

					} else if (data == "bs-not-loaded") {
						$("#popupText").text(
								"Base Stations dataSet is not loaded!");
						$("#divpopup").dialog({
							title : "DATASET IMPORT",
							width : 430,
							height : 200,
							modal : true,
							buttons : {
								OK : function() {
									$(this).dialog('close');

								}
							}
						});
					} else {
						$("#popupText")
								.html(
										"Bar Diagram 2 has been generated.<br/> Load Diagram?");
						$("#divpopup").dialog({
							title : "Bar Diagram 2",
							width : 430,
							height : 200,
							modal : true,
							buttons : {
								YES : function() {
									$(this).dialog('close');
									DrawDiagram2(data);

								},
								NO : function() {
									$(this).dialog('close');

								}
							}
						});

					}
				},

				error : function(XMLHttpRequest, textStatus, errorThrown) {
					console.log('error', textStatus + " " + errorThrown);
					alert('Bar Diagram 2 error loading response');
				}
			});

}

/**
 * 
 * Ask's for the economic route given the two parameters
 * Then calls a function to draw the result on the map
 * 
 */

function EconomicRoute(time_slack, radius) {

	$
			.ajax({
				type : "GET",
				dataType : "json",
				data : {
					userID : userID,
					startDate : startDate,
					endDate : endDate,
					time_slack : time_slack,
					radius : radius
				},
				contentType : "application/json",
				url : "/NetManagementApp/Battery-Economic-Route",
				success : function(data) {
					if (data == "Economic-route-problem") {

						$("#popupText")
								.html(
										"Economic-route problem Detected.<br/> Check the server");
						$("#divpopup").dialog({
							title : "Economic Route",
							width : 430,
							height : 200,
							modal : true,
							buttons : {
								OK : function() {
									$(this).dialog('close');

								}

							}
						});

					} else {
						$("#popupText").html(
								"Economic-route calculated.Load on Map?");
						$("#divpopup").dialog({
							title : "Economic Route",
							width : 430,
							height : 200,
							modal : true,
							buttons : {
								OK : function() {
									$(this).dialog('close');
									BatEcoRoute(data);
								}

							}
						});
					}
				},

				error : function(XMLHttpRequest, textStatus, errorThrown) {
					console.log('error', textStatus + " " + errorThrown);
					alert('Economic Route error loading response');
				}
			});

}
