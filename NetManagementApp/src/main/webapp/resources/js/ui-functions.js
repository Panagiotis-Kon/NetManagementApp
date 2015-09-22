/**
 * This script is responsible for all the user interface except the google maps
 * 
 */

/*********************************** Variables ******************************************/
var userID = '';
var startDate = '';
var endDate = '';
var Dmax = '';
var Tmin = '';
var Tmax = '';
var minDate = null;
var maxDate = null;
var matcherA = /[^a-zA-Z]/g;
var matcherN = /[^0-9]/g;
var submittedChoice = 0;
var dateRange = [];
var firstTimeClicked = 0;
var chart;
var chartData = [];
var chartExists = 0;

/*********************************** UI-FUNCTIONS ******************************************/

/* Sort Alphanumeric */
function sortAlphaNum(a, b) {

	var tempA = a.replace(matcherA, "");
	var tempB = b.replace(matcherA, "");
	if (tempA === tempB) {
		var aN = parseInt(a.replace(matcherN, ""), 10);
		var bN = parseInt(b.replace(matcherN, ""), 10);
		return aN === bN ? 0 : aN > bN ? 1 : -1;
	} else {
		return tempA > tempB ? 1 : -1;
	}
}

/* TimeValidator validates some time parameters with regular Expression
 * if the time value is accepted it returns true
 * else false
 * */

function TimeValidator(time) {

	var regEx = /^[0-9][0-9]:[0-9][0-9]:[0-9][0-9]:[0-9][0-9]/;
	if (!regEx.test(time)) {
		console.log("problem in RegEx: " + time);

		return false;
	} else {
		/* the format is ok, but check if it's time correct */

		var cur_time = time.split(':');
		var hours = cur_time[1];
		var mins = cur_time[2];
		var secs = cur_time[3];
		console.log(cur_time[0] + " - " + hours + " - " + mins + " - " + secs);

		var h = hours.indexOf('0');
		var m = mins.indexOf('0');
		var s = secs.indexOf('0');
		if (h == 0) {
			hours = hours.replace('0', '');
		}
		if (m == 0) {
			mins = mins.replace('0', '');
		}
		if (s == 0) {
			secs = secs.replace('0', '');
		}

		if ((parseInt(mins) < 60 && parseInt(mins) >= 0)
				&& (parseInt(secs) < 60 && parseInt(secs) >= 0)
				&& (parseInt(hours) < 24 && parseInt(hours) >= 0)) {
			//console.log("Time is ok");
			return true;
		} else if ((parseInt(mins) > 60 || parseInt(mins) < 0)
				|| (parseInt(secs) > 60 || parseInt(secs) < 0)
				|| (parseInt(hours) > 24 || parseInt(hours) < 0)) {
			//console.log("Problem in min,sec etc: " + hours + " - " + mins
				//	+ " - " + secs);
			return false;
		}

	}

}

/**
 * SelectUser is a function that creates a select menu with jquery on the right hand side of the screen
 * When a user is chosen then an ajax call is made to the server to return the available dates for this user
 */

