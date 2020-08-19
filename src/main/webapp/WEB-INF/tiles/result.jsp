<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  

<div class="row">
	<div class="col-md-12 results-noresults">
		<c:if test="${empty results}">
			No results
		</c:if>
	</div>
</div>

<c:forEach var="result" items="${results}">
	<c:url var="profilePhoto" value="/profilephoto/${result.userId}" />
	
	<div class="row">
		<div class="col-md-12">
			<div class="results-photo">
				<img src="${profilePhoto}" id="profile-photo-img">
			</div>
			
			<div class="results-detail">
				
				<div class="results-name">
					<c:out value="${result.firstname} ${result.surname}" />
				</div>
				
				<c:forEach var="interest" items="${result.interests}">
					${interest.name}
				</c:forEach>
			</div>
		</div>
	</div>
</c:forEach>