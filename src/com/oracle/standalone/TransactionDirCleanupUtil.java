package com.oracle.standalone;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.oracle.common.AppConstants;

/**
 * @author jjikumar
 *
 */
public class TransactionDirCleanupUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		TransactionDirCleanupUtil util = new TransactionDirCleanupUtil();
		util.cleanupDirs();
		
	}

	private void cleanupDirs() throws Exception {
		TransactionDirCleanupUtil client = new TransactionDirCleanupUtil();
		ArrayList<String> orphanTransactions = new ArrayList<String>();
		ArrayList<String> allTransactionList = client.listDirectories(AppConstants.DIRECOTRY_CLEANUP_PATH);
		ArrayList<String> activeTransactionList = getListOfCurrentAvaliableTransactions();
		for (String dirName : allTransactionList) {
			if (!activeTransactionList.contains(dirName)) {
				orphanTransactions.add(dirName);
			}
		}
		System.out.println("List of dirs To be deleted : "+orphanTransactions);
		client.removeDirectories(orphanTransactions);

	}

	private ArrayList<String> getListOfCurrentAvaliableTransactions() throws Exception {
		ArrayList<String> logLocationList = new ArrayList<>();
		Mongo mongo = new Mongo(AppConstants.HOST_NAME, AppConstants.DB_PORT);
		DB db = mongo.getDB(AppConstants.DB_NAME);
		DBCollection collection = db.getCollection(AppConstants.DB_SCHEMA_NAME);
		DBCursor curs = collection.find();
		Iterator<DBObject> fields = curs.iterator();
		while (fields.hasNext()) {
			logLocationList.add(AppConstants.LOG_DIRECTORY_START
					+ ((String) fields.next().get(AppConstants.DB_SCHEMA_FILTER_NAME)).substring(1)
					+ AppConstants.LOG_DIRECTORY_END);
		}
		System.out.println("List of Active Trans : "+ logLocationList);
		return logLocationList;
	}

	private ArrayList<String> removeDirectories(ArrayList<String> dirList) {
		ArrayList<String> dirListNotDeleted = new ArrayList<String>();
		for (String dir : dirList) {
			try {
				FileUtils.forceDelete(new File(dir));
			} catch (IOException e) {
				e.printStackTrace();
				dirListNotDeleted.add(dir);
			}
		}
		return dirListNotDeleted;
	}

	private ArrayList<String> listDirectories(String dirPath) {
		File file = new File(dirPath);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		ArrayList<String> dirList = new ArrayList<String>();
		for (String dirName : directories)
			dirList.add(dirPath + File.separator + dirName);
		System.out.println("List of All Folders : "+dirList);
		return dirList;
	}
}
