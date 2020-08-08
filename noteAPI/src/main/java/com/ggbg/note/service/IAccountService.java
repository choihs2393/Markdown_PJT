package com.ggbg.note.service;

import com.ggbg.note.bean.Account;

public interface IAccountService {
//	// authentication
//	public Authentication getAuthentication(String token);
	// logout process
	public String logout(String email);
	
	//modify account
	public boolean validAccountCheck(String email, String password);
	public boolean saveAccount(Account account);
	
	//delete account
	public boolean deleteAccount(String email);

	//init
	public String onLocalInit(String accessToken);

}
