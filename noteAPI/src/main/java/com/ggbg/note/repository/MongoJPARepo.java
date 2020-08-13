package com.ggbg.note.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.ggbg.note.domain.entity.NoteEntity;

public interface MongoJPARepo extends MongoRepository<NoteEntity, Integer>{
	@Query(value = "({ '_id': ?0 })", fields = "{'_id' : true}") 
	NoteEntity findBynote(int _id);
}
