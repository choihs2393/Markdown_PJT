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
import com.ggbg.note.domain.entity.NoteEntity;
import com.ggbg.note.repository.MongoJPARepo;
import com.ggbg.note.repository.MongoRepo;
import com.ggbg.note.util.MapperUtil;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/test")
@RestController
public class NoteController {
	
	@Autowired
	private MongoRepo mongoRepo;
	@Autowired
	private MapperUtil util;
	@Autowired
	private MongoJPARepo mongoJPARepo;
	/*
	 * 
	 * account no / band no -> 글 no /글 제목 넘겨주고
	 * account no / band no / 글 no -> 글 내용
	 * account no / band no / 글 no / 수정된 글 제목 / 수정된 글 내용 -> success fail
	 * 
	 * delete 
	 * account no / band no / 글 no -> 삭제 하고 -> 글 no / 글제목 -> success fail
	 * 
	 * insert
	 * account no / band no / 제목 / 내용
	 * 
	 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	@ApiOperation(value = "getBandMember", httpMethod = "POST", notes = "Hello this is getBandMember")
	@PostMapping("/insert")
	public ResponseEntity getBandMember(HttpServletRequest request, 
			@RequestBody(required = true) Map<String, String> map) {
		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();
//		List<NoteEntity> noteEntity = mongoRepo.getInfo();
//		NoteDetailEntity noteDetailEntity = mongoRepo.getDetail();
		NoteEntity noteEntity = mongoJPARepo.findBynote(2);
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("noteDTO", noteEntity);
		result.map = retMap;
		result.result = "success";
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	} // 만약 Unauthorized가 뜨면 acc
}
