package com.zliang;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoDBLocalTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Mongo mongoClient = null;
		DB db = null;
		try {
			mongoClient = new Mongo( "localhost" , 27017 );
			db = mongoClient.getDB("mydb");
			
			//get collection name list
			Set<String> collNames = db.getCollectionNames();
			for(String colName : collNames){
				System.out.println(colName);
			}
			
			//get collection by name
			DBCollection inst3Coll = db.getCollection("inst3");
			DBCollection collection = inst3Coll;
			long count = collection.count();
			System.out.println("testData count = "+count);
			System.out.println("<<<<<<<<<<<<<<<<<<<<<################################>>>>>>>>>>>>>>>>>>>>>>");
			
			//query collection
			DBCursor cursor = collection.find();
			for (Iterator<DBObject> iterator = cursor.iterator(); iterator.hasNext();) {
				DBObject dbObject = iterator.next();
				System.out.println(dbObject);
			}
			
			//query the first line
			System.out.println("//////////////   first one");
			DBObject one = collection.findOne();
			System.out.println(one);
			BSONTimestamp ts = (BSONTimestamp) one.get("t");
			ObjectId _id = (ObjectId) one.get("_id");
			Date d = (Date) one.get("d");
			System.out.println(one.get("t"));
			
			System.out.println("//////////////   query");
			BasicDBObject query = new BasicDBObject("x", 10);
			cursor = collection.find(query);
			for (Iterator<DBObject> it = cursor.iterator(); it.hasNext();) {
				DBObject dbo = it.next();
				System.out.println(dbo);
			}
			
			System.out.println("//////////////   query scope 20< x <=23");
			query = new BasicDBObject("x", new BasicDBObject("$gt",20).append("$lte",23));
			cursor = collection.find(query);
			while (cursor.hasNext()) {
				DBObject dbObject = (DBObject) cursor.next();
				System.out.println(dbObject);
			}
			
			System.out.println("//////////////   query scope d > 2014-3-1");
			Date date = null;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse("2014-3-1");
				query = new BasicDBObject("d",new BasicDBObject("$gt",date));
				cursor = collection.find(query);
				while(cursor.hasNext()){
					DBObject dbObject = (DBObject) cursor.next();
					System.out.println(dbObject);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			System.out.println("//////////////   query scope ts > now");
			try {
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2014-3-1 7:30:00.111");
				Timestamp now = new Timestamp(new Date().getTime());
				query = new BasicDBObject("t",new BasicDBObject("$gt",now));
				cursor = collection.find(query);
				while(cursor.hasNext()){
					DBObject dbObject = (DBObject) cursor.next();
					System.out.println(dbObject);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			query = new BasicDBObject("x", new BasicDBObject("$gt",20).append("$lte",23));
			cursor = collection.find(query);
			while (cursor.hasNext()) {
				DBObject dbObject = (DBObject) cursor.next();
				System.out.println(dbObject);
			}
			
			cursor.close();
			
			System.out.println("//////////////   Creating An Index");
			collection.createIndex(new BasicDBObject("x",1));
			
			System.out.println("//////////////   iterator index list");
			List<DBObject> indexList = collection.getIndexInfo();
			for (DBObject dbObject : indexList) {
				System.out.println(dbObject);
			}
			
			System.out.println("//////////////   drop a database");
//			mongoClient.dropDatabase("mydb");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally{
			if(mongoClient!=null){
				mongoClient.close();
			}
		}
		
		/*// To directly connect to a single MongoDB server (note that this will not auto-discover the primary even
		// if it's a member of a replica set:
		MongoClient mongoClient = new MongoClient();
		// or
		MongoClient mongoClient = new MongoClient( "C0007294.itcs.hp.com" );
		// or
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		// or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
		MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
		                                      new ServerAddress("localhost", 27018),
		                                      new ServerAddress("localhost", 27019)));

		DB db = mongoClient.getDB( "mydb" );
		
		Set<String> colls = db.getCollectionNames();

		for (String s : colls) {
		    System.out.println(s);
		}*/
	}

}
