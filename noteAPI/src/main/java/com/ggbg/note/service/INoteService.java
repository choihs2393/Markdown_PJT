package com.ggbg.note.service;

import java.util.List;

import com.ggbg.note.domain.dto.NoteDetailDTO;

public interface INoteService {
	/*
	 * 
	 * account no / band no -> 글 no /글 제목 넘겨주고
	 * account no / band no / 글 no -> 글 내용
	 * account no / band no / 글 no / 수정된 글 제목 / 수정된 글 내용 -> success fail
	 * 
	 * delete 
	 * account no / band no / 글 no -> 삭제 하고 -> 글 no / 글제목 -> success fail
	 * 
	 * insert
	 * account no / band no / 제목 / 내용
	 * 
	 */
	
	//select
	public List<NoteDetailDTO> getNoteSubject(int accountNo, int bandNo);
	public NoteDetailDTO getNoteContent(int accountNo, int bandNo, int noteNo);
	
	//udpate
	public boolean updateNoteDetail(int accountNo, int bandNo, int noteNo, String subject, String content, int occupiedNo, String occupiedName);
	public boolean updateNoteDetailSubject(int accountNo, int bandNo, int noteNo, String subject);
	
	//delete
	public boolean deleteNoteDetail(int accountNo, int bandNo, int noteNo);
	
	//insert
	public int insertNoteDetail(int accountNo, int bandNo, String subject, String content);
}
