<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib tagdir="/WEB-INF/tags" prefix="jbs" %>

<div class="row">
	<div class="col-md-12 results-noresults">
		<c:if test="${empty page.content}">
			No results
		</c:if>
	</div>
</div>

<c:url var="searchUrl" value="/search?s=${s}"/>
<c:url var="image" value="/img" />

<div class="row">
	<div class="col-md-12">
		<jbs:pagination page="${page}" url="${searchUrl}" size="10"/>
	</div>
</div>

<c:forEach var="result" items="${page.content}">
	<c:url var="profilePhoto" value="/profilephoto/${result.userId}" />
	<c:url var="profileLink" value="/profile/${result.userId}" />
	<c:url var="chatViewLink" value="/chatview/${result.userId}" />
	
	<div class="row person-row">
		<div class="col-md-12">
			<div class="results-photo">
				<a href="${profileLink}"><img src="${profilePhoto}" id="profile-photo-img"></a>
			</div>
			
			<div class="results-detail">
				
				<div class="results-name">
					<a href="${profileLink}"><c:out value="${result.firstname} ${result.surname}" /></a>
				</div>
				
				<div class="results-interests">
					<c:forEach var="interest" items="${result.interests}" varStatus="status">
						<c:url var="interestLink" value="/search?s=${interest.name}" />
						
						<a href="${interestLink}"><c:out value="${interest.name}" /></a>
						
						<c:if test="${!status.last}"> | </c:if>
					</c:forEach>
				</div>
				
				<div class="results-contact">
					<a href="${chatViewLink}">
						<img id="message-icon" src="${image}/message.png" alt="contact this user" />
					</a>
				</div>
			</div>
		</div>
	</div>
</c:forEach>