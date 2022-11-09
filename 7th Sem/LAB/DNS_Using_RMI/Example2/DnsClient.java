//DnsClient
import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.rmi.Naming;

public class DnsClient extends JFrame implements ActionListener {

	JButton b1, b2, b3, b4, b5;
	JPanel p1, p2;
	JLabel l1, l2;
	JTextField t1, t2;
	DataOutputStream output;
	DataInputStream input;

	DnsClient() {
		b1 = new JButton("AddHost");
		b2 = new JButton("Lookup");
		b3 = new JButton("Remove");
		b4 = new JButton("Refresh");
		b5 = new JButton("Close");
		p1 = new JPanel();
		p2 = new JPanel();
		l1 = new JLabel("Host");
		l2 = new JLabel("IP");
		t1 = new JTextField("", 20);
		t2 = new JTextField("", 20);
		p1.setLayout(new FlowLayout());
		p2.setLayout(new FlowLayout());

		p1.add(l1);
		p1.add(t1);
		p1.add(l2);
		p1.add(t2);

		p2.add(b1);
		p2.add(b2);
		p2.add(b3);
		p2.add(b4);
		p2.add(b5);
		add(p1, "North");
		add(p2, "South");
		setSize(600, 300);
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		b5.addActionListener(this);
		setTitle("DNS Client Application");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();

		DnsRemoteInterface dri = null;
		try {
			dri = (DnsRemoteInterface) Naming
					.lookup("rmi://localhost:1099/dnsrobj");

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (s.equals("Refresh")) {
			t1.setText("");
			t2.setText("");
		}
		if (s.equals("Close")) {
			System.exit(0);
		}

		try {
			if (s.equals("AddHost")) {
				if (!t1.getText().trim().isEmpty()
						|| !t2.getText().trim().isEmpty()) {
					Boolean b = dri.addHost(t1.getText(), t2.getText());
					if (b == true) {
						t2.setText("Registered");
					} else {
						t2.setText("Not Registered");
					}
				} else {
					JOptionPane.showMessageDialog(this,
							"Fields cannot be blank");
				}
			}
			if (s.equals("Lookup")) {
				if (!t1.getText().trim().isEmpty()) {
					String s1 = dri.lookupHost(t1.getText());
					t2.setText(s1);
					if (s1 == null) {
						t2.setText("host name not found");
					} else {
						t2.setText("the ip address is " + s1);
					}
				} else {
					JOptionPane
							.showMessageDialog(this, "Field cannot be blank");
				}
			}
			if (s.equals("Remove")) {
				if (!t1.getText().trim().isEmpty()) {			
					String s2 = dri.removeHost(t1.getText());
					if (s2 == null) {
						t2.setText("host name not found");
					} else {
						t2.setText("the ip address" + s2 + "is removed");
					}
				} else {
					JOptionPane
							.showMessageDialog(this, "Field cannot be blank");
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new DnsClient();
	}
}