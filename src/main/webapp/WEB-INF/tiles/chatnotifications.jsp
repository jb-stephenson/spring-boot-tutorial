<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>

	
<sec:authorize access="isAuthenticated()">

<c:url var="webSocketEndPoint" value="/chat" scope="request" />
<c:url var="inboundDestination" value="/user/queue/${thisUserID}" />

	<script>
	
		var connectionManager = new ConnectionManager("${webSocketEndPoint}");
	
		connectionManager.addSubscription("${inboundDestination}", function(messageJson) {
			var message = JSON.parse(messageJson.body);
			
			alert(message);
		});
		
	
	</script>

</sec:authorize>