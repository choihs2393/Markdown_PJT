package com.ggbg.note.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.ggbg.note.service.INoteService;
import com.ggbg.note.service.ISocketService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class InviteController {

	/*
	 * retMap 에 실패했으면 map.msg 에 fail 
	 * retMap 에 성공했으면 map.msg 에 success / map.noteDetailDTO 가 반환된다.
	 */
	
	@Autowired
	private ISocketService socketService;
	
	@Autowired
	private INoteService noteService;
	
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
	//		++ 4. subject/ 5. content도 보낼 것.
	@MessageMapping("/groupReceive/occupy/{bandNo}")
	@SendTo("/send/groupSend/occupy/{bandNo}")
	public Map<String, String> occupy(@DestinationVariable("bandNo") String bandNo, Map<String, String> map) {
		
		// NoteDetailRepo를 통해 할 작업은 아래와 같다.
		
		// map.get("noteId")값으로 Document에서 일치하는 _id로 접근하고,
		// 해당 row의 occupiedNo, occupiedName 값을 각각 map.get("account_no"), map.get("account_name")값으로 UPDATE 해준다.
	  
		/*
		 * input
		 */
		int bandId = Integer.parseInt(bandNo);
		int noteId = Integer.parseInt(map.get("noteId"));
		int accountNo = Integer.parseInt(map.get("accountNo"));
		String accountName = map.get("accountName");
		String subject = map.get("subject");
		String content = map.get("content");
		/*
		 * service
		 */
		Map<String, String> retMap = new HashMap<String, String>();
		retMap = socketService.occupy(accountNo, bandId, noteId, subject, content, accountName);
		
		// 그 결과, 업데이트된 row를 하나의 객체타입으로 리턴해준다.
		// 그럼, 프론트에서는 해당 row를 fileList에서 찾아 갈아 끼워주면 될 것이다.
		
		return retMap;
	}
	
	// 파라미터(map)에는 아래 세 정보가 포함되어 있음.
	// 		1. noteId
	//		2. account_no
	//		++ 3. subject/ 4. content도 보낼 것.
	@MessageMapping("/groupReceive/vacate/{bandNo}")
	@SendTo("/send/groupSend/vacate/{bandNo}")
	public Map<String, String> vacate(@DestinationVariable("bandNo") String bandNo, Map<String, String> map) {

		int noteId = Integer.parseInt(map.get("noteId"));
		int accountNo = Integer.parseInt(map.get("accountNo"));
		String subject = map.get("subject");
		String content = map.get("content");
		int bandId = Integer.parseInt(bandNo);
		
		// map.get("noteId")값으로 Document에서 일치하는 _id로 접근하고,
		// 해당 row의 occupiedNo, occupiedName 값을 각각 0, "" 값으로 UPDATE 해준다.
	
		// 그 결과, 업데이트된 row를 하나의 객체타입으로 리턴해준다.
		// 그럼, 프론트에서는 해당 row를 fileList에서 찾아 갈아 끼워주면 될 것이다.
		Map<String, String> retMap = new HashMap<String, String>();
		retMap = socketService.vacate(accountNo, bandId, noteId, subject, content);
		
		return retMap;
	}
	
	@MessageMapping("/groupReceive/content/{bandNo}/{noteNo}")
	@SendTo("/send/groupSend/content/{bandNo}")
	public Map<String, String> content(@DestinationVariable("bandNo") String bandNo, 
			@DestinationVariable("noteNo") String noteNo ,Map<String, String> map){
		int bandNo_ = Integer.parseInt(bandNo);
		int noteNo_ = Integer.parseInt(noteNo);
		int accountNo = Integer.parseInt(map.get("accountNo"));
		map.put("bandNo", bandNo);
		map.put("noteNo", noteNo);
		String subject = map.get("subject");
		String content = map.get("content");
		map.put("occupiedNo", map.get("accountNo"));
		String occupiedName = map.get("occupiedName");
		
		boolean ret = noteService.updateNoteDetail(accountNo, bandNo_, noteNo_, subject, content, accountNo, occupiedName);

		if(ret)
			map.put("msg", "success");
		else
			map.put("msg", "fail");
		return map;
	}
}