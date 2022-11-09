import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class RMIDemoClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url= "rmi://localhost:1099/rmiDemoObject";
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			RMIDemoInterface remoteIntf = (RMIDemoInterface) Naming.lookup(url);
			System.out.println(remoteIntf.sayHello());
			System.out.println("Enter two numbers:");
			System.out.print("a: ");
			int a = Integer.parseInt(br.readLine());
			System.out.print("b: ");
			int b = Integer.parseInt(br.readLine());
			int sum = remoteIntf.add(a, b);
			int deference = remoteIntf.subtract(a, b); 
			int product = remoteIntf.multiply(a, b);
			
			System.out.println("The sum is : "+sum);
			System.out.println("The deference is : "+deference);
			System.out.println("The product is : "+product);
			
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}