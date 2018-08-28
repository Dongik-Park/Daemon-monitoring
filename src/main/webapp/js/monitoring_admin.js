
$(document).ready(function() {
	
	// Add Service Btn listener(Add service Modal)
	$('#serviceBtn').click(function(){
		$.ajax({
			  url : "/monitor/admin/service",
			  type: "get",
			  dataType : "html",
			  success : function(res){
				 $('#modalContent').html(res); // serviceModalBody.jsp
			     $('.dropdown-toggle').dropdown();		 		  
			  },
			  error : function(){
			  }
		  });
		 $('#commonModal').modal('show');
	});
	
	// Add Server Btn listener(Add server Modal)
	$('#serverBtn').click(function(){
		$.ajax({
			  url : "/monitor/admin/server",
			  type: "get",
			  dataType : "html",
			  success : function(res){
				 $('#modalContent').html(res); // serverModalBody.jsp
			     $('.dropdown-toggle').dropdown();		 		  
			  },
			  error : function(){
			  }
		  });
		 $('#commonModal').modal('show');
	});
	
	// Add Daemon Btn listener(Add daemon Modal)
	$('#daemonBtn').click(function(){
		$.ajax({
			  url : "/monitor/admin/daemon",
			  type: "get",
			  dataType : "html",
			  success : function(res){
				 $('#modalContent').html(res); // daemonModalBody.jsp
			     $('.dropdown-toggle').dropdown();		 		  
			  },
			  error : function(){
			  }
		  });
		 $('#commonModal').modal('show');
	});
	
	// Load Daemons
	loadDaemons();
});

// Load daemon list 
var loadDaemons = function(){
	// ajax request to load daemon list from service
	$.ajax({
		  url : "/monitor/admin/main",
		  type: "get",
		  success : function(res){			  
			 $('#daemons').html(res); // daemons.jsp	 		  
		  },
		  error : function(){
		  }
	  });
}
