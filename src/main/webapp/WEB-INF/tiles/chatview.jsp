<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">

	<div class="col-md-8 col-md-offset-2">
	
		<div class="panel panel-default">
		
			<div class="pane panel-heading">
				<div class="panel-title">Chatting with <c:out value="${chatWithUserName}"/></div>
			</div>
			<div class="panel-body">
				<div id="chat-message-view">
					<div id="chat-message-previous">
						<a id="chat-older-messages" href="#">View Older Messages</a>
					</div>
					
					<div class="chat-message-record" id="chat-message-record">
					</div>
					
					<div class="input-group input-group-lg">
						<input id="chat-message-text" type="text" class="form-control" placeHolder="Enter message . . .">
						<span class="input-group-btn">
							<button id="chat-send-btn" class="btn btn-primary" type="button">Send</button>
						</span>
					</div>
				</div>
			</div>
		
		</div>
	
	</div>

</div>