package com.ggbg.note.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class NoteDTO {
	private int _id;
	private List<String> note;
}
