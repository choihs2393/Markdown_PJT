package com.ggbg.note.domain.dto;

import lombok.Data;

@Data
public class NoteDetailDTO {
	private int _id;
	private String subject;
	public String content;
}
