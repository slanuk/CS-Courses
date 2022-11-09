import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RMIDemoInterface extends Remote{
	public String sayHello() throws RemoteException;
	public int add(int a, int b) throws RemoteException;
	public int subtract(int a, int b) throws RemoteException;
	public int multiply(int a, int b) throws RemoteException;
}

