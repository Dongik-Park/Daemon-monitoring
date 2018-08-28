
<body>

	<nav class="navbar navbar-default transparent">
	
		  <div class="container-fluid">
		    <div class="navbar-header" >
		      <a class="navbar-brand" href="#" style="font-size:20px;font-weight:bold">
		      	ebay
		      </a>
		    </div>
		    <ul class="nav navbar-nav" id="domains">
		    </ul>
		  </div>
	   
	   <!-- domain info tag -->
	   <div id="domain" style="display:none"></div>
	  
	</nav>
</body>
 <script type="text/javascript">
 $(document).ready(function() {
	 loadHeaders();
});
 
 // Load all domains by name
 var loadHeaders = function(){
	 // ajax request to get domain by service info
 	  $.ajax({
 		  url : "/monitor/headers",
 		  type: "get",
 		  dataType : "html",
 		  success : function(res){
 			  $('#domains').html(res); // headers_fragment.jsp	 		  
 		  },
 		  error : function(){
 			  
 		  }
 	  });
 }

//Load daemons by service name
var loadService = function(domain){
	 console.log("loadService "+ domain+" invocated");
	 // ajax request to get daemon status by service info
	  $.ajax({
		  url : "/monitor/main?domain="+domain,
		  type: "get",
		  dataType : "html",
		  success : function(res){
			  $('#Main').html(res); // service.jsp	
			  $('#domain').html(domain); 	
			  
			  if(typeof(history.pushState) == 'function'){
				  history.pushState(null,null,"/main");
			  }
		  },
		  error : function(){
			  
		  }
	  });
}

</script>
</html>