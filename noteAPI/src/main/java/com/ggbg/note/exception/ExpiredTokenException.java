package com.ggbg.note.exception;

public class ExpiredTokenException extends RuntimeException{
	public ExpiredTokenException(String msg) {
		super(msg);
	}
}
