package com.ggbg.note.service;

import java.util.Map;

import com.ggbg.note.domain.dto.NoteDetailDTO;

public interface ISocketService {
	public Map<String, Object> occupy(int accountNo, int bandNo, int noteNo, String subject, String content, String occupiedName); // 혹시 갖고오는 부분에 boolean 처리를 해서 실패했는지 성공했는지에 대해서도 알려줘야하는지???
	public Map<String, Object> vacate(int accountNo, int bandNo, int noteNo, String subject, String content);
}
