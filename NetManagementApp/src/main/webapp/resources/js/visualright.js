        var userID='';
        var startDate='';
        var endDate='';
        var minDate;
        var maxDate;
        
function options(data) {
   	
	/*In this function we return the available users */
		  var arr = [];
		  clickable(1);
		  var header = $('<h2 id="headerTagId"></h2>').text('Available Users');
		  $('#headerTag').append(header);
		  
          var table = $('<table id="tableID"></table>');
         
		
         /* $.each(data,function(i,item){ 
        	arr.push(item);  
          })*/
          //arr.sort(function(a, b){return a-b});
          //for(var i=0; i<arr.length; i++){
		     
		     $.each(data,function(i,item) {
		    	 
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
        	$("#popupText").text("Are you sure you want to monitor " + e.target.innerHTML.toLowerCase()+ " ?");
        	$("#divpopup").dialog({
				title: "User Selected",
				width: 430,
				height: 200,
				modal:true,
				
				buttons: {
					YES: 
						function(){
						$(this).dialog('close');
						var elem = document.getElementById("User");
						elem.value = e.target.innerHTML.toLowerCase();
						userID = e.target.innerHTML.toLowerCase();
						},
					NO:
						function(){
						$(this).dialog('close');
						
						}
				}
				});
          //console.log("You clicked row " + e.target.innerHTML.toLowerCase());
        });
        
        
    	
    
    	    
    	var addUserBtn = $('<button id="addUserBtn" type="submit" class="btn btn-default"></button>').text('Submit User');
        $('#userSel').append(addUserBtn);
        
    	
        
        var submitUser = document.getElementById("addUserBtn");
        submitUser.addEventListener('click',function(e){
        	 if(document.getElementById('User').value == '') {
        		 alert("Please fill user field");
             }
        	 else {
        		 userID = document.getElementById('User').value;
        		 var out = getAvUserDates(userID);
        		 console.log('out',out);
        		 if(out == "success"){
        			 datePicker();
        		 }
        	 }
        	
        })
        
        
           
}



function enableSpecificDates(date) {
	var m = date.getMonth();
	var d = date.getDate();
	var y = date.getFullYear();
 
	//var currentdate = d + '-'+ (m+1) + '-' + y;
	var currentdate = new Date(y,m+1,d);
	console.log('currentdate',currentdate);
	if(currentdate >= minDate || currentdate <= maxDate)
	return [true];
	else
		return [false];
}
/*function selectTimeframe() {
	
	$( "#from" ).datepicker({
      defaultDate: "+1w",
      dateFormat: "dd-mm-yy",
      changeMonth: true,
      numberOfMonths: 3,
      beforeShowDay:enableSpecificDates,
      onClose: function( selectedDate ) { 
    	startDate = selectedDate;
        $( "#to" ).datepicker( "option", "minDate", selectedDate );
      }
    });
    $( "#to" ).datepicker({
      defaultDate: "+1w",
      dateFormat: "dd-mm-yy",
      changeMonth: true,
      numberOfMonths: 3,
      beforeShowDay: enableSpecificDates,
      onClose: function( selectedDate ) {
    	endDate = selectedDate;
        $( "#from" ).datepicker( "option", "maxDate", selectedDate );
      }
    });
}*/



function datePicker() {
	//alert('datePicker');
	
	/*var strTime = JSON.parse(sessionStorage.getItem('timeframe'));
	var splitter = strTime.split('#');
	var splitter1 = splitter[0].split(' ');
	var splitter2 = splitter[1].split(' ');
	
	console.log('minDate(string)',splitter1[0]);
	console.log('maxDate(string)',splitter2[0]);
	
	console.log('minDate(Date)',new Date(splitter1[0]));
	console.log('maxDate(Date)',new Date(splitter2[0]));*/
	
	
	$( "#from" ).datepicker({
	      changeMonth: true,
	      numberOfMonths: 1,
	      dateFormat: "yy-mm-dd",
	      //beforeShowDay: enableSpecificDates,
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
	      //beforeShowDay:enableSpecificDates,
	      onClose: function(selectedDate ) {
	    	  //var end =selectedDate.split("/").reverse().join("-"); 
	        $( "#from" ).datepicker( "option", "maxDate", selectedDate );
	        endDate = selectedDate;
	        console.log(' endDate: ' + selectedDate);
	      }
	    });
	
	    $("#timeline").show();
	    var submitTimeBtn = $('<button id="btnIdSub" type="submit" class="btn btn-default"></button>').text('Submit TimeFrame');
	    $('#closer').append(submitTimeBtn);
	    var closeBtn = $('<button id="btnId" class="btn btn-default"></button>').text('Close');
	    $('#closer').append(closeBtn);
	    
		
		
	
	var submit = document.getElementById("btnIdSub");
    submit.addEventListener('click',function(e){
 	 if(document.getElementById('User').value == '' || document.getElementById('from').value=='' || document.getElementById('to').value=='')
     {
 		 	alert("Please fill all the fields");
 	 }
 	 else
 	 {
 		//make get ajax call with params
 		 console.log('user: ' + document.getElementById('User').value + ' startDate: ' + document.getElementById('from').value + ' endDate: ' + document.getElementById('to').value);
 		 getApInfo();
 	 }
 	
 	 
    });
   
   
    var closer = document.querySelector('#btnId');
   closer.addEventListener('click',function(e){
	   var tab = document.querySelector('#tableID');
       
      
    	var hTag = document.querySelector('#headerTagId');
	   tab.parentNode.removeChild(tab); 
	   hTag.parentNode.removeChild(hTag);
	   closer.parentNode.removeChild(closer);
	   submit.parentNode.removeChild(submit);
	   
	   var elem = document.getElementById("User");
	   elem.value = '';
	   elem = document.getElementById("from");
	   elem.value = '';
	   elem = document.getElementById("to");
	   elem.value = '';
	   $("#timeline").hide();
	   $("#userSel").hide();
	   clickable(0);

   });
	
}


function clickable(option){
	if(option == 1){
		var link = document.getElementById('userLink');
		link.href="javascript:void(0);"
		link.style.color="grey";
	}
	else {
		var link = document.getElementById('userLink');
		link.href="javascript:options();"
			link.style.color="#000";
			link.onmouseover= function(){this.style.color="red";}
			link.onmouseout = function(){this.style.color="#000";}
			
	}
}





