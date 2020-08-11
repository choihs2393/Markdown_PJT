package com.ggbg.note.domain.dto;

import com.ggbg.note.domain.Role;

import lombok.Builder;
import lombok.Data;

@Data
public class AccountDTO {
private int no;
	
	private String email;
	private String name;
	private String password;
	private Role role;
	private String createDate;
	
}
