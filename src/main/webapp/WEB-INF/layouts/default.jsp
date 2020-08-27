<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatiable" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">

<!-- JQuery -->
<script 
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js"></script>

<title>
	<tiles:insertAttribute name="title" />
</title>

<c:set var="contextRoot" value="${pageContext.request.contextPath}" />

<!-- Bootstrap -->
<link href="${contextRoot}/css/bootstrap.min.css" rel="stylesheet">
<link href="${contextRoot}/css/main.css" rel="stylesheet">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">

<script src="${contextRoot}/js/connectionmanager.js"></script>
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<!-- This is only for javascript tagging on the profile -->
<link href="${contextRoot}/css/jquery.tagit.css" rel="stylesheet">
<script src="${contextRoot}/js/jquery-ui.min.js"></script>
<script src="${contextRoot}/js/tag-it.min.js"></script>

<!-- Websocket Imports -->
<script src="/webjars/sockjs-client/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/stomp.min.js"></script>

<tiles:insertAttribute name="chatnotifications"></tiles:insertAttribute>
<tiles:insertAttribute name="chatviewscript" ignore="true"></tiles:insertAttribute>

</head>
<body>
	<nav class="navbar navbar-expand-lg">
		<a class="navbar-brand" href="#">Spring Boot Demo</a>

		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item active">
					<a class="nav-link" href="${contextRoot}/">Home 
						<span class="sr-only">(current)</span>
					</a>
				</li>

				<li class="nav-item">
					<a class="nav-link"href="${contextRoot}/about">About</a>
				</li>
			</ul>

			<ul class="nav navbar-nav navbar-right">
				
				<sec:authorize access="!isAuthenticated()">
					<li><a href="${contextRoot}/login">Login</a></li>
					<li><a href="${contextRoot}/register">Register</a></li>
				</sec:authorize>
				
				<sec:authorize access="isAuthenticated()">
					<li><a href="${contextRoot}/profile"><sec:authentication property="principal.firstname"/>'s Profile</a></li>"
					<li><a href="javascript:$('#logoutForm').submit();">Logout</a></li>
				</sec:authorize>
					
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">Status</a>
						<ul class="dropdown-menu">
							<li><a href="${contextRoot}/addstatus">Add Status</a></li>
							<li><a href="${contextRoot}/viewstatus">View Status Updates</a></li>
						</ul>
					</li>
				</sec:authorize>
			</ul>
		</div>
	</nav>

	<c:url var="logoutlink" value="/logout" />
	<form id="logoutForm" method="post" action="${logoutlink}">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>

	<div class="container">
		<tiles:insertAttribute name="content" />
	</div>

	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="${contextRoot}/js/bootstrap.min.js"> </script>
	
	<sec:authorize access="isAuthenticated()">
		<script>
			connectionManager.connect();
		</script>
	</sec:authorize>
</body>
</html>