package com.ggbg.note.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggbg.note.domain.dto.NoteDetailDTO;
import com.ggbg.note.repository.AccountBandRepo;
import com.ggbg.note.repository.NoteDetailRepo;
import com.ggbg.note.repository.NoteRepo;
import com.ggbg.note.util.MapperUtil;

@Service
public class SocketServiceImpl implements ISocketService{
	@Autowired
	private AccountBandRepo accountBandRepo;
	
	@Autowired
	private NoteDetailRepo noteDetailRepo;
	
	@Autowired
	private NoteRepo noteRepo;
	@Autowired
	private MapperUtil mapperUtill;
	
	
	public boolean verify(int accountNo, int bandNo) {
		//db 들러서 해당 그룹에 있는 사람이 맞는지 확인
		Integer ret = -1;
		ret = accountBandRepo.findAccountBandToVerify(accountNo, bandNo);
		if(ret == -1 || ret == 0 || ret == null)
			return false;
		else
			return true;
	}
	
	@Override
	public Map<String, String> occupy(int accountNo, int bandNo, int noteNo, String subject, String content,
			String occupiedName) {
		Map<String, String> map = new HashMap<String, String>();
		
		boolean ret = verify(accountNo, bandNo);
		if(!ret) {
			map.put("msg", "fail");
			return map;
		}
		noteRepo.updateNoteDetail(bandNo, noteNo, subject, content, accountNo, occupiedName);
//		NoteDetailDTO noteDetailDTO = new NoteDetailDTO(noteNo, subject, content, accountNo, occupiedName);
		map.put("noteNo", Integer.toString(noteNo));
		map.put("subject", subject);
		map.put("content", content);
		map.put("occupiedNo", Integer.toString(accountNo));
		map.put("occupiedName", occupiedName);
		
		return map;
	}
	@Override
	public Map<String, String> vacate(int accountNo, int bandNo, int noteNo, String subject, String content) {
		Map<String, String> map = new HashMap<String, String>();
		
		boolean ret = verify(accountNo, bandNo);
		if(!ret) {
			map.put("msg", "fail");
			return map;
		}
		noteRepo.updateNoteDetail(bandNo, noteNo, subject, content, 0, "");
		NoteDetailDTO noteDetailDTO = new NoteDetailDTO(noteNo, subject, content, 0, "");
//		map.put("noteDetailDTO", noteDetailDTO);
		map.put("noteNo", Integer.toString(noteNo));
		map.put("subject", subject);
		map.put("content", content);
		map.put("occupiedNo", "0");
		map.put("occupiedName", "");
		
		return map;
	}

	
}
