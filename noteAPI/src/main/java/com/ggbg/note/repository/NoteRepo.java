package com.ggbg.note.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.ggbg.note.domain.entity.NoteDetailEntity;
import com.ggbg.note.domain.entity.NoteEntity;
import com.ggbg.note.domain.entity.NoteInfoEntity;
import com.mongodb.BasicDBObject;

@Repository
public class NoteRepo {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private NoteNoRepo noteNoRepo;
    
    public void insertNote(int bandNo) {
    	NoteEntity noteEntity = new NoteEntity(bandNo, null);
    	mongoTemplate.insert(noteEntity);
    }
    
    public void createNewNote(int bandNo) {
    	NoteEntity noteEntity = new NoteEntity(bandNo, null);
    	mongoTemplate.insert(noteEntity);
    	
    	NoteInfoEntity noteInfoEntity = new NoteInfoEntity(bandNo, 1);
    	mongoTemplate.insert(noteInfoEntity);
    }
    
    public void removeNote(int bandNo) {
    	NoteEntity noteEntity = new NoteEntity(bandNo, null);
    	mongoTemplate.remove(noteEntity);
    	
    	NoteInfoEntity noteInfoEntity = new NoteInfoEntity(bandNo, 0);
    	mongoTemplate.remove(noteInfoEntity);
    }
    
    public void removeNoteDetail(int bandNo, int noteNo) {
    	Query query = new Query().addCriteria(Criteria.where( "_id" ).is(bandNo) );
        Update update = new Update();
        update.pull("note", new BasicDBObject("_id", noteNo));
        mongoTemplate.updateFirst(query, update, NoteEntity.class);
    }
    
    public int insertNoteDetail(int bandNo, String subject, String content) {
    	int no = noteNoRepo.findNoteNo(bandNo).getNo();
    	
    	Query query = new Query().addCriteria(Criteria.where( "_id" ).is(bandNo) );
        Update update = new Update();
        update.inc("no", 1);  //증감시킬 숫자
        mongoTemplate.updateFirst(query, update, NoteInfoEntity.class);
    	
    	query = new Query(Criteria.where("_id").is(bandNo));
    	update = new Update();
    	Document item = new Document();
    	item.put("_id", no);
    	item.put("subject", subject);
    	item.put("content", content);
    	
    	update.push("note").each(item);
    	mongoTemplate.updateFirst(query, update, NoteEntity.class);
    	
    	return no;
    }
    
    public void updateNoteDetail(int bandNo, int noteNo, String subject, String content) {
    	Query query = new Query().addCriteria(Criteria.where( "_id" ).is(bandNo));
    	
    	Update update = new Update();
    	Document item = new Document();  //배열안에 담을 값 입니다.
        item.put("_id", noteNo);
        item.put("subject", subject);
        item.put("content", content);
        update.push("note").each(item);
        
    	mongoTemplate.updateFirst(query, update, NoteEntity.class);
    }
}


