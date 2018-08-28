<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

    <!-- Home a tag -->
    <li><a id="all" href="javascript:void(0);" onclick="loadService(this.id);">Home</a></li>
	
	<!-- Domains tag area -->
	<c:forEach items="${domains}" var="domain" varStatus="j">
     		<li><a id="${domain.name}" href="javascript:void(0);" onclick="loadService(this.id);">${domain.name}</a></li>
	</c:forEach>