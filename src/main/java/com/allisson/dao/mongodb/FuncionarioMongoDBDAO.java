package com.allisson.dao.mongodb;


import org.bson.Document;
import org.bson.types.ObjectId;

import com.allisson.dao.FuncionarioDAO;
import com.allisson.model.Funcionario;
import com.allisson.mongodb.MongoDBUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public abstract class FuncionarioMongoDBDAO<T extends Funcionario> implements FuncionarioDAO<T> {

	protected MongoDatabase db;
	protected MongoCollection<Document> collection;
	
	public FuncionarioMongoDBDAO(String tableName) {
		this.db = MongoDBUtil.getDatabase();
		this.collection = db.getCollection(tableName);
	}
	
	public void insert(T t) {
		
		Document doc = t.toDocument();
		this.collection.insertOne(doc);
		t.setId((ObjectId)doc.get("_id"));
	}

	public void update(T t) {
		Document update = new Document();
		update.append("$set", t.toDocument());
		
		BasicDBObject query = new BasicDBObject();
	    query.put("_id", new ObjectId(t.getId().toString()));
		
		collection.updateOne(query, update);
	}

	public void delete(T t) {
		delete(t.getId());
	}

	
	@Override
	public void delete(Object id) {
		BasicDBObject query = new BasicDBObject();
	    query.put("_id", new ObjectId(id.toString()));
		collection.deleteOne(query);
	}

	
	@Override
	public void close() {
		MongoDBUtil.closeConnection(db);
	}

}
