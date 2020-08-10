package com.ggbg.note.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggbg.note.domain.SuccessResponse;
import com.ggbg.note.domain.dto.BandMemberDTO;
import com.ggbg.note.domain.dto.PrimitiveAccountDTO;
import com.ggbg.note.service.IAccountBandService;
import com.ggbg.note.service.INonMemberService;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/accountBand")
@RestController
public class AccountBandController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private IAccountBandService accountBandService;

	@ApiOperation(value = "getBandMember", httpMethod = "POST", notes = "Hello this is getBandMember")
	@PostMapping("/v1/getBandMember")
	public ResponseEntity getBandMember(HttpServletRequest request, 
			@RequestBody(required = true) Map<String, String> map) {

		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();

		int bandNo = Integer.parseInt(map.get("bandNo"));
		
		List<BandMemberDTO> bandMemberList = accountBandService.getBandMember(bandNo);
		
		result.status = true;
		if(!bandMemberList.isEmpty()) {
			result.result = "success";
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("bandMemberList", bandMemberList);
			result.map = retMap;
		}else {
			result.result = "empty";
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	} // 만약 Unauthorized가 뜨면 access token 이 변조된것이다. 로그아웃 시켜야함.
	
	
	@ApiOperation(value = "inviteBandMember", httpMethod = "POST", notes = "Hello this is inviteBandMember")
	@PostMapping("/v1/inviteBandMember")
	public ResponseEntity inviteBandMember(HttpServletRequest request, 
			@RequestBody(required = true) Map<String, String> map) {

		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();

		String accessToken = request.getHeader("Authorization").substring(7);
		String email = request.getHeader("email");
		int bandNo = Integer.parseInt(map.get("bandNo"));
		int accountNo = Integer.parseInt(map.get("accountNo"));
		
		BandMemberDTO bandMemberDTO = accountBandService.inviteBandMember(email, bandNo, accountNo);
		
		result.status = true;
		if(bandMemberDTO != null) {
			result.result = "success";
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("bandMember", bandMemberDTO);
			result.map = retMap;
		}else {
			result.result = "fail";
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	} // 만약 Unauthorized가 뜨면 access token 이 변조된것이다. 로그아웃 시켜야함.
	
	@ApiOperation(value = "findAccountList", httpMethod = "POST", notes = "Hello this is findAccountList")
	@PostMapping("/findAccountList")
	public ResponseEntity findAccountList(HttpServletRequest request, 
			@RequestBody(required = true) Map<String, String> map) {

		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();

		List<PrimitiveAccountDTO> primitiveAccountList = accountBandService.findAccountList(map.get("email"));
		
		result.status = true;
		if (!primitiveAccountList.isEmpty()) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("primitiveAccountList", primitiveAccountList);
			result.result = "success";
			result.map = retMap;
		} else {
			result.result ="empty";
			
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	} // 만약 Unauthorized가 뜨면 access token 이 변조된것이다. 로그아웃 시켜야함.
}
