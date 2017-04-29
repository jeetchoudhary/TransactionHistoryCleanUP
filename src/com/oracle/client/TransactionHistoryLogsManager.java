package com.oracle.client;

import java.util.ArrayList;
import java.util.Iterator;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.oracle.common.AppConstants;

public class TransactionHistoryLogsManager {

	public static void main(String[] args) throws Exception {
		System.out.println(getListOfCurrentAvaliableTransactions());;
	}

	public static ArrayList<String> getListOfCurrentAvaliableTransactions() throws Exception {
		ArrayList<String> logLocationList = new ArrayList<>();
		Mongo mongo = new Mongo(AppConstants.HOST_NAME, AppConstants.DB_PORT);
		DB db = mongo.getDB(AppConstants.DB_NAME);
		DBCollection collection = db.getCollection(AppConstants.DB_SCHEMA_NAME);
		DBCursor curs = collection.find();
		Iterator<DBObject> fields = curs.iterator();
		while (fields.hasNext()) {
			logLocationList.add(AppConstants.LOG_DIRECTORY_START+((String) fields.next().get(AppConstants.DB_SCHEMA_FILTER_NAME)).substring(1)+AppConstants.LOG_DIRECTORY_END);
		}
		return logLocationList;
	}
}
