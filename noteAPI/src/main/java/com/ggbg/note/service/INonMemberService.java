package com.ggbg.note.service;

import com.ggbg.note.domain.dto.AccountDTO;

public interface INonMemberService {
	// signUp process
	public boolean signUp(AccountDTO accountDTO);

	public boolean emailCheck(String email);

	public boolean emailAuthSend(String email);

	public boolean emailAuthCheck(String email, String authNum);

}
