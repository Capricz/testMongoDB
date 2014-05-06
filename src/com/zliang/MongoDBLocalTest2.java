package com.zliang;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoDBLocalTest2 {

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
			DBCollection testDataColl = db.getCollection("testData");
			long count = testDataColl.count();
			System.out.println("testData count = "+count);
			System.out.println("<<<<<<<<<<<<<<<<<<<<<################################>>>>>>>>>>>>>>>>>>>>>>");
			
			//query PA collection
			/*DBCursor cursor = paColl.find();
			for (Iterator<DBObject> iterator = cursor.iterator(); iterator.hasNext();) {
				DBObject dbObject = iterator.next();
				System.out.println(dbObject);
			}*/
			
			//query the first line
			System.out.println("//////////////   first one");
			DBObject one = testDataColl.findOne();
			System.out.println(one);
			
			System.out.println("//////////////   query");
			BasicDBObject query = new BasicDBObject("x", 10);
			DBCursor cursor = testDataColl.find(query);
			for (Iterator<DBObject> it = cursor.iterator(); it.hasNext();) {
				DBObject dbo = it.next();
				System.out.println(dbo);
			}
			cursor.close();
			
			System.out.println("//////////////   query scope 20< x <=23");
			query = new BasicDBObject("x", new BasicDBObject("$gt",20).append("$lte",23));
			DBCursor cursor2 = testDataColl.find(query);
			while (cursor2.hasNext()) {
				DBObject dbObject = (DBObject) cursor2.next();
				System.out.println(dbObject);
			}
			cursor2.close();
			
			System.out.println("//////////////   Creating An Index");
			testDataColl.createIndex(new BasicDBObject("x",1));
			
			System.out.println("//////////////   iterator index list");
			List<DBObject> indexList = testDataColl.getIndexInfo();
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
