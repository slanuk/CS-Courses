import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class DnsServer extends UnicastRemoteObject implements DnsRemoteInterface{
    Properties hostRecords;
    FileInputStream fin = null;
    FileOutputStream fout = null;
    File nameList,dird;
	
	protected DnsServer() throws RemoteException {
		super();
		hostRecords = new Properties();
		dird = new File("d:/temp/");
		if (!dird.exists()) {
				dird.mkdir();
		}
		nameList = new File("d:/temp/NameList.txt");
		if (!nameList.exists()) {
			try {
				nameList.createNewFile();
			} catch (IOException e) {
			}
		}
		nameList.setReadOnly();

	}
	public static void main(String[] args) throws Exception{

		DnsRemoteInterface robj = (DnsRemoteInterface)new DnsServer();
        System.out.println("Creating RMI Registry...");
        Registry reg = LocateRegistry.createRegistry(1099);
        System.out.println("Binding Remote Object...");
        reg.rebind("dnsrobj", robj);
        System.out.println("Remote Object bound.");
        System.out.println("\nPress Ctrl+C to stop.");
	}

	@Override
	public boolean addHost(String hostName, String hostIP)
			throws RemoteException {
		// TODO Auto-generated method stub
        hostRecords.clear();
        nameList.setWritable(true);
		try {
            fin = new FileInputStream(nameList);
            if (fin != null) {
            	hostRecords.load(fin);
                fin.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		if (hostRecords.get(hostName) != null) {
            return false;
        }
        hostRecords.put(hostName, hostIP);
        try {
            fout = new FileOutputStream(nameList);
            hostRecords.store(fout, "");
            fout.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        nameList.setReadOnly();
        return true;

	}
	@Override
	public String lookupHost(String hostName) throws RemoteException {
		// TODO Auto-generated method stub
		String ip=null ;  
        hostRecords.clear();
		try
	        {
			fin = new FileInputStream(nameList);
			hostRecords.load(fin);
			ip = (String) hostRecords.get(hostName);
			fin.close();
	        }
			catch (IOException ex) {
	            ex.printStackTrace();
	        }		
	        return ip;
	}
	@Override
	public String removeHost(String hostName) throws RemoteException {
		// TODO Auto-generated method stub
		String ip=null;
        hostRecords.clear();
		nameList.setWritable(true);
		try {
			fin = new FileInputStream(nameList);
	        hostRecords.load(fin);
			ip = (String) hostRecords.remove(hostName);
	        try {
	            fout = new FileOutputStream(nameList);
	            hostRecords.store(fout, "");
	            fout.close();
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
			nameList.setReadOnly();
			fin.close();
	        
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return ip;
	}
}
