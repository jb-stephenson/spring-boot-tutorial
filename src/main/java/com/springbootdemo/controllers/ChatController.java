package com.springbootdemo.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.springbootdemo.model.dto.ChatRequest;
import com.springbootdemo.model.dto.SimpleMessage;
import com.springbootdemo.model.entity.Message;
import com.springbootdemo.model.entity.SiteUser;
import com.springbootdemo.service.MessageService;
import com.springbootdemo.service.UserService;

@Controller
public class ChatController {
	
	@Autowired
	private SimpMessagingTemplate simpleMessagingTemplate;
	
	@Autowired
	private Util util;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;	

	@RequestMapping("/markread")
	String markRead(ModelAndView modelAndView, @RequestParam("f") long userId, @RequestParam("m") long messageId) {

		Optional<Message> messageOptional = messageService.get(messageId);
		
		if(messageOptional.isPresent()) {
			Message message = messageOptional.get();
			message.setRead(true);
			messageService.save(message);
		}
		
		return "redirect:/chatview/" + userId;
	}
	
	@RequestMapping("/messages")
	ModelAndView checkMessages(ModelAndView modelAndView, @RequestParam("p") int pageNumber) {

		SiteUser user = util.getUser();
		Page<SimpleMessage> messages = messageService.fetchMessageListPage(user.getId(), pageNumber);

		modelAndView.getModel().put("messageList", messages);
		modelAndView.getModel().put("pageNumber", pageNumber);
		modelAndView.getModel().put("userId", user.getId());

		modelAndView.setViewName("app.checkmessages");
		return modelAndView;
	}
	
	@RequestMapping(value="/conversation/{otherUserId}", method=RequestMethod.POST, produces="application/json")
	@ResponseBody
	List<SimpleMessage> fetchConversation(@PathVariable(name="otherUserId") Long otherUserId, @RequestBody ChatRequest request) {
		
		SiteUser thisUser = util.getUser();
		
		List<SimpleMessage> list = messageService.fetchConversation(thisUser.getId(), otherUserId, request.getPage());
		
		return list;
	}
	
	@RequestMapping("/chatview/{chatWithUserID}")
	ModelAndView chatView(ModelAndView modelAndView, @PathVariable Long chatWithUserID) {
		
		SiteUser thisUser = util.getUser();
		String chatWithUserName = userService.getUserName(chatWithUserID);
		
		modelAndView.setViewName("chat.chatview");
		modelAndView.getModel().put("thisUserID", thisUser.getId());
		modelAndView.getModel().put("chatWithUserID", chatWithUserID);
		modelAndView.getModel().put("chatWithUserName", chatWithUserName);
		
		return modelAndView;
	}
	
	
	@MessageMapping("/message/send/{toUserID}")
	public void send(Principal principal, SimpleMessage message, @DestinationVariable Long toUserID) {

		//Get details for the sending user (current user)
		String fromUsername = principal.getName();
		SiteUser fromUser = userService.get(fromUsername);
		Long fromUserId = fromUser.getId();
		
		//Get details for the user we are chatting with
		Optional<SiteUser> userOptional = userService.get(toUserID);
		SiteUser toUser = userOptional.get();
		String toUserName = toUser.getEmail();
		
		String returnReceiptQueue = String.format("/queue/%d", toUserID);
		String toUserQueue = String.format("/queue/%d", fromUserId);
		
		messageService.save(fromUser, toUser, message.getText());
		
		//Sending message to user who sent the message
		message.setIsReply(false);
		simpleMessagingTemplate.convertAndSendToUser(fromUsername, returnReceiptQueue, message);
		
		message.setFrom(fromUser.getFirstname() + " " + fromUser.getSurname());
		
		//Sending message to desired user
		message.setIsReply(true);
		simpleMessagingTemplate.convertAndSendToUser(toUserName, toUserQueue, message);
		
		//Send a new message notification
		simpleMessagingTemplate.convertAndSendToUser(toUserName, "/queue/newmessages", message);
	}
}