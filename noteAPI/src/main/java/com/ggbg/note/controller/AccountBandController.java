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
	
	@ApiOperation(value = "deleteMember", httpMethod = "POST", notes = "Hello this is deleteMember")
	@PostMapping("/v1/deleteMember")
	public ResponseEntity deleteMember(HttpServletRequest request, 
			@RequestBody(required = true) Map<String, String> map) {

		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();

		int masterNo = Integer.parseInt(map.get("masterNo"));
		int accountNo = Integer.parseInt(map.get("accountNo"));
		int bandNo = Integer.parseInt(map.get("bandNo"));
		boolean ret = accountBandService.deleteMember(masterNo, accountNo, bandNo);
		
		result.status = true;
		if (ret) {
			result.result = "success";
		} else {
			result.result ="fail";
			
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

		int masterNo = Integer.parseInt(map.get("masterNo"));
		int bandNo = Integer.parseInt(map.get("bandNo"));
		String email = map.get("email");
		
		BandMemberDTO bandMemberDTO = accountBandService.inviteBandMember(email, masterNo, bandNo);
		
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
	
	@ApiOperation(value = "acceptInvite", httpMethod = "POST", notes = "Hello this is acceptInvite")
	@PostMapping("/v1/acceptInvite")
	public ResponseEntity acceptInvite(HttpServletRequest request, 
			@RequestBody(required = true) Map<String, String> map) {

		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();

		int accountNo = Integer.parseInt(map.get("accountNo"));
		int bandNo = Integer.parseInt(map.get("bandNo"));
		boolean ret = accountBandService.acceptInvite(accountNo, bandNo);
		
		result.status = true;
		if (ret) {
			result.result = "success";
		} else {
			result.result ="fail"; // false 가 나오는경우는 업데이트가 안된경우 -> 그럼 얘는 탈퇴를 했다는것 -> 목록에서 지워버리면됨
			
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	} // 만약 Unauthorized가 뜨면 access token 이 변조된것이다. 로그아웃 시켜야함.
	
	@ApiOperation(value = "declineInvite", httpMethod = "POST", notes = "Hello this is declineInvite")
	@PostMapping("/v1/declineInvite")
	public ResponseEntity declineInvite(HttpServletRequest request, 
			@RequestBody(required = true) Map<String, String> map) {

		ResponseEntity response = null;
		final SuccessResponse result = new SuccessResponse();

		int accountNo = Integer.parseInt(map.get("accountNo"));
		int bandNo = Integer.parseInt(map.get("bandNo"));
		boolean ret = accountBandService.declineInvite(accountNo, bandNo);
		
		result.status = true;
		if (ret) {
			result.result = "success";
		} else {
			result.result ="fail";// false 가 나오는경우는 지울게 없는경우 -> 그럼 그냥 지워버리면 됨
		}
		response = new ResponseEntity<>(result, HttpStatus.OK);
		return response;
	} // 만약 Unauthorized가 뜨면 access token 이 변조된것이다. 로그아웃 시켜야함.
}
