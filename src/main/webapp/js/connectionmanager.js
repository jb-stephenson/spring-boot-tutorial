/*****************************************************************************
 *
 *   Details with retrieving messages, creating message notifications, etc.
 *
 *****************************************************************************/

function ConnectionManager(webSocketEndPoint) {
	console.log("new ConnectionManager created");
	
	var csrfTokenName = $("meta[name='_csrf_header']").attr("content");
	var csrfTokenValue = $("meta[name='_csrf']").attr("content");
	
	console.log("CSRF name", csrfTokenName);
	console.log("CSRF value", csrfTokenValue);
	
	this.webSocketEndPoint = webSocketEndPoint;
	this.client = null;
	this.headers = [];
	this.headers[csrfTokenName] = csrfTokenValue;
	
	var wsocket = new SockJS("${webSocketEndPoint}");
	var client = Stomp.over(wsocket);
	
	this.subscriptions = [];
}

ConnectionManager.prototype.connect = function() {
	console.log("connect method called");
	
	var wsocket = new SockJS(this.webSocketEndPoint);
	this.client = Stomp.over(wsocket);
	
	var _self = this;
	
	this.client.connect(this.headers, function() { _self.connectSuccess() });
}

ConnectionManager.prototype.connectSuccess = function() {
	console.log("Established web socket connection");
	
	for(var i=0; i < this.subscriptions.length; i++) {
		var subscription = this.subscriptions[i];
		
		var inboundDestination = subscription.inboundDestination;
		var messageCallBack = subscription.newMessageCallBack;
		
		this.client.subscribe(inboundDestination, messageCallBack);
		console.log(inboundDestination, ":", messageCallBack);
	}
}

ConnectionManager.prototype.send = function(outboundDestination, message) {
	
	this.client.send(outboundDestination, this.eaders, JSON.stringify(message));
}

ConnectionManager.prototype.addSubscription = function(inboundDestination, newMessageCallBack) {
	
	this.subscriptions.push({
		"inboundDestination": inboundDestination,
		"newMessageCallBack": newMessageCallBack
	});
	console.log("addSubscription called");
}