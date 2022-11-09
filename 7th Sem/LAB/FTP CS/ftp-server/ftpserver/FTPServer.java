import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class FTPServer extends JFrame {

	private static final long serialVersionUID = 112345678L;

	static JLabel l;

	JPanel middle;
	JList filelist;
	static DefaultListModel model;
	JScrollPane scrollPane;
	JButton refresh;

	public FTPServer(String name) throws IOException {
		super(name);
		setLayout(new BorderLayout());
		setSize(600, 200);
		setResizable(false);
		// creating label
		l = new JLabel("Waiting for Connection");
		JPanel pj = new JPanel();
		pj.add(l);
		pj.setPreferredSize(new Dimension(600, 30));
		add(pj, BorderLayout.NORTH);

		// creating space for file
		middle = new JPanel();
		// middle.setLayout(new BorderLayout());
		middle.setPreferredSize(new Dimension(600, 200));
		middle.setLayout(new BorderLayout());
		model = new DefaultListModel();
		
		filelist = new JList(model);
		filelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		scrollPane = new JScrollPane(filelist);
		updateList();

		JPanel jscp = new JPanel();
		jscp.setLayout(new FlowLayout());
		jscp.add(scrollPane);

		middle.add(jscp, BorderLayout.CENTER);

		JPanel ref = new JPanel();
		ref.setLayout(new FlowLayout());
		refresh = new JButton("Refersh");
		refresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					updateList();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		ref.add(refresh);
		middle.add(ref, BorderLayout.SOUTH);
		add(middle, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void updateList() throws IOException {
		// TODO Auto-generated method stub
		model.clear();
		File f = new File("."); // current directory

		File[] files = f.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				continue;
			} else {
				model.addElement("       " + file.getName() + "       ");
			}

		}

	}

	/**
	 * @param args
	 * @throws IOException
	 */

	static Socket ClientSoc;
	static DataInputStream din;
	static DataOutputStream dout;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket soc = new ServerSocket(5217);
		FTPServer ftp = new FTPServer("Server");
		ClientSoc = soc.accept();
		l.setText("Connected");
		din = new DataInputStream(ClientSoc.getInputStream());
		dout = new DataOutputStream(ClientSoc.getOutputStream());
		Thread t = new Thread() {
			public void run() {
				try {
					while (true) {
						String filename = din.readUTF();
						System.out.println("File name:"+filename  + filename.indexOf("_$_"));
						if (filename.indexOf("?")==0) {

							File f = new File("."); // current directory
							String ans = "";
							File[] files = f.listFiles();
							for (File file : files) {
								if (file.isDirectory()) {
									continue;
								} else {
									ans += file.getName() + "?";
								}

							}
							dout.writeUTF(ans);

						} else if (filename.indexOf("////") == 0) {
							String s = filename.substring(4);
							System.out.println("REquested me to send"+s);
							
							File f = new File(s);

							if (!f.exists()) {
								l.setText("Requested File not Found..." + s);
								dout.writeUTF("???");
								continue;
							}

							try {
								dout.writeUTF(s);
								System.out.println(s);

								din.readUTF();

								l.setText("Sending File ...");
								FileInputStream fin = new FileInputStream(f);
								int ch;
								do {
									ch = fin.read();
									dout.writeUTF(String.valueOf(ch));
								} while (ch != -1);
								fin.close();
								din.readUTF();
								l.setText("File send Sucessfully");
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							System.out.println(filename);
							l.setText("recivening file..");
							File f = new File(filename);

							dout.writeUTF("SendFile");
							FileOutputStream fout = new FileOutputStream(f);
							int ch;
							String temp;
							do {
								temp = din.readUTF();
								ch = Integer.parseInt(temp);
								if (ch != -1) {
									fout.write(ch);
								}
							} while (ch != -1);
							fout.close();
							dout.writeUTF("OS");
							l.setText("FileRecived");
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
}
