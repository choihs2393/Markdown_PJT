package com.ggbg.note.service;

import com.ggbg.note.bean.Account;

public interface INonMemberService {
	// signUp process
	public String signUp(Account account);

	public String emailCheck(String email);

	public String emailAuthSend(String email);

	public String emailAuthCheck(String email, String authNum);

	
	
}
