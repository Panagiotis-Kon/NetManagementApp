        var userID='';
        var startDate='';
        var endDate='';
        var Dmax='';
        var Tmin = '';
        var Tmax='';
        var minDate=null;
        var maxDate=null;
        var matcherA = /[^a-zA-Z]/g;
	    var matcherN = /[^0-9]/g;
        var submittedChoice = 0;
        var dateRange = [];
        var firstTimeClicked = 0;
        var MinMaxD = [];
        //var dataArray = [];
        
function sortAlphaNum(a,b) {
	
      	  var tempA = a.replace(matcherA, "");
      	  var tempB = b.replace(matcherA, "");
      	  if(tempA === tempB) 
      	  {
      		  var aN = parseInt(a.replace(matcherN, ""), 10);
      	  	  var bN = parseInt(b.replace(matcherN, ""), 10);
      	  	  return aN === bN ? 0 : aN > bN ? 1 : -1;
      	  }
      	  else 
      	  {
      	  		return tempA > tempB ? 1 : -1;
      	  }
}        
        
function options(data) {
   	
	
		  var arr = [];

		  clickableMenuVisual(0, 1);
		  var header = $('<h2 id="headerTagId"></h2>').text('Available Users');
		  $('#headerTag').append(header);
		  
          var table = $('<table id="tableID"></table>');
         
		
          $.each(data,function(i,item){ 
        	arr.push(item);  
          })
      
          arr.sort(sortAlphaNum);
		     
		     $.each(arr,function(i,item) {
		    	 
        	  row = $('<tr></tr>');
              var rowData = $('<td></td>').text(item);
              row.append(rowData);
              table.append(row);
        	  
          });
          
 		
        if ($('#tableID').length) {
             $("#usersTable tr:first").after(row);
             console.log("Table created (if)");
        }
        else {
            $('#usersTable').append(table);
            console.log("Table created (else)");
        }

		var parent = document.querySelector('#usersTable');
		
		$("#userSel").show();
        
        parent.addEventListener('click', function(e){
        	var elem = document.getElementById("User");
			elem.value = e.target.innerHTML.toLowerCase();
			userID = e.target.innerHTML.toLowerCase();
        });
   
    	var addUserBtn = $('<button id="addUserBtn" type="submit" class="btn btn-default"></button>').text('Show TimeLine');
        $('#userSel').append(addUserBtn);

        var submitUser = document.getElementById("addUserBtn");
        submitUser.addEventListener('click',function(e){
        	firstTimeClicked++;
        	var time = document.getElementById('timeline');
        	var submit = document.getElementById("btnIdSub");
        	var close = document.getElementById("btnId");
        	if(firstTimeClicked > 1 && time != null && submit != null && close!=null){
        		
        		document.getElementById('from').value = '';
        		document.getElementById('to').value = '';
        		$("#timeline").hide();
        		submit.parentNode.removeChild(submit);
        		close.parentNode.removeChild(close);
        	}
        	 if(document.getElementById('User').value == '') {
        		 alert("Please fill user field");
             }
        	 else {
        		 userID = document.getElementById('User').value;
        		 if(analysisPage == 1) {
        			 getAvUserDates(userID,1);
        	    }
        		else{
        			 getAvUserDates(userID,0);
        		 }
        		 
        	 }        	
        })
                     
}

function createRange(data) {
	
	if(dateRange.length > 0) {
		dateRange.length = 0;
	}
	
	for(var i=0; i< data.length; i++) {
		
		//console.log('data[i]',data[i]);
		var replacer = data[i].replace(/-/g , "/");
		var tempDate = new Date(replacer);
		//console.log('tempDate: ',tempDate);
		var finalDate = tempDate.getFullYear() + '-'+ (tempDate.getMonth() +1) + '-' + tempDate.getDate();
		//console.log('finalDate',finalDate);
		dateRange.push(finalDate);
		
	}
	for(var i=0; i<dateRange.length; i++){
		console.log('daterange: ', dateRange[i]);
	}
}



function enableSpecificDates(date) {

	var m = date.getMonth();
	var d = date.getDate();
	var y = date.getFullYear();
	var currentDate = y + '-'+ (m+1) + '-' + d;
	for(var i=0; i<dateRange.length; i++){
		
		if ($.inArray(currentDate, dateRange) > -1) {
			console.log('currentdate OK',currentDate);
			 return [true];
			 
		 }
		else
		{
			//console.log('currentdate NOT OK',currentDate);
			return [false];
		}
		
	}
	
}


