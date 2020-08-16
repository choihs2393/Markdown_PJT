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

	@MessageMapping("/receive/{toNo}")
	@SendTo("/send/{toNo}")
//	public Map<String, String> getIntivationList(@RequestBody(required = true) Map<String, String> map) {
	public Map<String, String> getIntivationList(@DestinationVariable("toNo") String toNo, Map<String, String> map) {
		System.out.println(map);
		System.out.println(map.get("fromEmail"));
		System.out.println(map.get("fromName"));
		System.out.println(map.get("toEmail"));
		System.out.println(map.get("toNo"));
		System.out.println(map.get("groupName"));
		System.out.println(map.get("groupNo"));
		
		return map;
	}
	
	// 파라미터(map)에는 아래 세 정보가 포함되어 있음.
	// 		1. noteId
	//		2. account_no
	//		3. account_name
	@MessageMapping("/groupReceive/occupy/{bandNo}")
	@SendTo("/groupSend/{bandNo}")
	public void occupy(@DestinationVariable("bandNo") String bandNo, Map<String, String> map) {
		
		// NoteDetailRepo를 통해 할 작업은 아래와 같다.
		
		// map.get("noteId")값으로 Document에서 일치하는 _id로 접근하고,
		// 해당 row의 occupiedNo, occupiedName 값을 각각 map.get("account_no"), map.get("account_name")값으로 UPDATE 해준다.
	
		// 그 결과, 업데이트된 row를 하나의 객체타입으로 리턴해준다.
		// 그럼, 프론트에서는 해당 row를 fileList에서 찾아 갈아 끼워주면 될 것이다.
	}
	
	// 파라미터(map)에는 아래 세 정보가 포함되어 있음.
	// 		1. noteId
	//		2. account_no
	//		3. account_name
	@MessageMapping("/groupReceive/vacate/{bandNo}")
	@SendTo("/groupSend/{bandNo}")
	public void vacate(@DestinationVariable("bandNo") String bandNo, Map<String, String> map) {
		
		// NoteDetailRepo를 통해 할 작업은 아래와 같다.
		
		// map.get("noteId")값으로 Document에서 일치하는 _id로 접근하고,
		// 해당 row의 occupiedNo, occupiedName 값을 각각 0, "" 값으로 UPDATE 해준다.
	
		// 그 결과, 업데이트된 row를 하나의 객체타입으로 리턴해준다.
		// 그럼, 프론트에서는 해당 row를 fileList에서 찾아 갈아 끼워주면 될 것이다.
	}
}