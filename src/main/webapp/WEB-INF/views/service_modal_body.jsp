<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

		  <div class="modal-header">
			  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="modalTitle">Add Service</h4>
		  </div>
	      <div class="modal-body" >
			<!-- content goes here -->
			<form>
			  <!-- Service name form group -->
              <div class="form-group">
                <label for="ServiceNameInput">Service Name</label>
                <input type="text" class="form-control" id="serviceName" placeholder="Enter Service Name">
              </div>
              
			  <!-- Service type form group -->
              <div class="form-group">
                <label for="ServiceTypeInput">Service Type</label>
                <div id="serviceModal" class="dropdown">
					 <button class="btn btn-default dropdown-toggle" type="button" id="choosedService" data-toggle="dropdown" aria-expanded="true">
					    Select
					    <span class="caret"></span>
					  </button>
					  <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu3">
				      <c:forEach items="${serviceList}" var="list" varStatus="j">
					    <li role="presentation"><a class="list" role="menuitem" id="${list.type}" tabindex="-1" href="#">${list.type}</a></li>
					  </c:forEach>
 					</ul>
				</div>
              </div>
            </form> 					
 		  </div>
          
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        <button type="button" class="btn btn-primary" id="serverAddBtn">Save changes</button>
	      </div>    		
 <script type="text/javascript">
$(document).ready(function() {
	// dropdown toggle set
	$('.dropdown-toggle').dropdown();
	// a tag listener to change button text by selected item
	$("a").bind("click", function(e){
        $('#choosedService').html($(this).attr("id")+'<span class="caret"></span>');
    });
	// add server btn click listener
	$('#serverAddBtn').click(function(){
		// Read input data
		var serviceType = $('#choosedService').text();
		var serviceName = $('#serviceName').val();
		// Check serviceName and type
		if(serviceName == null || serviceName == ""){
			alert("Check service name.")
			return;
		}
		if(serviceType.includes("Select") == true){
			alert("Select service type.");
			return;
		}
		//ajax request to add new server
		$.ajax({
			  url : "/api/service",
			  type: "post",
			  data : {
				  // new service infos
				  name : serviceName,
				  type : serviceType
			  },
			  success : function(res){
				 alert(serviceName+"."+serviceType+" successfully added.");		 
				 $('#commonModal').modal('hide');				  
			  },
			  error : function(){
			     alert("Adding " + serviceName+"."+serviceType+" failed.");		
			  }
		  });
	});
});
</script>
	