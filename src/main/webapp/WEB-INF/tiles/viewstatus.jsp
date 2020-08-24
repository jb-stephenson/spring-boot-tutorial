<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="jbs" %>

<c:url var="url" value="/viewstatus"/>

<div class="row">
	<div class="col-md-8 col-md-offset-2">
		
		<jbs:pagination page="${page}" url="${url}" size="10"/>
		
		<c:forEach var="statusUpdate" items="${page.content}">
			<c:url var="editLink" value="/editstatus?id=${statusUpdate.id}" />
			<c:url var="deleteLink" value="/deletestatus?id=${statusUpdate.id}" />
			
			<div class="panel panel-default">
				<div class="panel-heading">
					<div class="panel-title">
						Last Updated: <fmt:formatDate pattern="EEEE d MMMM y 'at' H:mm:ss" value="${statusUpdate.dateAdded}"/>
					</div>
				</div>
				<div class="panel-body">
					<div>${statusUpdate.text}</div>
					<div class="edit-links pull-right">
						<a href="${editLink}">edit</a> | <a onclick="return confirm('Are you sure you wish to delete this update?');" href="${deleteLink}">delete</a>
					</div>
				</div>
			</div>
		</c:forEach>
	
	</div>
 </div>
