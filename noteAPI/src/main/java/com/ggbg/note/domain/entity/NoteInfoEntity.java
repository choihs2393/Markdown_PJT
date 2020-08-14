package com.ggbg.note.domain.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("noteNo")
@Data
public class NoteInfoEntity {
	private int _id;
	private int no;
	public NoteInfoEntity(int _id, int no) {
		this._id = _id;
		this.no = no;
	}
}
