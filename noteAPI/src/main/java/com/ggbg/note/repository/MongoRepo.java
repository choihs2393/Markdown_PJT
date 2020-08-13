package com.ggbg.note.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.ggbg.note.domain.entity.NoteDetailEntity;
import com.ggbg.note.domain.entity.NoteEntity;

@Repository
public class MongoRepo {
    @Autowired
    private MongoTemplate mongoTemplate;
    
    public List<NoteEntity> getInfo() {
//    	NoteEntity noteEntity = new NoteEntity(2, null);
//    	mongoTemplate.insert(noteEntity);
    	
    	Query query = new Query(Criteria.where("_id").is(1));
    	return mongoTemplate.findAll(NoteEntity.class, "note");
    }
    
    public NoteDetailEntity getDetail() {
    	Query query = new Query(Criteria.where("_id").is(1)
    								.where("note")
    								.where("subject").is(true)
    							);
    	return mongoTemplate.findOne(query, NoteDetailEntity.class, "note");
    }
}


