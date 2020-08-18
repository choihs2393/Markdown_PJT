package com.ggbg.note.domain.dto;

import lombok.Data;

@Data
public class NoteDetailDTO {
	private int _id;
	private String subject;
	public String content;
	public int occupiedNo;
	public String occupiedName;
	
	
	public NoteDetailDTO(int _id, String subject, String content, int occupiedNo, String occupiedName) {
		this._id = _id;
		this.subject = subject;
		this.content = content;
		this.occupiedNo = occupiedNo;
		this.occupiedName = occupiedName;
	}


	public NoteDetailDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
