<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<body>

	<div class="container" id="container">
		<c:forEach items="${daemonMap}" var="list" varStatus="i">
		 	 	<div class="panel panel-default" id = "${services.name}">
		 	 		<!-- Set service full name 'service name'.'service type' -->
					<div class="panel-heading" style="font-family: 'Quicksand', sans-serif;">
						${list.key}
					</div>
					<!-- Set daemons included in service -->
					<div class="panel-body">
					
						<c:forEach items="${list.value}" var="servers" varStatus="j">
							<div class="panel panel-default col-xs-2" id="${servers.key}" >
						 	 		<!-- Set service full name 'service name'.'service type' -->
									<div class="panel-heading" style="background: none; font-family: 'Quicksand', sans-serif;">
										${servers.key}
									</div>
									<div class="panel-body">
										<div class="row" id="${servers.key}Daemons" >
											<c:forEach items="${servers.value}" var="daemons" varStatus="j">
													<c:choose>
												       <c:when test="${daemons.status == 1}">
														 <div class="col-xs-1 normal" id="${daemons.serverId}" onclick="showList(this.id)"
														 data-toggle="tooltip" data-placement="bottom" title="Server : ${daemons.hostName }&#13;Name : ${daemons.name} &#13;Cycle : ${daemons.cycle} &#13;Time : ${daemons.recentTime}">
															
														</div>
														
												       </c:when>
												       <c:otherwise>
												         <!-- Error state -->
														 <div class="col-xs-1 error" id="${daemons.serverId}" onclick="showList(this.id)"
														 data-toggle="tooltip" data-placement="bottom" title="Server : ${daemons.hostName }&#13;Name : ${daemons.name} &#13;Cycle : ${daemons.cycle} &#13;Time : ${daemons.recentTime}">
														 	!
														</div>
												       </c:otherwise>
												   </c:choose>
											</c:forEach>
										</div>
									</div>
							</div>			
						</c:forEach>
							
						
			
					</div>
				</div>
		</c:forEach>
	</div>
</body>
 <script>
// Daemon div click listener to sub page
var showList = function(id) {
	var url="/monitor/daemonDetail?serverId="+id;
	$(location).attr('href',url);
}
</script>
</html>