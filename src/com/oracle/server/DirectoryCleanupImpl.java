/**
 * 
 */
package com.oracle.server;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import com.oracle.common.AppConstants;

/**
 * @author jjikumar
 *
 */
public class DirectoryCleanupImpl implements DirectoryCleanup {

	public static void main(String args[]) {

		try {
			DirectoryCleanupImpl obj = new DirectoryCleanupImpl();
			DirectoryCleanup stub = (DirectoryCleanup) UnicastRemoteObject.exportObject(obj, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.bind(AppConstants.RMI_BINDING_NAME, stub);
			System.err.println("Transaction Cleanup Server Ready to Server...");
		} catch (Exception e) {
			System.err.println("Transaction Cleanup Server faild to start with exception: " + e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<String> removeDirectories(ArrayList<String> dirList) throws RemoteException {
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

	@Override
	public ArrayList<String> listDirectories(String dirPath) throws RemoteException {
		File file = new File(dirPath);
		String[] directories = file.list(new FilenameFilter()
		{
		  @Override
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		ArrayList<String> dirList = new ArrayList<String>();
		for(String dirName : directories)
			dirList.add(dirPath+File.separator+dirName);
		return dirList;
	}
}
