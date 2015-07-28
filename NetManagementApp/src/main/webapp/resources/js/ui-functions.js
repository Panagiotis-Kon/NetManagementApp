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
   	
	/*In this function we return the available users */
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
        	
        	/*$("#popupText").text("Are you sure you want to monitor " + e.target.innerHTML.toLowerCase()+ " ?");
        	$("#divpopup").dialog({
				title: "User Selected",
				width: 430,
				height: 200,
				modal:true,
				
				buttons: {
					YES: 
						function(){
						$(this).dialog('close');
						
						},
					NO:
						function(){
						$(this).dialog('close');
						
						}
				}
				});*/
          //console.log("You clicked row " + e.target.innerHTML.toLowerCase());
        });
   
    	var addUserBtn = $('<button id="addUserBtn" type="submit" class="btn btn-default"></button>').text('Show TimeLine');
        $('#userSel').append(addUserBtn);

        var submitUser = document.getElementById("addUserBtn");
        submitUser.addEventListener('click',function(e){
        	 if(document.getElementById('User').value == '') {
        		 alert("Please fill user field");
             }
        	 else {
        		 userID = document.getElementById('User').value;
        		 getAvUserDates(userID);
        		 
        	 }
        	
        })
        
        
           
}

function createRange() {
	var tempArr = [];
	while (minDate <= maxDate)
	{
		tempArr.push(new Date(minDate));
		minDate.setDate(minDate.getDate()+1);
    }
	if(dateRange.length > tempArr.length) {
		dateRange.length = 0;
	}
	for(var i=0; i<tempArr.length; i++){
		
		var m = tempArr[i].getMonth();
		var d = tempArr[i].getDate();
		var y = tempArr[i].getFullYear();
		var finalDate = y + '-'+ (m+1) + '-' + d;
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


function datePicker(data) {
	//alert('datePicker');
	var strTime = '';
	var splitter = '';
	var splitter1 = '';
	var splitter2 = '';
	
	strTime = data;
	var splitter = strTime.split('#');
	var splitter1 = splitter[0].split(' ');
	var splitter2 = splitter[1].split(' ');
	minDate = new Date(splitter1[0]);
	maxDate = new Date(splitter2[0]);
	
	console.log('minDate(string)',splitter1[0]);
	console.log('maxDate(string)',splitter2[0]);
	createRange();
	
	
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
 		 if(analysisPage == 1) {
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
			link.href="javascript:getUsers();"
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
			link.href="javascript:getUsers();"
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
			link.href="javascript:BatteryGraph();"
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
			link.href="javascript:Cells();"
				link.style.color="#000";
				link.onmouseover= function(){this.style.color="red";}
				link.onmouseout = function(){this.style.color="#000";}
				
		}
	}
	
}


function addParameters() {
	
	dataArray.push(document.getElementById('fromPOI').value);
	dataArray.push(document.getElementById('toPOI').value);
	dataArray.push(document.getElementById('DmaxPOI').value);
	dataArray.push(document.getElementById('TminPOI').value);
	dataArray.push(document.getElementById('TmaxPOI').value);
	dataArray.push(document.getElementById('epsPOI').value);
	dataArray.push(document.getElementById('minPtsPOI').value);
	return true;
	//CalculatePOI(dataArray);
	
}

function POIParameters() {
	
	 //$("#popupText").text("Stay Points created. Load on Map?");
	$( "#fromPOI" ).datepicker({
	      changeMonth: true,
	      numberOfMonths: 1,
	      dateFormat: "yy-mm-dd",
	      changeYear: true,
	      //beforeShowDay: enableSpecificDates,
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
	     // beforeShowDay:enableSpecificDates,
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





