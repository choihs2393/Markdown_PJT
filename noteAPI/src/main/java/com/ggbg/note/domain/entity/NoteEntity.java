package com.ggbg.note.domain.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("note")
@Data
public class NoteEntity {
	@Id
	private int _id;
	private List<NoteDetailEntity> note;
	public NoteEntity(int _id, List<NoteDetailEntity> note) {
		this._id = _id;
		this.note = note;
	}
}
