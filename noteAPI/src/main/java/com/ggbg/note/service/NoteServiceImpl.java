package com.ggbg.note.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggbg.note.domain.dto.NoteDetailDTO;
import com.ggbg.note.domain.entity.NoteDetailEntity;
import com.ggbg.note.domain.entity.NoteEntity;
import com.ggbg.note.repository.AccountBandRepo;
import com.ggbg.note.repository.NoteDetailRepo;
import com.ggbg.note.repository.NoteRepo;
import com.ggbg.note.util.MapperUtil;

@Service
public class NoteServiceImpl implements INoteService{

	@Autowired
	private AccountBandRepo accountBandRepo;
	
	@Autowired
	private NoteDetailRepo noteDetailRepo;
	
	@Autowired
	private NoteRepo noteRepo;
	@Autowired
	private MapperUtil mapperUtil;
	
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
	public List<NoteDetailDTO> getNoteSubject(int accountNo, int bandNo) {
		boolean ret = verify(accountNo, bandNo);
		if(!ret)
			return new ArrayList<NoteDetailDTO>();
		
		NoteEntity entity = noteDetailRepo.findNoteDetailList(bandNo);
//		List<NoteDetailEntity> entityList = noteDetailRepo.findNoteDetailList(bandNo);
		List<NoteDetailDTO> DTOList = new ArrayList<NoteDetailDTO>();
		if(entity.getNote() != null) {
			for(NoteDetailEntity nde : entity.getNote()) {
				NoteDetailDTO ndd = mapperUtil.convertToDTO(nde, NoteDetailDTO.class);
				DTOList.add(ndd);
			}
		}
		
		return DTOList;
	}

	@Override
	public NoteDetailDTO getNoteContent(int accountNo, int bandNo, int noteNo) {
		boolean ret = verify(accountNo, bandNo);
		if(!ret)
			return new NoteDetailDTO();
		System.out.println("getNoteContent");
		NoteEntity ne = noteDetailRepo.findNoteDetailContent(bandNo, noteNo);
		if(ne.getNote() == null)
			return new NoteDetailDTO();
		else {
			NoteDetailDTO dto = mapperUtil.convertToDTO(ne.getNote(), NoteDetailDTO.class);
//			String str = ne.getNote().get(0).getContent();
			return dto;
		}
		
	}

	@Override
	public boolean updateNoteDetail(int accountNo, int bandNo, int noteNo, String subject, String content, int occupiedNo, String occupiedName) {
		boolean ret = verify(accountNo, bandNo);
		if(!ret)
			return false;
		System.out.println("updateNoteDetail");
//		noteRepo.removeNoteDetail(bandNo, noteNo);
		noteRepo.updateNoteDetail(bandNo, noteNo, subject, content, occupiedNo, occupiedName);
		
		return true;
	}
	
	@Override
	public boolean updateNoteDetailSubject(int accountNo, int bandNo, int noteNo, String subject) {
		boolean ret = verify(accountNo, bandNo);
		if(!ret)
			return false;	
		System.out.println("updateNoteDetailSubject");
	
		NoteEntity ne = noteDetailRepo.findNoteDetailContent(bandNo, noteNo);
		String content = "";
		int occupiedNo = -1;
		String occupiedName = "";
		if(ne.getNote() != null) {
			content = ne.getNote().get(0).getContent();
			occupiedNo = ne.getNote().get(0).getOccupiedNo();
			occupiedName = ne.getNote().get(0).getOccupiedName();
		}else {
			return false;
		}
	
		noteRepo.updateNoteDetail(bandNo, noteNo, subject, content, occupiedNo, occupiedName);

		return true;
	}

	@Override
	public boolean deleteNoteDetail(int accountNo, int bandNo, int noteNo) {
		boolean ret = verify(accountNo, bandNo);
		if(!ret)
			return false;
		System.out.println("deleteNoteDetail");
		noteRepo.removeNoteDetail(bandNo, noteNo);
		
		return true;
	}

	@Override
	public int insertNoteDetail(int accountNo, int bandNo, String subject, String content) {
		boolean ret = verify(accountNo, bandNo);
		if(!ret)
			return -1;
		
		int n1 = noteRepo.insertNoteDetail(bandNo, subject, content);
		return n1;
	}
//update는 현재 있는거 받아서 업데이트 시키면 되고 / 새로 저장하는건 새로 만들때 그냥 db에 넣어버리고
}