function SelectUser(data) {

	var usersMenu = document.createElement('select');
	usersMenu.setAttribute("id", "usersMenu");
	usersMenu.style.width = '500px';

	clickableMenuVisual(0, 1);
	var header = $('<h2 id="headerTagId"></h2>').text('Available Users');
	$('#headerTag').append(header);
	var UsersArray = [];
	$.each(data, function(i, item) {
		UsersArray.push(item);
	})
	UsersArray.sort(sortAlphaNum);
	var dummyoption = document.createElement('option');
	dummyoption.innerHTML = '';
	usersMenu.appendChild(dummyoption);

	$.each(UsersArray, function(i, item) {
		var option = document.createElement('option');
		option.innerHTML = item;
		usersMenu.appendChild(option);
	})

	$('#usersTable').append(usersMenu);

	$("#usersMenu").selectmenu().selectmenu("menuWidget").addClass("overflow");

	$("#userSel").show();

	var timeline = $(
			'<button id="addUserBtn" type="submit" class="btn btn-default"></button>')
			.text('Show TimeLine');

	$('#userSel').append(timeline);

	var submitUser = document.getElementById("addUserBtn");
	submitUser.addEventListener('click', function(e) {
		e.preventDefault();
		if (dateRange.length > 0) {
			dateRange.length = 0;
		}
		firstTimeClicked++;
		var time = document.getElementById('timeline');
		var submit = document.getElementById("btnIdSub");
		var close = document.getElementById("btnId");
		if (firstTimeClicked > 1 && time != null && submit != null
				&& close != null) {

			document.getElementById('from').value = '';
			document.getElementById('to').value = '';
			$("#timeline").hide();
			submit.parentNode.removeChild(submit);
			close.parentNode.removeChild(close);
		}
		if (document.getElementById('UserText').value == '') {
			alert("Please fill user field");
		} else {
			userID = document.getElementById('UserText').value;

			if (analysisPage == 1) {
				getAvUserDates(userID, 1);
			} else {
				getAvUserDates(userID, 0);
			}

		}
	})
	var first = 0;
	$("#usersMenu").selectmenu({
		change : function(event, data) {
			first++;
			if (data.item.value != userID && first > 1) {

				if (analysisPage == 1) {
					var elem = document.getElementById("Dmax");
					elem.value = '';
					elem = document.getElementById("Tmin");
					elem.value = '';
					elem = document.getElementById("Tmax");
					elem.value = '';
					$("#parameters").hide();

				}
				if ( $( "#btnId" ).length ) {
					var closeBtn = document.getElementById("btnId");
					var submitBtn = document.getElementById("btnIdSub");
					closeBtn.parentNode.removeChild(closeBtn);
					submitBtn.parentNode.removeChild(submitBtn);
					var elem = document.getElementById("from");
					elem.value = '';
					elem = document.getElementById("to");
					elem.value = '';
					$("#timeline").hide();
				}
			}
			userID = data.item.value;
			//console.log('userID: ' + userID);
			$("#UserText").val(data.item.value);

		}
	});

}

/* Creates a date range for the given data (startDate - endDate) */

function createRange(data) {

	if (dateRange.length > 0) {
		dateRange.length = 0;
	}

	for (var i = 0; i < data.length; i++) {

		
		var replacer = data[i].replace(/-/g, "/");
		var tempDate = new Date(replacer);
	
		var finalDate = tempDate.getFullYear() + '-'
				+ (tempDate.getMonth() + 1) + '-' + tempDate.getDate();
		
		dateRange.push(finalDate);

	}
	/*for (var i = 0; i < dateRange.length; i++) {
		console.log('daterange: ', dateRange[i]);
	}*/
}

/* Enables specific dates for the DatePicker */

function enableSpecificDates(date) {

	var m = date.getMonth();
	var d = date.getDate();
	var y = date.getFullYear();
	var currentDate = y + '-' + (m + 1) + '-' + d;
	for (var i = 0; i < dateRange.length; i++) {

		if ($.inArray(currentDate, dateRange) > -1) {
			//console.log('currentdate OK', currentDate);
			return [ true ];

		} else {
			//console.log('currentdate NOT OK',currentDate);
			return [ false ];
		}

	}

}

/**
 * The function PickerDate takes the choices of the user from the timeline and makes an ajax Call if everything is ok 
 * 
 */

