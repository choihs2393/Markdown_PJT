package com.ggbg.note.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.ggbg.note.domain.entity.NoteEntity;
import com.ggbg.note.domain.entity.NoteInfoEntity;

public interface NoteNoRepo extends MongoRepository<NoteInfoEntity, Integer> {
	
	@Query(value = "{'_id' : ?0}")
	NoteInfoEntity findNoteNo(int bandNo);
}
