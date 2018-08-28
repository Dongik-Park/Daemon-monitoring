<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

<!-- js 파일 -->
<script src="/js/monitoring_main.js"></script>

<!-- css 파일 -->
<link rel="stylesheet" type="text/css" href="/styles/init.css">
<link rel="stylesheet" type="text/css" href="/styles/div_error.css">
<link rel="stylesheet" type="text/css" href="/styles/div_normal.css">
<link rel="stylesheet" type="text/css" href="/styles/div_pannel_daemons.css">
<link rel="stylesheet" href="../styles/cover.css">

</head>
<body>

   <!-- 헤더부 -->
   <jsp:include page="headers.jsp"/>
   
   <!-- 데몬 패널 목록 -->
   <div class = "container" id="Main">
   </div>
   
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
		loadHeaders();
		loadService($('#domain').html());
	}
	
	//서버와 연결을 끊었을 때
	function onClose(evt) {
		console.log("websocket disconnected.");
	}
	
//----- websocket functions end -----
	
</script>
</html>