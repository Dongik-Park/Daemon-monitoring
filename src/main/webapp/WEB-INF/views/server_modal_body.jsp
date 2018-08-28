<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		  
		  <div class="modal-header">
			  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="modalTitle">Add Server</h4>
		  </div>
	      <div class="modal-body" >
			  <!-- Service type form group -->
			  <div class="form-group">
                <label for="ServiceTypeInput">Service</label>
                <div id="service" class="dropdown">
					 <button class="btn btn-default dropdown-toggle" type="button" id="choosedService" data-toggle="dropdown" aria-expanded="true">
					    Select Service
					    <span class="caret"></span>
					  </button>
					  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu3">
				      <c:forEach items="${serviceList}" var="list" varStatus="j">
					    <li role="presentation">
					    	<a class="list"role="menuitem" id="${list.id}" tabindex="-1" href="#">
					    		${list.name}.${list.type}
					    	</a>
					    </li>
					  </c:forEach>
 					</ul>
				</div>
              </div>
              
			  <!-- Server name form group -->
              <div class="form-group">
                <label for="ServerNameInput">Server Name</label>
                <input type="text" class="form-control" id="serverName" placeholder="Enter Server Name">
              </div>
              
			  <!-- Server ip form group -->
              <div class="form-group">
                <label for="IpInput">I P</label>
                <input type="text" class="form-control" id="ip" placeholder="127.0.0.1">
              </div>
              
			  <!-- Server cycle form group -->
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
              
			  <!-- Server activate form group -->
              <div class="form-group">
                <label for="ServiceTypeInput">Activate</label><br>
				<input type="checkbox" name="alarm" id="active" value="checked" style="margin:10px" checked> It will be activated.
              </div>
			  
			  <!-- Server alarm form group -->
              <div class="form-group">
                <label for="AlarmInput">Alarm</label><br>
				<input type="checkbox" name="alarm" id="slack" value="Slack" style="margin:10px" checked>Slack
				<input type="checkbox" name="alarm" id="email" value="Email" style="margin:10px" > Email 
              </div>
 		  </div>
          
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" id="serverAddBtn">Save changes</button>
	      </div>    		
 					
 <script type="text/javascript">
$(document).ready(function() {
	// dropdown toggle set
	$('.dropdown-toggle').dropdown();
	// set server id
	var service_id = 0;
	// a tag listener to change button text by selected item
	$("a").bind("click", function(e){
        $('#choosedService').html($(this).text()+'<span class="caret"></span>');
        service_id = $(this).attr("id");
    });
	// add server btn click listener
	$('#serverAddBtn').click(function(){
		// Read input data
		var service = $('#choosedService').text();
		var hostname = $('#serverName').val();
		var user_ip = $('#ip').val();
		var user_cycle = $('#cycle_sec').val() + " " + $('#cycle_min').val() +" " + $('#cycle_hour').val() + " " 
						+ $('#cycle_Dmonth').val() + " " + $('#cycle_month').val() + " " + $('#cycle_Dweek').val();
		var activate;
		if($('#active').is(":checked")){
			activate = 1;
		}		
		var s_alarm;
		if($('#slack').is(":checked")){
			s_alarm = 1;
		}		
		var e_alarm;
		if($('#email').is(":checked")){
			e_alarm = 1;
		}
		
		//ajax request to add new server
		$.ajax({
			  url : "/api/server",
			  type: "post",                
			  contentType: "application/x-www-form-urlencoded; charset=UTF-8", 
			  data : {
				  // new server infos
				  hostName : hostname,
				  serviceId : service_id,
				  ip : user_ip,
				  cycle : user_cycle,
				  status : 1,
				  enabled : activate
			  },
			  success : function(res){
				 alert(hostname +" successfully added.");	
				 $('#commonModal').modal('hide');			 		  
			  },
			  error : function(){
				 alert("Adding " + hostname + " failed.");	 	
			  }
		  });
	});
});
</script>
	