function Pickerdate(data) {


	createRange(data);
	
	
	$("#timeline").show();
	
	$( "#from" ).datepicker({
	      changeMonth: true,
	      numberOfMonths: 1,
	      dateFormat: "yy-mm-dd",
	      changeYear: true,
	      beforeShowDay: enableSpecificDates,
	      onClose: function( selectedDate ) {
	    	 
	        $( "#to" ).datepicker( "option", "minDate", selectedDate );
	        startDate = selectedDate;
	        console.log(' startDate: ' + startDate);
	      }
	    });
	    $( "#to" ).datepicker({
	      changeMonth: true,
	      numberOfMonths: 1,
	      dateFormat: "yy-mm-dd",
	      changeYear: true,
	      beforeShowDay:enableSpecificDates,
	      onClose: function(selectedDate ) {
	    	  
	        $( "#from" ).datepicker( "option", "maxDate", selectedDate );
	        endDate = selectedDate;
	        console.log(' endDate: ' + selectedDate);
	      }
	    });
	    
	    
	    if(analysisPage == 1) {
	    	 $("#parameters").show();
	    }
	    var submitChoiceBtn = $('<button id="btnIdSub" type="submit" class="btn btn-default"></button>').text('Submit Choice');
	    $('#closer').append(submitChoiceBtn);
	    var closeBtn = $('<button id="btnId" class="btn btn-default"></button>').text('Close');
	    $('#closer').append(closeBtn);
	    
		//var tooltipDate = "[ " + splitter1[0] + " - " + splitter2[0] + " ]" ;
	   // document.getElementById('User').title=tooltipDate;	
	
	var submit = document.getElementById("btnIdSub");
    submit.addEventListener('click',function(e){
 	 if(document.getElementById('User').value == '' || document.getElementById('from').value=='' || document.getElementById('to').value=='')
     {
 		 	alert("Please fill all the fields");
 		 	
 	 }
 	 else
 	 {
 		//after choice enable other menu items
 		 console.log('user: ' + document.getElementById('User').value + ' startDate: ' + document.getElementById('from').value + ' endDate: ' + document.getElementById('to').value);
 		 userID = document.getElementById('User').value;
 		 startDate = document.getElementById('from').value;
 		 endDate = document.getElementById('to').value;
 		 
 		if(startDate == endDate) {
	    	$("#popupText").html("Start Date and End Date are the same <br/> Please select another date range");
			   $("#divpopup").dialog({
					title: "DATE PROBLEM",
					width: 430,
					height: 300,
					modal:true,
					buttons: {
						OK: 
							function(){
							$(this).dialog('close');
						
							}
					}
					}); 
			    
	    }
 		 
 		 
 		 if(analysisPage == 1) {
 			 
 			 console.log('analysis page');
 			if(document.getElementById('Dmax').value == '' || document.getElementById('Tmin').value=='' || document.getElementById('Tmax').value=='')
 			{
 				alert("Please fill the parameters");
 			}
 			Dmax = document.getElementById('Dmax').value;
 			Tmin = document.getElementById('Tmin').value;
 			Tmax = document.getElementById('Tmax').value;
 			
 			$("#popupText").text("Your choices: " + userID + " , " + startDate + " , " + endDate + "\n" +
 					"Dmax: " + Dmax + "\n" + "Tmin: " + Tmin + "\n" + "Tmax: " + Tmax);
 			
  		   $("#divpopup").dialog({
  				title: "Choice Submission",
  				width: 430,
  				height: 200,
  				modal:true,
  				buttons: {
  					OK: 
  						function(){
  						$(this).dialog('close');
  						StayPoints();
  						}
  					
  					}
  				});
  		 clickableMenuAnalysis(1, 2);
  		 //clickableMenuAnalysis(1, 3);
  		 
 		 }
 		 else {
 			$("#popupText").text("Your choices: " + userID + " , " + startDate + " , " + endDate);
 		   $("#divpopup").dialog({
 				title: "Choice Submission",
 				width: 430,
 				height: 200,
 				modal:true,
 				buttons: {
 					OK: 
 						function(){
 						$(this).dialog('close');
 						
 						/*$("#timeline").hide();
 		        		$("#btnIdSub").hide();
 		        		$("#btnId").hide();*/
 						}
 					
 					}
 				});
 		  clickableMenuVisual(0, 1);
 		  clickableMenuVisual(1, 2);
 		  clickableMenuVisual(1, 3);
 		  clickableMenuVisual(1, 4);
 		  clickableMenuVisual(1, 5);
 		 }
 		
 		 //getApInfo();
 	 }
 	
 	 
    });
   
   
    var closer = document.querySelector('#btnId');
    closer.addEventListener('click',function(e){
	   var tab = document.querySelector('#tableID');
       var hTag = document.querySelector('#headerTagId');
       var showDate =  document.querySelector('#addUserBtn');
	   tab.parentNode.removeChild(tab); 
	   hTag.parentNode.removeChild(hTag);
	   closer.parentNode.removeChild(closer);
	   showDate.parentNode.removeChild(showDate);
	   submit.parentNode.removeChild(submit);
	   
	   var elem = document.getElementById("User");
	   elem.value = '';
	   elem = document.getElementById("from");
	   elem.value = '';
	   elem = document.getElementById("to");
	   elem.value = '';
	   if(analysisPage == 1) {
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
	   clickableMenuAnalysis(0, 2);
	   clickableMenuAnalysis(0, 4);
   });
	
}



function clickableMenuAnalysis(option, menuitem) {
	
	if(menuitem == 1){
		
		if(option == 0){
			var link = document.getElementById('userLink');
			link.href="javascript:void(0);"
			link.style.color="grey";
		}
		else {
			var link = document.getElementById('userLink');
			link.href="javascript:getUsers(1);"
				link.style.color="#000";
				link.onmouseover= function(){this.style.color="red";}
				link.onmouseout = function(){this.style.color="#000";}	
		}
	}
	else if(menuitem == 2) {
		if(option == 0){
			var link = document.getElementById('StayPointsLink');
			link.href="javascript:void(0);"
			link.style.color="grey";
		}
		else {
			var link = document.getElementById('StayPointsLink');
			link.href="javascript:StayPoints();"
				link.style.color="#000";
				link.onmouseover= function(){this.style.color="red";}
				link.onmouseout = function(){this.style.color="#000";}	
		}
	}
	else if(menuitem == 3) {
		if(option == 0){
			var link = document.getElementById('POIParamLink');
			link.href="javascript:void(0);"
			link.style.color="grey";
		}
		else {
			var link = document.getElementById('POIParamLink');
			link.href="javascript:POIParameters();"
				link.style.color="#000";
				link.onmouseover= function(){this.style.color="red";}
				link.onmouseout = function(){this.style.color="#000";}	
		}
	}
	else if(menuitem == 4) {
		if(option == 0){
			var link = document.getElementById('PoiLink');
			link.href="javascript:void(0);"
			link.style.color="grey";
		}
		else {
			var link = document.getElementById('PoiLink');
			link.href="javascript:POI();"
				link.style.color="#000";
				link.onmouseover= function(){this.style.color="red";}
				link.onmouseout = function(){this.style.color="#000";}	
		}
	}
	
}


function clickableMenuVisual(option, menuitem){
	//option 0 for disable, 1 for enable
	
	if(menuitem == 1) {
		
		if(option == 0){
			var link = document.getElementById('userLink');
			link.href="javascript:void(0);"
			link.style.color="grey";
		}
		else {
			var link = document.getElementById('userLink');
			link.href="javascript:getUsers(0);"
				link.style.color="#000";
				link.onmouseover= function(){this.style.color="red";}
				link.onmouseout = function(){this.style.color="#000";}
				
		}
		
	}
	else if(menuitem == 2) {
		if(option == 0){
			var link = document.getElementById('markersLink');
			link.href="javascript:void(0);"
			link.style.color="grey";
		}
		else {
			var link = document.getElementById('markersLink');
			link.href="javascript:getApInfo();"
				link.style.color="#000";
				link.onmouseover= function(){this.style.color="red";}
				link.onmouseout = function(){this.style.color="#000";}
				
		}
	
	}
	else if(menuitem == 3) {
		if(option == 0){
			var link = document.getElementById('polyLink');
			link.href="javascript:void(0);"
			link.style.color="grey";
		}
		else {
			var link = document.getElementById('polyLink');
			link.href="javascript:Polyline();"
				link.style.color="#000";
				link.onmouseover= function(){this.style.color="red";}
				link.onmouseout = function(){this.style.color="#000";}
				
		}
	}
	else if(menuitem == 4) {
		if(option == 0){
			var link = document.getElementById('graphLink');
			link.href="javascript:void(0);"
			link.style.color="grey";
		}
		else {
			var link = document.getElementById('graphLink');
			link.href="javascript:getBatteryInfo();"
				link.style.color="#000";
				link.onmouseover= function(){this.style.color="red";}
				link.onmouseout = function(){this.style.color="#000";}
				
		}
	}
	else if(menuitem == 5) {
		if(option == 0){
			var link = document.getElementById('cellsLink');
			link.href="javascript:void(0);"
			link.style.color="grey";
		}
		else {
			var link = document.getElementById('cellsLink');
			link.href="javascript:getCellsInfo();"
				link.style.color="#000";
				link.onmouseover= function(){this.style.color="red";}
				link.onmouseout = function(){this.style.color="#000";}
				
		}
	}
	
}



function getMINMAXDates() {

	    var s = new Date(2015,2,27);
	    var e = new Date(2015,4,6);
	    

	    while(s < e) {
	    	MinMaxD.push(s);
	        s = new Date(s.setDate(
	            s.getDate() + 1
	        ))
	    }

	  
	
}

function allowDatesPOI(date) {
	var m = date.getMonth();
	var d = date.getDate();
	var y = date.getFullYear();
	var currentDate = y + '-'+ (m+1) + '-' + d;
	for(var i=0; i<MinMaxD.length; i++){
		
		if ($.inArray(currentDate, MinMaxD) > -1) {
			console.log('currentdate OK',currentDate);
			 return [true];
			 
		 }
		else
		{
			//console.log('currentdate NOT OK',currentDate);
			return [false];
		}
		
	}
	
}


function POIParameters() {
	
	 //$("#popupText").text("Stay Points created. Load on Map?");
	if(MinMaxD.length == 0){
		getMINMAXDates();
	}
	
	
	
	
	$( "#fromPOI" ).datepicker({
	      changeMonth: true,
	      numberOfMonths: 1,
	      dateFormat: "yy-mm-dd",
	      changeYear: true,
	      beforeShowDay: allowDatesPOI,
	      onClose: function( selectedDate ) {
	    	 
	        $( "#toPOI" ).datepicker( "option", "minDate", selectedDate );
	        //startDate = selectedDate;
	        //console.log(' startDate: ' + startDate);
	      }
	    });
	    $( "#toPOI" ).datepicker({
	      changeMonth: true,
	      numberOfMonths: 1,
	      dateFormat: "yy-mm-dd",
	      changeYear: true,
	      beforeShowDay:allowDatesPOI,
	      onClose: function(selectedDate ) {
	    	  
	        $( "#fromPOI" ).datepicker( "option", "maxDate", selectedDate );
	       // endDate = selectedDate;
	        //console.log(' endDate: ' + selectedDate);
	      }
	    });
	   $("#divpopupParam").dialog({
			title: "POI PARAMETERS",
			width: 500,
			height: 500,
			modal:true,
			buttons: {
				Submit: function(){
					
					
					var dataArray = [];
					dataArray.push(document.getElementById('fromPOI').value);
					dataArray.push(document.getElementById('toPOI').value);
					dataArray.push(document.getElementById('DmaxPOI').value);
					dataArray.push(document.getElementById('TminPOI').value);
					dataArray.push(document.getElementById('TmaxPOI').value);
					dataArray.push(document.getElementById('epsPOI').value);
					dataArray.push(document.getElementById('minPtsPOI').value);
					
					$(this).dialog('close');
					CalculatePOI(dataArray);
					
				},

				Cancel:
					function(){
					$(this).dialog('close');
				
					}
			}
			}); 
	   
	
}



/*------------------------------------- Bar Diagrams ---------------------------------------------------- */
function DrawDiagram1(data){
	
	var chart;
	var chartData = [];
	for(var i=0; i<data.length; i++){
		var splitter = data[i].split('#');
		chartData.push({
            time: splitter[0],
            Nusers: splitter[1],
            
        });
		
	}
	
	 
	 // SERIAL CHART
    chart = new AmCharts.AmSerialChart();
    chart.dataProvider = chartData;
    chart.categoryField = "time";
    // the following two lines makes chart 3D
    chart.depth3D = 20;
    chart.angle = 30;

    // AXES
    // category
    var categoryAxis = chart.categoryAxis;
    categoryAxis.labelRotation = 90;
    categoryAxis.dashLength = 5;
    categoryAxis.gridPosition = "start";

    // value
    var valueAxis = new AmCharts.ValueAxis();
    valueAxis.title = "Users battery <= 15%";
    valueAxis.gridAlpha = 1;
    valueAxis.dashLength = 5;
    chart.addValueAxis(valueAxis);

    // GRAPH
    var graph = new AmCharts.AmGraph();
    graph.valueField = "Nusers";
    //graph.colorField = "color";
    graph.balloonText = "<span style='font-size:14px'>[[time]]: <b>[[value]]</b></span>";
    graph.type = "column";
    graph.lineAlpha = 0;
    
    graph.fillAlphas = 1;
    chart.addGraph(graph);

    // CURSOR
    var chartCursor = new AmCharts.ChartCursor();
    chartCursor.cursorAlpha = 0;
    chartCursor.zoomable = true;
    chartCursor.categoryBalloonEnabled = false;
    chart.addChartCursor(chartCursor);

    chart.creditsPosition = "top-right";

    // CURSOR
    //var chartCursor = new AmCharts.ChartCursor();
    //chart.addChartCursor(chartCursor);

    // SCROLLBAR

    var chartScrollbar = new AmCharts.ChartScrollbar();
    chart.addChartScrollbar(chartScrollbar);
    // WRITE
    if(document.getElementById('chartdiv')) {
    	chart.write("chartdiv");
    }
    else {
    	var iDiv = document.createElement('div');
    	iDiv.setAttribute('id','chartdiv');
    	iDiv.style.width = "100%";
    	iDiv.style.height = "500px";
    	document.getElementById('bar-diagram1').appendChild(iDiv);
    	chart.write("chartdiv");
    }
    

	
}


function DrawDiagram2(data){
	
	var chart;
	var colorVal;
    var chartData = [];
	for(var i=0; i<data.length; i++){
		var splitter = data[i].split('#');
		if(splitter[0] == "GR COSMOTE" || splitter[0] == "COSMOT" || splitter[0] == "COSMOTE")
		{
			colorVal = "#00CC00";
			
		}
		else if(splitter[0] == "VODAFONEGR" || splitter[0] == "VODAFONE GR" || splitter[0] == "vodafone GR" || splitter[0] == "VODAFONE gr"){
			colorVal = "#bf1c25";
			
		}
		else if(splitter[0] == "WIND GR " || splitter[0] == "WIND "){
			colorVal ="#00FFFF";
			
		}
		else{
			colorVal = "#FF9933";
			
		}
		chartData.push({
            operator: splitter[0],
            Nusers: splitter[1],
            color: colorVal,
        });
		
	}
	
	chartData.sort(function(a, b){
		 return b.Nusers - a.Nusers})
	
         // SERIAL CHART
         chart = new AmCharts.AmSerialChart();
         chart.dataProvider = chartData;
         chart.categoryField = "operator";
         // this single line makes the chart a bar chart,
         // try to set it to false - your bars will turn to columns
         chart.rotate = true;
         // the following two lines makes chart 3D
         chart.depth3D = 20;
         chart.angle = 30;

         // AXES
         // Category
         var categoryAxis = chart.categoryAxis;
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
         chart.addValueAxis(valueAxis);

         // GRAPH
         var graph = new AmCharts.AmGraph();
         graph.title = "Users";
         graph.valueField = "Nusers";
         graph.type = "column";
         graph.balloonText = "Users in [[category]]:[[value]]";
         graph.lineAlpha = 0;
         graph.colorField = "color";
         graph.fillAlphas = 1;
         chart.addGraph(graph);

         chart.creditsPosition = "top-right";

      // WRITE
         if(document.getElementById('chartdiv')) {
         	chart.write("chartdiv");
         }
         else {
         	var iDiv = document.createElement('div');
         	iDiv.setAttribute('id','chartdiv');
         	iDiv.style.width = "100%";
         	iDiv.style.height = "500px";
         	document.getElementById('bar-diagram1').appendChild(iDiv);
         	chart.write("chartdiv");
         	//$("#chartdiv").show();
         	//console.log('hiiiii');
         }
    
	
	
}




