package com.ggbg.note.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggbg.note.domain.SuccessResponse;
import com.ggbg.note.domain.dto.NoteDetailDTO;
import com.ggbg.note.service.INoteService;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/note")
@RestController
public class NoteController {
	
	@Autowired
	private INoteService noteService;
	
	@ApiOperation(value = "getNoteSubject", httpMethod = "POST", notes = "Hello this is getNoteSubject")
	@PostMapping("/getNoteSubject")
	public ResponseEntity getNoteSubject(HttpServletRequest request, 
			@RequestBody(required = true) Map<String, String> map) {
		
		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();
		
		int bandNo = Integer.parseInt(map.get("bandNo"));
		int accountNo = Integer.parseInt(map.get("accountNo"));
		
		List<NoteDetailDTO> list = noteService.getNoteSubject(accountNo, bandNo);
		result.status = true;
		if(!list.isEmpty()) {
			result.result = "success";
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("noteDetailDTOList", list);
			result.map = retMap;
		} else {
			result.result = "empty";
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	} 
	
	@ApiOperation(value = "getNoteContent", httpMethod = "POST", notes = "Hello this is getNoteContent")
	@PostMapping("/getNoteContent")
	public ResponseEntity getNoteContent(HttpServletRequest request, 
			@RequestBody(required = true) Map<String, String> map) {
		
		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();
		
		int bandNo = Integer.parseInt(map.get("bandNo"));
		int accountNo = Integer.parseInt(map.get("accountNo"));
		int noteNo = Integer.parseInt(map.get("noteNo"));
		
		String content = noteService.getNoteContent(accountNo, bandNo, noteNo);
		
		result.status = true;
		if(content != null && !content.equals("")) {
			result.result = "success";
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("content", content);
			result.map = retMap;
		} else {
			result.result = "fail";
			result.result = "empty";
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	} 
	
	
	
	//새 글 추가
	@ApiOperation(value = "insertNoteDetail", httpMethod = "POST", notes = "Hello this is insertNoteDetail")
	@PostMapping("/insertNoteDetail") 
	public ResponseEntity insertNote(HttpServletRequest request, 
			@RequestBody(required = true) Map<String, String> map) {
		
		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();
		
		int accountNo = Integer.parseInt(map.get("accountNo"));
		int bandNo = Integer.parseInt(map.get("bandNo"));
		String subject = map.get("subject");
		int ret = noteService.insertNoteDetail(accountNo, bandNo, subject, "");
		
		result.status = true;
		if(ret != -1) {
			result.result = "success";
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("no", ret);
			result.map = retMap;
		} else {
			result.result = "fail";
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	} 
	
	//글 삭제
	@ApiOperation(value = "deleteNoteDetail", httpMethod = "POST", notes = "Hello this is deleteNoteDetail")
	@PostMapping("/deleteNoteDetail") 
	public ResponseEntity deleteNoteDetail(HttpServletRequest request, 
			@RequestBody(required = true) Map<String, String> map) {
		
		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();
		
		int accountNo = Integer.parseInt(map.get("accountNo"));
		int bandNo = Integer.parseInt(map.get("bandNo"));
		int noteNo = Integer.parseInt(map.get("noteNo"));
		boolean ret = noteService.deleteNoteDetail(accountNo, bandNo, noteNo);
		
		result.status = true;
		if(ret) {
			result.result = "success";
		} else {
			result.result = "fail";
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	} 
	
	@ApiOperation(value = "updateNoteDetail", httpMethod = "POST", notes = "Hello this is updateNoteDetail")
	@PostMapping("/updateNoteDetail")
	public ResponseEntity updateNoteDetail(HttpServletRequest request, 
			@RequestBody(required = true) Map<String, String> map) {
		
		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();
		
		int bandNo = Integer.parseInt(map.get("bandNo"));
		int accountNo = Integer.parseInt(map.get("accountNo"));
		int noteNo = Integer.parseInt(map.get("noteNo"));
		String subject = map.get("subject");
		String content = map.get("content");
		
		boolean ret = noteService.updateNoteDetail(accountNo, bandNo, noteNo, subject, content);
		
		result.status = true;
		if(ret) {
			result.result = "success";
		} else {
			result.result = "fail";
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	} 
	
	
	@ApiOperation(value = "updateNoteDetailSubject", httpMethod = "POST", notes = "Hello this is updateNoteDetailSubject")
	@PostMapping("/updateNoteDetailSubject")
	public ResponseEntity updateNoteDetailSubject(HttpServletRequest request, 
			@RequestBody(required = true) Map<String, String> map) {
		
		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();
		
		int bandNo = Integer.parseInt(map.get("bandNo"));
		int accountNo = Integer.parseInt(map.get("accountNo"));
		int noteNo = Integer.parseInt(map.get("noteNo"));
		String subject = map.get("subject");
		
		boolean ret = noteService.updateNoteDetailSubject(accountNo, bandNo, noteNo, subject);
		
		result.status = true;
		if(ret) {
			result.result = "success";
		} else {
			result.result = "fail";
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	}
}