function Pickerdate(data) {

	
	createRange(data);

	$("#timeline").show();

	$("#from").datepicker({
		changeMonth : true,
		numberOfMonths : 1,
		dateFormat : "yy-mm-dd",
		changeYear : true,
		beforeShowDay : enableSpecificDates,
		onClose : function(selectedDate) {

			$("#to").datepicker("option", "minDate", selectedDate);
			startDate = selectedDate;
			//console.log(' startDate: ' + startDate);
		}
	});
	$("#to").datepicker({
		changeMonth : true,
		numberOfMonths : 1,
		dateFormat : "yy-mm-dd",
		changeYear : true,
		beforeShowDay : enableSpecificDates,
		onClose : function(selectedDate) {

			$("#from").datepicker("option", "maxDate", selectedDate);
			endDate = selectedDate;
			//console.log(' endDate: ' + selectedDate);
		}
	});

	if (analysisPage == 1) {
		$("#parameters").show();
	}
	var submitChoiceBtn = $(
			'<button id="btnIdSub" type="submit" class="btn btn-default"></button>')
			.text('Submit Choice');
	$('#closer').append(submitChoiceBtn);
	var closeBtn = $('<button id="btnId" class="btn btn-default"></button>')
			.text('Close');
	$('#closer').append(closeBtn);

	var submit = document.getElementById("btnIdSub");
	submit
			.addEventListener(
					'click',
					function(e) {
						if (document.getElementById('UserText').value == ''
								|| document.getElementById('from').value == ''
								|| document.getElementById('to').value == '') {
							alert("Please fill all the fields");

						} else {
							//after choice enable other menu items
							/*console.log('user: '
									+ document.getElementById('UserText').value
									+ ' startDate: '
									+ document.getElementById('from').value
									+ ' endDate: '
									+ document.getElementById('to').value);*/
							userID = document.getElementById('UserText').value;
							startDate = document.getElementById('from').value;
							endDate = document.getElementById('to').value;

							if (startDate == endDate) {
								$("#popupText")
										.html(
												"Start Date and End Date are the same <br/> Please select another date range");
								$("#divpopup").dialog({
									title : "DATE PROBLEM",
									width : 430,
									height : 300,
									modal : true,
									buttons : {
										OK : function() {
											$(this).dialog('close');

										}
									}
								});

							}

							if (analysisPage == 1) {

								console.log('analysis page');
								if (document.getElementById('Dmax').value == ''
										|| document.getElementById('Tmin').value == ''
										|| document.getElementById('Tmax').value == '') {
									alert("Please fill the parameters");
								}
								Dmax = document.getElementById('Dmax').value;
								Tmin = document.getElementById('Tmin').value;
								Tmax = document.getElementById('Tmax').value;
								if (!TimeValidator(Tmin)
										|| !TimeValidator(Tmax)) {
									$("#popupText")
											.text(
													"Wrong Format of Tmin and Tmax. Please try again");

									$("#divpopup").dialog({
										title : "Wrong Format",
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
									StayPoints();
								}

							} else {
								getApInfo();

								clickableMenuVisual(0, 1);
								clickableMenuVisual(1, 2);
								clickableMenuVisual(1, 3);
								clickableMenuVisual(1, 4);
								clickableMenuVisual(1, 5);
							}

						}

					});
	
	/* Listener for the close button and cleanup */

	var closer = document.querySelector('#btnId');
	closer.addEventListener('click', function(e) {
		var usermenu = document.querySelector('#usersMenu');
		var hTag = document.querySelector('#headerTagId');
		var showDate = document.querySelector('#addUserBtn');
		$('#usersMenu').selectmenu('destroy');
		usermenu.parentNode.removeChild(usermenu);
		hTag.parentNode.removeChild(hTag);
		closer.parentNode.removeChild(closer);
		showDate.parentNode.removeChild(showDate);
		submit.parentNode.removeChild(submit);

		var elem = document.getElementById("UserText");
		elem.value = '';
		elem = document.getElementById("from");
		elem.value = '';
		elem = document.getElementById("to");
		elem.value = '';
		if (analysisPage == 1) {
			$("#parameters").hide();
		}

		$("#timeline").hide();
		$("#userSel").hide();
		clickableMenuVisual(1, 1);
		clickableMenuVisual(0, 2);
		clickableMenuVisual(0, 3);
		clickableMenuVisual(0, 4);
		clickableMenuVisual(0, 5);
		clickableMenuAnalysis(1, 1);

	});

}

/* Manager of the Analysis Menu */
function clickableMenuAnalysis(option, menuitem) {

	if (menuitem == 1) {

		if (option == 0) {
			var link = document.getElementById('userLink');
			link.href = "javascript:void(0);"
			link.style.color = "grey";
		} else {
			var link = document.getElementById('userLink');
			link.href = "javascript:getUsers(1);"
			link.style.color = "#000";
			link.onmouseover = function() {
				this.style.color = "red";
			}
			link.onmouseout = function() {
				this.style.color = "#000";
			}
		}
	}

	else if (menuitem == 2) {
		if (option == 0) {
			var link = document.getElementById('POIParamLink');
			link.href = "javascript:void(0);"
			link.style.color = "grey";
		} else {
			var link = document.getElementById('POIParamLink');
			link.href = "javascript:POIParameters();"
			link.style.color = "#000";
			link.onmouseover = function() {
				this.style.color = "red";
			}
			link.onmouseout = function() {
				this.style.color = "#000";
			}
		}
	}

}

/**
 * Manager for the mini menu in data visualization page
 * @param option
 * @param menuitem
 */

function clickableMenuVisual(option, menuitem) {
	//option 0 for disable, 1 for enable

	if (menuitem == 1) {

		if (option == 0) {
			var link = document.getElementById('userLink');
			link.href = "javascript:void(0);"
			link.style.color = "grey";
		} else {
			var link = document.getElementById('userLink');
			link.href = "javascript:getUsers(0);"
			link.style.color = "#000";
			link.onmouseover = function() {
				this.style.color = "red";
			}
			link.onmouseout = function() {
				this.style.color = "#000";
			}

		}

	} else if (menuitem == 2) {
		if (option == 0) {
			var link = document.getElementById('Markers');
			link.href = "javascript:void(0);"
			link.style.color = "grey";
		} else {
			var link = document.getElementById('Markers');
			link.href = "javascript:getApInfo();"
			link.style.color = "#000";
			link.onmouseover = function() {
				this.style.color = "red";
			}
			link.onmouseout = function() {
				this.style.color = "#000";
			}

		}
	}

	else if (menuitem == 3) {
		if (option == 0) {
			var link = document.getElementById('polyLink');
			link.href = "javascript:void(0);"
			link.style.color = "grey";
		} else {
			var link = document.getElementById('polyLink');
			link.href = "javascript:getPolyline();"
			link.style.color = "#000";
			link.onmouseover = function() {
				this.style.color = "red";
			}
			link.onmouseout = function() {
				this.style.color = "#000";
			}

		}
	} else if (menuitem == 4) {
		if (option == 0) {
			var link = document.getElementById('graphLink');
			link.href = "javascript:void(0);"
			link.style.color = "grey";
		} else {
			var link = document.getElementById('graphLink');
			link.href = "javascript:getBatteryInfo();"
			link.style.color = "#000";
			link.onmouseover = function() {
				this.style.color = "red";
			}
			link.onmouseout = function() {
				this.style.color = "#000";
			}

		}
	} else if (menuitem == 5) {
		if (option == 0) {
			var link = document.getElementById('cellsLink');
			link.href = "javascript:void(0);"
			link.style.color = "grey";
		} else {
			var link = document.getElementById('cellsLink');
			link.href = "javascript:getCellsInfo();"
			link.style.color = "#000";
			link.onmouseover = function() {
				this.style.color = "red";
			}
			link.onmouseout = function() {
				this.style.color = "#000";
			}

		}
	}

}
/*
function allowDatesPOI(date) {
	var m = date.getMonth();
	var d = date.getDate();
	var y = date.getFullYear();
	var currentDate = y + '-' + (m + 1) + '-' + d;
	for (var i = 0; i < MinMaxD.length; i++) {

		if ($.inArray(currentDate, MinMaxD) > -1) {
			
			return [ true ];

		} else {
			//console.log('currentdate NOT OK',currentDate);
			return [ false ];
		}

	}

}
*/

/*
 *	POIParameters creates the popup dialog for the user to set up the parameters for the dbscan 
 * 
 */
function POIParameters() {


	var choice = 0; // 0: DBSCAN-Authors, 1: DBSCAN-Apache
	$("#fromPOI").datepicker({
		changeMonth : true,
		numberOfMonths : 1,
		dateFormat : "yy-mm-dd",
		changeYear : true,
		onClose : function(selectedDate) {

			$("#toPOI").datepicker("option", "minDate", selectedDate);
			
		}
	});
	$("#toPOI").datepicker({
		changeMonth : true,
		numberOfMonths : 1,
		dateFormat : "yy-mm-dd",
		changeYear : true,
		onClose : function(selectedDate) {

			$("#fromPOI").datepicker("option", "maxDate", selectedDate);
			
		}
	});
	
	/* Listener for picking up the dbscan algorithm */
	$("#dbscanSelect").change(function() {
		  
		  if($( "#dbscanSelect" ).val()== 'DBSCAN-Apache' ){
			  choice = 1;
			  
		  }
		  else {
			  choice = 0;
			  
		  }
		});


	$("#divpopupParam").dialog({
		title : "POI PARAMETERS",
		width : 600,
		height : 700,
		modal : true,
		buttons : {
			Submit : function() {

				var timeTmin = document.getElementById('TminPOI').value;
				var timeTmax = document.getElementById('TmaxPOI').value;
				if (!TimeValidator(timeTmin) || !TimeValidator(timeTmax)) {
					alert("Wrong Format for Time")
					$(this).dialog('close');
				} else {
					var dataArray = [];
					dataArray.push(document.getElementById('fromPOI').value);
					dataArray.push(document.getElementById('toPOI').value);
					dataArray.push(document.getElementById('DmaxPOI').value);
					dataArray.push(document.getElementById('TminPOI').value);
					dataArray.push(document.getElementById('TmaxPOI').value);
					dataArray.push(document.getElementById('epsPOI').value);
					dataArray.push(document.getElementById('minPtsPOI').value);
					dataArray.push(choice);
					$(this).dialog('close');
					CalculatePOI(dataArray);
				}

			},

			Cancel : function() {
				$(this).dialog('close');

			}
		}
	});

}

/*------------------------------------- Battery-User Diagram -------------------------------------------- */

/**
 * getRandomColor in a rand function for making colors
 */

function getRandomColor() {
	var letters = '0123456789ABCDEF'.split('');
	var color = '#';
	for (var i = 0; i < 6; i++) {
		color += letters[Math.floor(Math.random() * 16)];
	}
	return color;
}

/**
 * 
 * BatteryGraph is a function that creates an am chart graph for the battery level of the users
 */

function BatteryGraph(data) {

	if (chartData.length != 0) {
		chart.removeChartCursor();
		chart.removeChartScrollbar();
		chart.removeListener(chart, "dataUpdated", zoomChart);
		chart.clear();
	}
	var prevDate = null;

	for (var i = 0; i < data.length; i++) {

		var replacer = (data[i].timestamp).replace(/-/g, "/");
		var Ddate = new Date(replacer);
		var color;
		if (i == 0) {
			prevDate = new Date(Ddate);
			color = getRandomColor();
		}
		if (Ddate.getDate() != prevDate.getDate()) {
			color = getRandomColor();

		}
		var DTime = Ddate.getHours() + ':' + Ddate.getMinutes() + ':'
				+ Ddate.getSeconds();
		var level = data[i].level;
		var plugged = data[i].plugged;
		chartData.push({
			date : Ddate,
			time : DTime,
			level : level,
			plugged: plugged,
			color : color,
		});
		prevDate = Ddate;
	}

	chart = new AmCharts.AmSerialChart();
	chart.dataProvider = chartData;
	chart.categoryField = "date";
	chart.dataDateFormat = "YYYY-MM-DD";
	chart.startDuration = 1;
	chart.columnWidth = 0.3;
	// AXES
	// category axis is the x axis
	var categoryAxis = chart.categoryAxis;
	categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
	categoryAxis.minPeriod = "ss"; // set second based data
	categoryAxis.dashLength = 1;
	categoryAxis.labelRotation = 90;
	categoryAxis.gridPosition = "start";
	categoryAxis.title = "Time";

	categoryAxis.position = "bottom";

	categoryAxis.axisAlpha = 0;
	categoryAxis.type = "date";

	// value axis is the y axis
	var valueAxis = new AmCharts.ValueAxis();
	valueAxis.axisAlpha = 0;
	valueAxis.dashLength = 1;
	valueAxis.title = "Level";
	chart.addValueAxis(valueAxis);
	
	// GRAPH
	var graph = new AmCharts.AmGraph();
	graph.valueField = "level";
	graph.colorField = "color";
	graph.balloonText = "Date:<b>[[date]]</b><br> Time:<b>[[time]]</b><br>Level:<b>[[level]]</b><br> Plugged:<b>[[plugged]]</b>";
	graph.type = "column";
	graph.lineAlpha = 0;
	graph.fillAlphas = 0.8;
	chart.addGraph(graph);

	chart.addListener("dataUpdated", zoomChart);

	// CURSOR
	var chartCursor = new AmCharts.ChartCursor();
	chartCursor.cursorAlpha = 0;
	chartCursor.zoomable = true;
	chartCursor.fullWidth = true;
	chartCursor.categoryBalloonDateFormat = "YYYY-MM-DD";
	chartCursor.categoryBalloonEnabled = true;
	chart.addChartCursor(chartCursor);
	chart.mouseWheelZoomEnabled = true;
	chart.creditsPosition = "top-right";

	var chartScrollbar = new AmCharts.ChartScrollbar();
	chart.addChartScrollbar(chartScrollbar);

	if (document.getElementById('chartdiv')) {
		chart.write("chartdiv");
	} else {
		var iDiv = document.createElement('div');
		iDiv.setAttribute('id', 'chartdiv');
		iDiv.style.width = "100%";
		iDiv.style.height = "500px";
		document.getElementById('BatteryChart').appendChild(iDiv);
		chart.write("chartdiv");
	}

}

function zoomChart() {
	
	chart.zoomToIndexes(chartData.length - 50, chartData.length - 1);
}

/*------------------------------------- Bar Diagrams ---------------------------------------------------- */

/**
 * DrawDiagram1 creates the graph that shows the amount of users who had battery level <= 15%
 * for all the days every hour
 */

function DrawDiagram1(data) {

	var chartBat;
	var chartDataBat = [];
	var prevDate = null;
	for (var i = 0; i < data.length; i++) {
		var splitter = data[i].split('#');
		var Ddate = new Date(splitter[0]);

		var color;
		if (i == 0) {
			prevDate = new Date(Ddate);
			color = getRandomColor();
		}
		if (Ddate.getDate() != prevDate.getDate()) {
			color = getRandomColor();

		}
		chartDataBat.push({
			time : splitter[0],
			Nusers : splitter[1],
			color : color,
		});
		prevDate = Ddate;

	}

	// SERIAL CHART
	chartBat = new AmCharts.AmSerialChart();
	chartBat.dataProvider = chartDataBat;
	chartBat.categoryField = "time";
	// the following two lines makes chart 3D
	chartBat.depth3D = 20;
	chartBat.angle = 30;

	// AXES
	// category
	var categoryAxis = chartBat.categoryAxis;
	categoryAxis.title = "Time";
	categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
	categoryAxis.minPeriod = "hh"; // our data is daily, so we set minPeriod to DD
	categoryAxis.labelRotation = 90;
	categoryAxis.dashLength = 1;
	categoryAxis.gridPosition = "start";

	// value
	var valueAxis = new AmCharts.ValueAxis();
	valueAxis.title = "Users battery <= 15%";
	valueAxis.gridAlpha = 1;
	valueAxis.dashLength = 1;
	chartBat.addValueAxis(valueAxis);

	// GRAPH
	var graph = new AmCharts.AmGraph();
	graph.valueField = "Nusers";
	graph.colorField = "color";
	graph.balloonText = "<span style='font-size:14px'>[[time]]: <b>[[value]]</b></span>";
	graph.type = "column";
	graph.lineAlpha = 0;

	graph.fillAlphas = 1;
	chartBat.addGraph(graph);

	// CURSOR
	var chartCursor = new AmCharts.ChartCursor();
	chartCursor.cursorAlpha = 0;
	chartCursor.zoomable = true;
	chartCursor.categoryBalloonEnabled = false;
	chartBat.addChartCursor(chartCursor);
	chartBat.mouseWheelZoomEnabled = true
	chartBat.creditsPosition = "top-right";

	// CURSOR
	//var chartCursor = new AmCharts.ChartCursor();
	//chart.addChartCursor(chartCursor);

	// SCROLLBAR

	var chartScrollbar = new AmCharts.ChartScrollbar();
	chartBat.addChartScrollbar(chartScrollbar);
	// WRITE
	if (document.getElementById('chartdiv')) {
		chartBat.write("chartdiv");
	} else {
		var iDiv = document.createElement('div');
		iDiv.setAttribute('id', 'chartdiv');
		iDiv.style.width = "100%";
		iDiv.style.height = "500px";
		document.getElementById('bar-diagram1').appendChild(iDiv);
		chartBat.write("chartdiv");
	}

}

/* Diagram for the Operators and the Users */

function DrawDiagram2(data) {

	var chartOP;
	var colorVal;
	var chartDataOP = [];
	for (var i = 0; i < data.length; i++) {
		var splitter = data[i].split('#');
		if (splitter[0] == "GR COSMOTE" || splitter[0] == "COSMOT"
				|| splitter[0] == "COSMOTE") {
			colorVal = "#00CC00";

		} else if (splitter[0] == "VODAFONEGR" || splitter[0] == "VODAFONE GR"
				|| splitter[0] == "vodafone GR" || splitter[0] == "VODAFONE gr") {
			colorVal = "#bf1c25";

		} else if (splitter[0] == "WIND GR " || splitter[0] == "WIND ") {
			colorVal = "#00FFFF";

		} else {
			colorVal = "#FF9933";

		}
		chartDataOP.push({
			operator : splitter[0],
			Nusers : splitter[1],
			color : colorVal,
		});

	}

	chartDataOP.sort(function(a, b) {
		return b.Nusers - a.Nusers
	})

	// SERIAL CHART
	chartOP = new AmCharts.AmSerialChart();
	chartOP.dataProvider = chartDataOP;
	chartOP.categoryField = "operator";
	// this single line makes the chart a bar chart,
	
	chartOP.rotate = true;
	// the following two lines makes chart 3D
	chartOP.depth3D = 20;
	chartOP.angle = 30;

	// AXES
	// Category
	var categoryAxis = chartOP.categoryAxis;
	categoryAxis.gridPosition = "start";
	categoryAxis.axisColor = "#DADADA";
	categoryAxis.title = "Operators";
	categoryAxis.fillAlpha = 1;
	categoryAxis.gridAlpha = 0;
	categoryAxis.fillColor = "#FAFAFA";

	// value
	var valueAxis = new AmCharts.ValueAxis();
	valueAxis.axisColor = "#DADADA";
	valueAxis.title = "Number of Users";
	valueAxis.gridAlpha = 1;
	chartOP.addValueAxis(valueAxis);

	// GRAPH
	var graph = new AmCharts.AmGraph();
	graph.title = "Users";
	graph.valueField = "Nusers";
	graph.type = "column";
	graph.balloonText = "Users in [[category]]:[[value]]";
	graph.lineAlpha = 0;
	graph.colorField = "color";
	graph.fillAlphas = 1;
	chartOP.addGraph(graph);

	chartOP.creditsPosition = "top-right";

	// WRITE
	if (document.getElementById('chartdiv')) {
		chartOP.write("chartdiv");
	} else {
		var iDiv = document.createElement('div');
		iDiv.setAttribute('id', 'chartdiv');
		iDiv.style.width = "100%";
		iDiv.style.height = "500px";
		document.getElementById('bar-diagram1').appendChild(iDiv);
		chartOP.write("chartdiv");
		
	}

}
