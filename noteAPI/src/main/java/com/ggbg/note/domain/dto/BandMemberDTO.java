package com.ggbg.note.domain.dto;

import lombok.Data;

@Data
public class BandMemberDTO {
	private int no;
	private String email;
	private String name;
	private int status;
	
	public BandMemberDTO() {}
	
	public BandMemberDTO(int no, String email, String name, int status) {
		this.no = no;
		this.email = email;
		this.name = name;
		this.status = status;
	}
}
