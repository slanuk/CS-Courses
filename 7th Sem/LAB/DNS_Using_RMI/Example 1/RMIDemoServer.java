import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

class RMIDemoImpl extends UnicastRemoteObject implements RMIDemoInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected RMIDemoImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String sayHello() throws RemoteException {
		// TODO Auto-generated method stub
		return "Hello Client! Welcome:";
	}

	@Override
	public int add(int a, int b) throws RemoteException {
		// TODO Auto-generated method stub
		return a+b;
	}

	@Override
	public int subtract(int a, int b) throws RemoteException {
		// TODO Auto-generated method stub
		return a-b;
	}

	@Override
	public int multiply(int a, int b) throws RemoteException {
		// TODO Auto-generated method stub
		return a*b;
	}
	
}
public class RMIDemoServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			RMIDemoInterface rmiDemoObject = new RMIDemoImpl();
			LocateRegistry.createRegistry(1099);
			Naming.rebind("rmiDemoObject",rmiDemoObject);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}