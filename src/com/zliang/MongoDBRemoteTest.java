package com.zliang;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoDBRemoteTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Mongo mongoClient = null;
		DB db = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			//connect to db
			mongoClient = new Mongo( "C0007294.itcs.hp.com" , 27017 );
			
			//connect to database			
			db = mongoClient.getDB("track");
			
			//authorization
			char[] password = {'a','d','m','i','n'};
			db.authenticate("admin",password);
			
			//get collection name list
			Set<String> collNames = db.getCollectionNames();
			for(String colName : collNames){
				System.out.println(colName);
			}
			
			//get collection by name
			DBCollection paColl = db.getCollection("PA");
			long count = paColl.count();
			System.out.println("PA count = "+count);
			
			System.out.println("<<<<<<<<<<<<<<<<<<<<<################################>>>>>>>>>>>>>>>>>>>>>>");
			
			//query PA collection
			/*DBCursor cursor = paColl.find();
			for (Iterator<DBObject> iterator = cursor.iterator(); iterator.hasNext();) {
				DBObject dbObject = iterator.next();
				System.out.println(dbObject);
			}*/
			
			//query the first line
			DBObject one = paColl.findOne();
			Set<String> keySet = one.keySet();
			for (String key : keySet) {
				Object object = one.get(key);
				if(object instanceof BasicDBObject){
					BasicDBObject dbObj = (BasicDBObject)object;
					Set<Entry<String,Object>> entrySet = dbObj.entrySet();
					for (Entry<String, Object> entry : entrySet) {
						String entryKey = entry.getKey();
						Long entryValue = (Long) entry.getValue();
						System.out.println("entryKey = "+entryKey+", entryValue = "+entryValue);
					}
				} else if(object instanceof Date){
					Date date = (Date) object;
					String dateStr = df.format(date);
					System.out.println("dateStr = "+dateStr);
				}
			}
			System.out.println(one);
			Calendar cal = Calendar.getInstance();
			cal.set(2014, 3, 1);
//			Date date = cal.getTime();
			Date date = new Date();
			BasicDBObject query = new BasicDBObject("time",new BasicDBObject("$gt",date));
			DBCursor cursor = paColl.find(query);
			while(cursor.hasNext()){
				DBObject dbObject = cursor.next();
				ObjectId objectId = (ObjectId) dbObject.get("_id");
				BSONTimestamp ts = (BSONTimestamp) dbObject.get("time");
//				String user = (String) dbObject.get("user");
//				String pass = (String) dbObject.get("pass");
				System.out.print(objectId);
				System.out.print(", ");
				System.out.println(ts);
			}
			cursor.close();
			
			
			
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
