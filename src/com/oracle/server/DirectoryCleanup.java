/**
 * 
 */
package com.oracle.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author jjikumar
 *
 */
public interface DirectoryCleanup extends Remote {
	public ArrayList<String> removeDirectories(ArrayList<String> dirList)throws RemoteException;
	public ArrayList<String> listDirectories(String dirPath)throws RemoteException;
}
