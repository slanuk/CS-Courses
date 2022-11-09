//DnsRemoteInterface
import java.rmi.Remote;

public interface DnsRemoteInterface extends Remote{
	public boolean addHost(String hostName,String hostIP) throws java.rmi.RemoteException;
	public String lookupHost(String hostName) throws java.rmi.RemoteException;
	public String removeHost(String hostName) throws java.rmi.RemoteException;

}
