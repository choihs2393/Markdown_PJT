package com.ggbg.note.service;

import java.util.List;
import java.util.Map;

import com.ggbg.note.bean.Account;
import com.ggbg.note.bean.Band;

public interface IAccountService {
//	// authentication
//	public Authentication getAuthentication(String token);
	// logout process
	public String logout(String email);
	
	//modify account
	public boolean validAccountCheck(String email, String password);
	public boolean saveAccount(Account account);
	public boolean deleteAccount(String email);

	//init
	public Map<String, Object> onLocalInit(String accessToken);
	public Map<String, Object> onServerInit(String accessToken);
	
	//statusList
	public List<Band> statusList(String accessToken);
}
