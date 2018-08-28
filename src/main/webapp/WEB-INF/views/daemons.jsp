<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>admin</title>

<!-- Data table import -->
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/dt/dt-1.10.18/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css">

</head>
<body>
<table id="example" class="display table table-hover" style="width:100%">
			<!-- table headers -->
	        <thead>
	            <tr>
	                <th>Server</th>
	                <th>Daemon</th>
	                <th>Description</th>
	                <th>Status</th>
	                <th>Cycle</th>
	                <th>RecentTime</th>
	                <th>Enable</th>
	                <th>Update</th>
	            </tr>
	        </thead>
	        <!-- table body -->
	        <tbody id="tBody">
	        	<!-- Read daemon list -->
				<c:forEach items="${daemons}" var="daemon" varStatus="j">
				  <c:choose>
				       <c:when test="${daemon.status == 1}">
				            <tr class="daemon" id="${daemon.id}">
				                <td> ${daemon.hostName} </td> 
				                <td> ${daemon.name} </td> 
				                <td> ${daemon.description} </td> 
				                <td style="text-align:center"> <span style="color:#1e9966" class="glyphicon glyphicon-ok" aria-hidden="true"></span></td> 
				                <td style="text-align:center"> ${daemon.cycle} </td> 
				                <td style="text-align:center"> ${daemon.recentTime} </td> 
				                <!-- Check daemon enabled status -->
				                <c:choose>
				                	<c:when test="${daemon.enabled eq 1}">
				                		<td style="text-align:center"><button class="btn btn-success btn-xs" onclick="javascript:activateBtnClicked(${daemon.id})">enable</button></td> 
				                	</c:when>
				                	<c:otherwise>
				                		<td style="text-align:center"><button class="btn btn-default btn-xs" onclick="javascript:activateBtnClicked(${daemon.id})">disable</button></td> 
				                	</c:otherwise>
				                </c:choose>
				                <td style="text-align:center"><span class="glyphicon glyphicon-pencil" aria-hidden="true" onclick="javascript:daemonEditClicked(${daemon.id})"></span> / <span class="glyphicon glyphicon-trash" aria-hidden="true" onclick="javascript:daemonDeleteClicked(${daemon.id})"></span></td>
				       </c:when>
				       <c:otherwise>
	                	   <!-- Daemon occured error -->
				            <tr class="daemon" id="${daemon.id}" style="text-shadow: none">
				                <td> ${daemon.hostName} </td> 
				                <td> ${daemon.name} </td> 
				                <td> ${daemon.description} </td> 
				                <td style="text-align:center"> <span style="color:red"class="glyphicon glyphicon-alert" aria-hidden="true"><a style="display:none">1</a></span></td> 
				                <td style="text-align:center"> ${daemon.cycle} </td> 
				                <td style="text-align:center"> ${daemon.recentTime} </td> 
				                <!-- Check daemon enabled status -->
				                <c:choose>
				                	<c:when test="${daemon.enabled eq 1}">
				                		<td style="text-align:center"><button class="btn btn-success btn-xs" onclick="javascript:activateBtnClicked(${daemon.id})">enable</button></td> 
				                	</c:when>
				                	<c:otherwise>
				                		<td style="text-align:center"><button class="btn btn-default btn-xs" onclick="javascript:activateBtnClicked(${daemon.id})">disable</button></td> 
				                	</c:otherwise>
				                </c:choose>
				                <td style="text-align:center"><span class="glyphicon glyphicon-pencil" aria-hidden="true" onclick="javascript:daemonEditClicked(${daemon.id})" ></span> / <span class="glyphicon glyphicon-trash" aria-hidden="true" onclick="javascript:daemonDeleteClicked(${daemon.id})"></span></td>
				            </tr>
				       </c:otherwise>
				   </c:choose>
	            </c:forEach>
	        </tbody>
	        <tfoot>
	            <tr>
	                <th>Server</th>
	                <th>Daemon</th>
	                <th>Description</th>
	                <th>Status</th>
	                <th>Cycle</th>
	                <th>RecentTime</th>
	                <th>Enable</th>
	                <th>Update</th>
	            </tr>
	        </tfoot>
	    </table>
</body>
<script type="text/javascript">
// Activate btn click listener
var activateBtnClicked = function(id){
	// Ajax request to change daemon activation
	$.ajax({
		  url : "/monitor/admin/daemon/enable",
		  type: "put",
		  data : {
			  daemonId : id
		  },
		  dataType : "text",
		  success : function(res){
			  console.log(res);
			 alert(res + " successfully changed.");
		  },
		  error : function(res){
			 console.log(res);
			 alert("failed.");
		  }
	  });
}
// Delete icon click listener
var daemonDeleteClicked = function(id){
	// id check
	if(id < 1){
		alert("Check daemon.")
		return;
	}	
	// Confirm delete
	var result = confirm('Are you sure you want to delete?'); 
	if(result) { //yes 
		// Ajax request to delete daemon 
		$.ajax({
			  url : "/monitor/admin/daemon?" + $.param({"daemonId": id}),
			  type: "delete",
			  success : function(res){
				 alert(res + " successfully deleted.");
			  },
			  error : function(res){
				 console.log(res);
				 //alert(res.name + " - " + res.enabled + " failed.");
			  }
		  });
	} 
	else { //no 
		
	}
}
// Edit icon click listener
var daemonEditClicked = function(id){	
	// Ajax request to get daemon detail info
	$.ajax({
		  url : "/monitor/admin/daemon/edit",
		  type: "get",
		  data : {
			  daemonId : id
		  },
		  success : function(res){
			 $('#modalContent').html(res); // daemonEditModalBody.jsp
		     $('.dropdown-toggle').dropdown();
		  },
		  error : function(res){
				alert("Transaction failed.");
		  }
	  });
	//수신된 데이터를 바탕으로 모달 생성
	$('#commonModal').modal('show');
}
$(document).ready(function() {
	// datatable source binding
	$('#example').DataTable();	
});
</script>
</html>