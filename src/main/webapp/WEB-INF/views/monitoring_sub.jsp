<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Process monitoring Page</title>
<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<!-- 부가적인 테마 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<link href="https://fonts.googleapis.com/css?family=Quicksand|Source+Sans+Pro" rel="stylesheet">

<!-- 합쳐지고 최소화된 최신 자바스크립트 -->    <!-- jquery -->
<script src="//code.jquery.com/jquery-1.12.0.min.js"></script>
<script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<!-- Web socket CDN -->
<script src="http://cdn.sockjs.org/sockjs-0.3.4.js"></script>
<!-- Datatable js -->
<script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.18/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css">

<!-- js 파일 -->
<script src="/js/monitoring_sub.js"></script>

<!-- css 파일 -->
<link rel="stylesheet" type="text/css" href="/styles/init.css">
<link rel="stylesheet" type="text/css" href="/styles/div_log_count.css">
<link rel="stylesheet" type="text/css" href="/styles/dataTable.css">
<link rel="stylesheet" type="text/css" href="/styles/div_error.css">
<link rel="stylesheet" type="text/css" href="/styles/div_normal.css">
<link rel="stylesheet" type="text/css" href="/styles/div_pannel_daemons.css">

</head>
<body>
	<!-- 헤더부 -->
	<jsp:include page="headers.jsp"/>
	<div class="container" id="Main">
		<div class="col-md-12" style="text-align:center;">
		
			<!-- log count area start -->
			<div class="col-md-2 alert alert-success normalCount" >
				Today <br><br>
				<strong id ="today" style="font-size:50px" >
					${logCount.today}
				</strong>
			</div>
			<div class="col-md-2 alert alert-success normalCount">
				This week <br><br>
				<strong id ="week" style="font-size:50px">
					${logCount.week}
				</strong>
			</div>
			<div class="col-md-2 alert alert-success normalCount">
				3 month <br><br>
				<strong id="quarter" style="font-size:50px">
					${logCount.quarter}
				</strong>
			</div>
			<div class="col-md-2 alert alert-success normalCount">
				Total <br><br>
				<strong id="total" style="font-size:50px">
					${logCount.total}
				</strong>
			</div>
			<!-- log count area end -->
			
		</div>
		
		<!-- Daemon list area start -->
		<div id="daemonListArea">
		<table id="example" class="display table table-hover" style="width:100%;text-shadow: 0 1px 2px rgba(0,0,0,.4)" >
	        <thead>
	            <tr>
	                <th>Server</th>
	                <th>Daemon</th>
	                <th>Description</th>
	                <th>Status</th>
	                <th>Cycle</th>
	                <th>RecentTime</th>
	                <th>ErrorTime</th>
	                <th>ErrorType</th>
	            </tr>
	        </thead>
	        <tbody id="tBody">
				<c:forEach items="${daemons}" var="daemon" varStatus="j">
				  <c:choose>
				       <c:when test="${daemon.status == 1}">
				            <tr>
				                <td> ${daemon.hostName} </td> 
				                <td> ${daemon.name} </td> 
				                <td> ${daemon.description} </td> 
				                <td> <span style="color:#1e9966" class="glyphicon glyphicon-ok" aria-hidden="true"></span></td> 
				                <td> ${daemon.cycle} </td> 
				                <td> ${daemon.recentTime} </td> 
				                <td> ${daemon.errorTime} </td> 
				                <td> ${daemon.errorType} </td> 
				            </tr>
				       </c:when>
				       <c:otherwise>
				            <tr style="background:#FDDDDD;color:red;font-weight: bold; text-shadow: none;">
				                <td> ${daemon.hostName} </td> 
				                <td> ${daemon.name} </td> 
				                <td> ${daemon.description} </td> 
				                <td> <span class="glyphicon glyphicon-alert" aria-hidden="true"><a style="display:none">1</a></span></td>
				                <td> ${daemon.cycle} </td> 
				                <td> ${daemon.recentTime} </td> 
				                <td> ${daemon.errorTime} </td> 
				                <td> ${daemon.errorType} </td> 
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
	                <th>ErrorTime</th>
	                <th>ErrorType</th>
	            </tr>
	        </tfoot>
	    </table>
    </div>
	</div>
    <!-- Daemon list area end -->
	
	<!-- Server info tag -->
	<div id="serverId" style="display:none">${logCount.serverId}</div>
	
</body>
<script type="text/javascript">

//----- websocket functions start -----
	/*
	//웹소켓을 지정한 url로 연결
	let sock = new SockJS("<c:url value="/wsMain"/>");
	sock.onmessage = onMessage;
	sock.onclose = onClose;
	
	//메시지 전송
	function sendMessage() {
	}
	
	//서버로부터 메시지를 받았을 때
	function onMessage(msg) {
		loadHeaders();
		// 현재 페이지에 대해 서버 ID로 데몬 정보 갱신
		loadDaemonsByServerId($("#serverId").html());
	}
	
	//서버와 연결을 끊었을 때
	function onClose(evt) {
		console.log("websocket disconnected.");
	}*/
//----- websocket functions end -----
</script>
</html>