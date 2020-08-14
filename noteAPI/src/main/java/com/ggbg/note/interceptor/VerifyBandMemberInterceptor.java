package com.ggbg.note.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggbg.note.domain.dto.NoteViewDTO;

@Component
public class VerifyBandMemberInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("v2 handler");
		try {
			NoteViewDTO noteViewDTO= new ObjectMapper().readValue(request.getInputStream(), NoteViewDTO.class);
			System.out.println(noteViewDTO.getAccountNo());
			System.out.println(noteViewDTO.getBandNo());
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
