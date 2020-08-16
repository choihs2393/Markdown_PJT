package com.ggbg.note.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("note")
@Data
public class NoteDetailEntity {
	@Id
	private int _id;
	private String subject;
	public String content;
	public int occupiedNo;
	public String occupiedName;
}
