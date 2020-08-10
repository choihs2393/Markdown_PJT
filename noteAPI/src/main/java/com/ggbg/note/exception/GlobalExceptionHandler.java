package com.ggbg.note.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ggbg.note.domain.ExceptionResponse;
import com.ggbg.note.util.HttpsConnection;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@Autowired
	HttpsConnection httpConnection;

	@ExceptionHandler(UnAuthorizationException.class)
	protected ResponseEntity UnAuthorizationException(UnAuthorizationException e) {
		ResponseEntity response = null;
		
		final ExceptionResponse result = new ExceptionResponse();
		
		result.code = ErrorCode.Unauthorized_Account.getCode();
		result.message = ErrorCode.Unauthorized_Account.getMessage() + " : " + e.getMessage();
		result.status = ErrorCode.Unauthorized_Account.getStatus();
		response = new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		
		httpConnection.postRequest(ErrorCode.Unauthorized_Account.getMessage(), e.getMessage());
		
		return response;
	}
	
	@ExceptionHandler(ExpiredTokenException.class)
	protected ResponseEntity ExpiredTokenException(ExpiredTokenException e) {
		ResponseEntity response = null;
		
		final ExceptionResponse result = new ExceptionResponse();
		
		result.code = ErrorCode.ExpiredToken.getCode();
		result.message = ErrorCode.ExpiredToken.getMessage() + " : " + e.getMessage();
		result.status = ErrorCode.ExpiredToken.getStatus();
		response = new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
		
		httpConnection.postRequest(ErrorCode.ExpiredToken.getMessage(), e.getMessage());

		return response;
	}
	
	@ExceptionHandler(UnknownException.class)
	protected ResponseEntity UnknownException(UnknownException e) {
		ResponseEntity response = null;
		
		final ExceptionResponse result = new ExceptionResponse();
		
		result.code = ErrorCode.Unknown.getCode();
		result.message = ErrorCode.Unknown.getMessage() + " : " + e.getMessage();
		result.status = ErrorCode.Unknown.getStatus();
		response = new ResponseEntity<>(result, HttpStatus.SERVICE_UNAVAILABLE);
		
		httpConnection.postRequest(ErrorCode.Unknown.getMessage(), e.getMessage());
		
		return response;
	}
	
	@ExceptionHandler(InternalServerException.class)
	protected ResponseEntity InternalServerException(InternalServerException e) {
		ResponseEntity response = null;
		
		final ExceptionResponse result = new ExceptionResponse();
		
		result.code = ErrorCode.InternalServer.getCode();
		result.message = ErrorCode.InternalServer.getMessage() + " : " + e.getMessage();
		result.status = ErrorCode.InternalServer.getStatus();
		response = new ResponseEntity<>(result, HttpStatus.SERVICE_UNAVAILABLE);
		
		httpConnection.postRequest(ErrorCode.InternalServer.getMessage(), e.getMessage());

		return response;
	}

}
