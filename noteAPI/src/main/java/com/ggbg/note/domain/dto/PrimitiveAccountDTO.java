package com.ggbg.note.domain.dto;

import lombok.Data;

@Data
public class PrimitiveAccountDTO {
	int no;
	String email;
	String name;
	
	public PrimitiveAccountDTO() {
	}
	
	public PrimitiveAccountDTO(int no, String email, String name) {
		this.no = no;
		this.email = email;
		this.name = name;
	}
	
	
}
