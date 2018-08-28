$(document).ready(function() {
	// when today error exists
	if($.trim($('#today').text()) != "0"){
		$('.col-md-2').removeClass("alert-success").addClass('alert-danger');
	}
	// datatable source binding
	$('#example').DataTable();	
});
// Load daemons by server info
var loadDaemonsByServerId = function(id){
	// ajax request to get new daemon infos
	$.ajax({
		  url : "/monitor/daemonDetail",
		  type: "get",
		  dataType : "html",
		  data : {
			  serverId : id
		  },
		  success : function(res){
			 // update body to new daemon data
		     $("body").html('');
			 $("body").html(res);
		  },
		  error : function(){
			  
		  }
	  });
}
