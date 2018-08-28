<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

		  <div class="modal-header">
			  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="modalTitle">Add Daemon</h4>
		  </div>
	      <div class="modal-body" >
	      
			  <!-- Service form group -->
			  <div class="form-group">
                <label for="ServiceTypeInput">Service</label>
                <div id="service" class="dropdown">
					  <button class="btn btn-default dropdown-toggle" type="button" id="choosedService" data-toggle="dropdown" aria-expanded="true">
					    Select Service
					    <span class="caret"></span>
					  </button>
					  
					  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu3"> 
					  
				      <c:forEach items="${serviceList}" var="list" varStatus="j">
					    <li role="presentation"><a class="serviceList" role="menuitem" id="${list.id}" tabindex="-1" href="#">${list.name}.${list.type}</a></li>
					  </c:forEach>
 					</ul>
				</div>
              </div>
              
			  <!-- Server form group -->
              <div class="form-group">
                <label for="ServiceTypeInput">Server</label>
                <div id="server" class="dropdown">
					 <button class="btn btn-default dropdown-toggle" type="button" id="choosedServer" data-toggle="dropdown" aria-expanded="true">
					    Select Server
					    <span class="caret"></span>
					  </button>
					  
					  <ul id="serverListmain" class=" dropdown-menu" role="menu" aria-labelledby="dropdownMenu3">
 					  </ul>
				</div>
              </div>
              
			  <!-- Daemon name form group -->
              <div class="form-group">
                <label for="ServerNameInput">Daemon Name</label>
                <input type="text" class="form-control" id="daemonName" placeholder="Enter daemon Name">
              </div>
              
			  <!-- Daemon cycle form group -->
              <div class="form-group">
                <label for="IpInput">Cycle</label><a href="https://www.freeformatter.com/cron-expression-generator-quartz.html"> (help)</a>
                <div>
	                <input type="text" class="form-control col-md-3" id="cycle_sec" placeholder="sec" style='width:60px; margin-right:20px'>
	                <input type="text" class="form-control col-md-3" id="cycle_min" placeholder="min" style='width:60px; margin-right:20px' >
	                <input type="text" class="form-control col-md-3" id="cycle_hour" placeholder="hour" style='width:60px; margin-right:20px' >
	                <input type="text" class="form-control col-md-3" id="cycle_Dmonth" placeholder="d/m" style='width:60px; margin-right:20px' >
	                <input type="text" class="form-control col-md-3" id="cycle_month" placeholder="month" style='width:60px; margin-right:20px' >
	                <input type="text" class="form-control col-md-3" id="cycle_Dweek" placeholder="d/w" style='width:60px; margin-right:20px' >
	                
                </div>
              </div>
              
              <br><br>
              
			 <!-- Daemon activate form group -->
              <div class="form-group">
                <label for="ServiceTypeInput">Activate</label><br>
				<input type="checkbox" name="alarm" id="active" value="checked" style="margin:10px" checked> It will be activated.
              </div>
			  
			  <!-- Daemon alarm form group -->
              <div class="form-group">
                <label for="AlarmInput">Alarm</label><br>
				<input type="checkbox" name="alarm" id="slack" value="Slack" style="margin:10px" checked>Slack
				<input type="checkbox" name="alarm" id="email" value="Email" style="margin:10px" > Email 
              </div>
              
			  <!-- Daemon alarm url form group -->
              <div class="form-group">
                <label for="ServerNameInput">Alarm URL</label>
                <input type="text" class="form-control" id="alarmURL" placeholder="https://hivearena.slack.com/services/new">
              </div>
              
			  <!-- Daemon description form group -->
              <div class="form-group">
                <label for="ServerNameInput">Receiver</label>
                <input type="text" class="form-control" id="receiver" placeholder="dongipark myupark jaesin">
              </div>
              
			  <!-- Daemon description form group -->
              <div class="form-group">
                <label for="ServerNameInput">Description</label>
                <input type="text" class="form-control" id="description" placeholder="Enter description">
              </div>
           </div>
          
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" id="daemonAddBtn">Save changes</button>
	      </div>    
	      
