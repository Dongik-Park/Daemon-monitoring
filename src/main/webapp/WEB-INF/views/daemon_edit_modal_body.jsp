<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

	      <div class="modal-header">
			  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="modalTitle">Edit ${daemon.name}</h4>
		  </div>
	      <div class="modal-body" >
	      
			  <!-- Service form group -->
			  <div class="form-group">
                <label for="ServiceTypeInput">Service</label>
                <div id="service" class="dropdown">
					 <button class="btn btn-default dropdown-toggle" type="button" id="choosedService" data-toggle="dropdown" aria-expanded="true">
					    ${daemon.serviceName}
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
					     ${daemon.hostName}
					    <span class="caret"></span>
					  </button>
					  <input type="hidden" id="cur_server" value="${daemon.serverId}">
					  <ul id="serverListmain" class=" dropdown-menu" role="menu" aria-labelledby="dropdownMenu3">
						<c:forEach items="${map}" var="list" varStatus="i">
					      	<c:forEach items="${list.value}" var="server" varStatus="j">
						    	<li role="presentation"><a class="${list.key}" role="menuitem" id="${server.id}" tabindex="-1" href="#">${server.hostName}</a></li>
						  	</c:forEach>
						</c:forEach>
 					  </ul>
				</div>
              </div>
              
			  <!-- Daemon name form group -->
              <div class="form-group">
                <label for="ServerNameInput">Daemon Name</label>
                <input type="text" class="form-control" id="daemonName" placeholder="Enter daemon Name" value="${daemon.name}">
              </div>
              
			  <!-- Daemon cycle form group -->
              <div class="form-group">
                <label for="IpInput">Cycle</label><a href="https://www.freeformatter.com/cron-expression-generator-quartz.html"> (help)</a>
                <br>
                <c:forEach items="${fn:split(daemon.cycle,' ')}" var="item" varStatus="i">
					 <input type="text" class="form-control col-md-3" value="${item}"
					 				id="cycle_${i.index}" style='width:60px; margin-right:20px'>
	            </c:forEach>
              </div>
              
              <br><br>
              
			  <!-- Daemon activate form group -->
              <div class="form-group">
                <label for="ServiceTypeInput">Activate</label><br>
                <c:choose>
                	<c:when test="${daemon.enabled eq 1 }">
				<input type="checkbox" name="alarm" id="active" value="checked" style="margin:10px" checked> It will be activated.
					</c:when>
					<c:otherwise>
				<input type="checkbox" name="alarm" id="active" value="checked" style="margin:10px"> It will be activated.
					</c:otherwise>
                </c:choose>
              </div>
			  
			  <!-- Daemon alarm form group -->
              <div class="form-group">
                <label for="AlarmInput">Alarm</label><br>
                <c:choose>
                	<c:when test="${daemon.notiType eq 0 }">
						<input type="checkbox" name="alarm" id="slack" value="Slack" style="margin:10px" >Slack
						<input type="checkbox" name="alarm" id="email" value="Email" style="margin:10px" > Email
					</c:when>
                	<c:when test="${daemon.notiType eq 1 }">
						<input type="checkbox" name="alarm" id="slack" value="Slack" style="margin:10px" checked>Slack
						<input type="checkbox" name="alarm" id="email" value="Email" style="margin:10px" > Email
					</c:when>
                	<c:when test="${daemon.notiType eq 2 }">
						<input type="checkbox" name="alarm" id="slack" value="Slack" style="margin:10px" >Slack
						<input type="checkbox" name="alarm" id="email" value="Email" style="margin:10px" checked> Email
					</c:when>
					<c:otherwise>
						<input type="checkbox" name="alarm" id="slack" value="Slack" style="margin:10px" checked>Slack
						<input type="checkbox" name="alarm" id="email" value="Email" style="margin:10px" checked> Email
					</c:otherwise>
                </c:choose>
              </div>
              
			  <!-- Daemon alarm url form group -->
              <div class="form-group">
                <label for="ServerNameInput">Alarm URL</label>
                <input type="text" class="form-control" id="alarmURL" value="${daemon.notiUrl}">
              </div>
              
			  <!-- Daemon description form group -->
              <div class="form-group">
                <label for="ServerNameInput">Description</label>
                <input type="text" class="form-control" id="description" value="${daemon.description}">
                
			  <!-- Daemon info tag -->
			  <div id="daemonId" style="display:none">${daemon.id}</div>
	          </div>
	          
			  <!-- Daemon info tag -->
			  <div id="daemonRecentTime" style="display:none">${daemon.recentTime}</div>
			  
			  <!-- Daemon info tag -->
			  <div id="daemonStatus" style="display:none">${daemon.status}</div>
			  
	          </div>
	          
          
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" id="daemonEditBtn">Save changes</button>
	      </div>    
	      
<script type="text/javascript">
$(document).ready(function() {
	// Server id set
	var server_id = $('#cur_server').val();
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
				 // set dropdown list
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
				 // click listener
		 		 $('.serverList').click(function(){
		 			 server_id = $(this).attr("id");
		 			$('#choosedServer').html($(this).text()+'<span class="caret"></span>');
		 		 });
			  },
			  error : function(){
				  
			  }
		  });
	});
	
	// Save changes btn click 	
	$('#daemonEditBtn').click(function(){
		// check server_id
		if(server_id == 0){
			alert("Select a host first.");
			return;
		}
		// Read input data
		var daemon_id = $("#daemonId").html();
		var service = $('#choosedService').text();
		var daemonName = $('#daemonName').val();
		var alarmUrl = $('#alarmURL').val();
		var description = $('#description').val();
		var recent_time = $("#daemonRecentTime").html();
		var d_status = $("#daemonStatus").html();
		var receiver = $('#receiver').val();
		var user_cycle = $('#cycle_0').val() + " " + $('#cycle_1').val() +" " + $('#cycle_2').val() + " " 
						+ $('#cycle_3').val() + " " + $('#cycle_4').val() + " " + $('#cycle_5').val();
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
		// ajax request to update daemon
		$.ajax({
			  url : "/api/daemon",
			  type: "put",
			  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			  data : {
				  // New daemon infos
				  id : daemon_id,
				  serverId : server_id,
				  name : daemonName,
				  cycle : user_cycle,
				  recentTime : recent_time,
				  status : d_status,
				  notiUrl : alarmUrl,
				  notiType : e_alarm + s_alarm,
				  enabled : activate,
				  description : description,
				  receiver : receiver
			  },
			  success : function(res){
				 alert(daemonName + " successfully updated.");
				 $('#commonModal').modal('hide');		 		  
			  },
			  error : function(){
				  
			  }
		  });
	});	
	
});
</script>