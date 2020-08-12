package com.ggbg.note.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.ggbg.note.service.IAccountService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class InviteController {

	@Autowired
	private IAccountService accountService;
	
	@MessageMapping("/ws/receive")
	@SendTo("/send/{toNo}")
//	public Map<String, String> getIntivationList(@RequestBody(required = true) Map<String, String> map) {
	public Map<String, String> getIntivationList(@DestinationVariable("toNo") String toNo, @RequestBody(required = true) Map<String, String> map) {

		return map;
	}
}