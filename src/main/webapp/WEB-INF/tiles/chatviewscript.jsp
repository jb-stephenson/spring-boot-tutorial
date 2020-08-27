<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:url var="outboundDestination" value="/app/message/send/${chatWithUserID}" />
<c:url var="inboundDestination" value="/user/queue/${thisUserID}" />

<script>

	function newMessageCallBack(message) {
		console.log("Message received: ", JSON.parse(message.body).text);
	}
	
	connectionManager.addSubscription("${inboundDestination}", newMessageCallBack);
	
	function sendMessage() {
		
		var text = $("#chat-message-text").val();
		var message = {
			'text': text	
		};
		
		connectionManager.send("${outboundDestination}", message);
		
		$("#chat-message-text").val("");
		$("#chat-message-text").focus();
	}

    $(document).ready(function () {
    	
    	$(document).keypress(function(e) {
    		
    		if(e.which == 13) {
    			
    			sendMessage();
    			return false;
    		}
    	});
    	
    	$('#chat-send-btn').click(function() {
    		sendMessage();
    	});
    });
</script>