<script type="text/javascript">
$(document).ready(function() {
	// Set server id
	var server_id = 0;
	
	// dropdown toggle set
	$('.dropdown-toggle').dropdown();
	
	// Change button text by selected item	
	$('.serviceList').click(function(){
		$('#choosedService').html($(this).text()+'<span class="caret"></span>');
		$('#choosedServer').html('Select Server<span class="caret"></span>');
		// service id set
		var service_id = $(this).attr("id");
		// ajax request for serverList
		$.ajax({
			  url : "/monitor/admin/serverList",
			  type: "get",
			  data : {
				  serviceId : service_id
			  },
			  success : function(res){
				 console.log(res);
				 // Set serverList html
				 var txt = "";
				 for(var i = 0; i < res.length; ++i){
					 txt += "<li role='presentation'><a class='serverList' role='menuitem' id='" +
					 			res[i].id + 
					 			"' tabindex ='-1' href='#'>" + 
					 			res[i].hostName +
					 			"</a></li>";
				 }				 
				 // Server list update
				 $('#serverListmain').html(txt);
				 // click listener bind
		 		 $('.serverList').click(function(){
		 			 server_id = $(this).attr("id");
		 			$('#choosedServer').html($(this).text()+'<span class="caret"></span>');
		 		 });
			  },
			  error : function(){
					alert("Load server list failed.");
			  }
		  });
	});
	
	// Save changes btn click 	
	$('#daemonAddBtn').click(function(){
		// Check server id
		if(server_id == 0){
			alert("Select a host first.");
			return;
		}
		// Read input data
		var service = $('#choosedService').text();
		var daemonName = $('#daemonName').val();
		var alarmUrl = $('#alarmURL').val();
		var description = $('#description').val();
		var receiver = $('#receiver').val();
		var user_cycle = $('#cycle_sec').val() + " " + $('#cycle_min').val() +" " + $('#cycle_hour').val() + " " 
						+ $('#cycle_Dmonth').val() + " " + $('#cycle_month').val() + " " + $('#cycle_Dweek').val();
		var activate;
		if($('#active').is(":checked")){
			activate = 1;
		}
		var s_alarm = 0;
		if($('#slack').is(":checked")){
			s_alarm = 1;
		}
		var e_alarm = 0;
		if($('#email').is(":checked")){
			e_alarm = 1;
		}
		
		var current = getTimeStamp();
		
		// Ajax request to add daemon
		$.ajax({
			  url : "/api/daemon",
			  type: "post",                
			  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			  data : {
				  // New daemon infos
				  serverId : server_id,
				  name : daemonName,
				  cycle : user_cycle,
				  recentTime : current,
				  status : 1,
				  notiUrl : alarmUrl,
				  notiType : e_alarm + s_alarm,
				  enabled : activate,
				  description : description,
				  receiver : receiver
			  },
			  success : function(res){
				 alert(daemonName+ " successfully added.");
				 $('#commonModal').modal('hide');
			  },
			  error : function(){
				 alert("Adding " + daemonName + " failed.");
			  }
		  });
	});
});
function getTimeStamp() {
	  var d = new Date();
	  var s =
	    leadingZeros(d.getFullYear(), 4) + '-' +
	    leadingZeros(d.getMonth() + 1, 2) + '-' +
	    leadingZeros(d.getDate(), 2) + ' ' +

	    leadingZeros(d.getHours(), 2) + ':' +
	    leadingZeros(d.getMinutes(), 2) + ':' +
	    leadingZeros(d.getSeconds(), 2);

	  return s;
	}

	function leadingZeros(n, digits) {
	  var zero = '';
	  n = n.toString();

	  if (n.length < digits) {
	    for (i = 0; i < digits - n.length; i++)
	      zero += '0';
	  }
	  return zero + n;
	}
</script>