        var userID='';
        var startDate='';
        var endDate='';
        
function options() {
   	
	/*In this function we return the available users */

		  clickable(1);
		  var header = $('<h2 id="headerTagId"></h2>').text('Available Users');
		  $('#headerTag').append(header);
	
          var table = $('<table id="tableID"></table>');
          
          
        for (var i = 0; i < 10; i++) {
                row = $('<tr></tr>');
               
                var rowData = $('<td></td>').text('user1');
                rowData.id=i;
                row.append(rowData);
                table.append(row);
        }
 		
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
        $("#timeline").show();
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
        
        
    	
    	$( "#from" ).datepicker({
    	      defaultDate: "+1w",
    	      changeMonth: true,
    	      numberOfMonths: 2,
    	      dateFormat: "yy-mm-dd",
    	      onClose: function( start ) {
    	    	  //var start =selectedDate.split("/").reverse().join("-");
    	        $( "#to" ).datepicker( "option", "minDate", start );
    	        startDate = start;
    	        console.log(' startDate: ' + startDate);
    	      }
    	    });
    	    $( "#to" ).datepicker({
    	      defaultDate: "+1w",
    	      changeMonth: true,
    	      numberOfMonths: 2,
    	      dateFormat: "yy-mm-dd",
    	      onClose: function( end ) {
    	    	  //var end =selectedDate.split("/").reverse().join("-"); 
    	        $( "#from" ).datepicker( "option", "maxDate", end );
    	        endDate = end;
    	        console.log(' endDate: ' + endDate);
    	      }
    	    });
    	
    	var submitBtn = $('<button id="btnIdSub" type="submit" class="btn btn-default"></button>').text('Submit');
        $('#closer').append(submitBtn);
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





