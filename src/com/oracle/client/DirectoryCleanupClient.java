package com.oracle.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import com.oracle.common.AppConstants;
import com.oracle.server.DirectoryCleanup;

public class DirectoryCleanupClient {

	public static void main(String[] args) throws Exception{
		DirectoryCleanupClient client = new DirectoryCleanupClient();
		ArrayList<String> orphanTransactions = new ArrayList<String>();
		ArrayList<String> allTransactionList = client.getListOfAllTransactionList(AppConstants.DIRECOTRY_CLEANUP_PATH);
		ArrayList<String> activeTransactionList = TransactionHistoryLogsManager.getListOfCurrentAvaliableTransactions();
		for(String dirName : allTransactionList){
			if(!activeTransactionList.contains(dirName)){
				orphanTransactions.add(dirName);
			}
		}
		client.removeDirectories(orphanTransactions);
	}
	
	private ArrayList<String> getListOfAllTransactionList(String dirPath) {
		ArrayList<String> response = null;
		try {
			Registry registry = LocateRegistry.getRegistry(AppConstants.HOST_NAME);
			DirectoryCleanup stub = (DirectoryCleanup) registry.lookup(AppConstants.RMI_BINDING_NAME);
			response = stub.listDirectories(dirPath);
			System.out.println("response: " + response);
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
		return response;
	}

	private ArrayList<String> removeDirectories(ArrayList<String> dirList) {
		ArrayList<String> response = null;
		try {
			Registry registry = LocateRegistry.getRegistry(AppConstants.HOST_NAME);
			DirectoryCleanup stub = (DirectoryCleanup) registry.lookup(AppConstants.RMI_BINDING_NAME);
			response = stub.removeDirectories(dirList);
			System.out.println("response: " + response);
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
		return response;
	}
}
