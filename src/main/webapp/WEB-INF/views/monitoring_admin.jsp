<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Admin Page</title>
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

<!-- js 파일 -->
<script src="/js/monitoring_admin.js"></script>
<script src="/js/dataTable.js"></script>

<!-- css 파일 -->
<link rel="stylesheet" type="text/css" href="/styles/init.css">
<!-- <link rel="stylesheet" href="../styles/cover.css">-->

</head>
<body>
	<!-- 헤더부 -->
   <jsp:include page="headers_admin.jsp"/>

	<div class="container">
	
		<!-- 버튼 그룹 -->
		<div class="btn-group" style="margin:13px">
		  <button type="button" class="btn btn-default" data-toggle="modal" id="serviceBtn">Add Service</button>
		  <button type="button" class="btn btn-default" data-toggle="modal" id="serverBtn">Add Server</button>
		  <button type="button" class="btn btn-default" data-toggle="modal" id="daemonBtn">Add Daemon</button>
		 </div>
		 
		<!-- 데몬 목록 그룹 -->
		<div id="daemons" style="margin:13px">
		</div>
		
	</div>	
	
	<!-- 추가 모달 -->
	<div class="modal fade" id ="commonModal">
	  <div class="modal-dialog">
	    <div class="modal-content" id="modalContent">
	      
	     	 <!-- Dynamic body content -->
	      
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
</body>
<script type="text/javascript">

//----- websocket functions start -----

	//웹소켓을 지정한 url로 연결
	let sock = new SockJS("<c:url value="/wsMain"/>");
	sock.onmessage = onMessage;
	sock.onclose = onClose;
	
	//메시지 전송
	function sendMessage() {
	}
	
	//서버로부터 메시지를 받았을 때
	function onMessage(msg) {
		console.log(msg);
		// Reload daemon list
		loadDaemons();
		// datatable source binding
		$('#example').DataTable();
	}
	
	//서버와 연결을 끊었을 때
	function onClose(evt) {
		console.log("websocket disconnected.");
	}
	
//----- websocket functions end -----

</script>
</html>