import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class FTPClient extends JFrame {

	private static final long serialVersionUID = 112345678L;

	JProgressBar jbar;
	JButton open, send, download, RefreshList;
	JFileChooser fc;
	JLabel l, file;
	JPanel middle;
	String filenameonly;
	JList filelist;
	DefaultListModel model;
	JScrollPane scrollPane;

	public FTPClient(String name) {
		super(name);
		setLayout(new BorderLayout());
		setSize(600, 200);
		setResizable(false);
		// creating label
		l = new JLabel("Welcome");
		JPanel pj = new JPanel();
		pj.add(l);
		pj.setPreferredSize(new Dimension(600, 30));
		add(pj, BorderLayout.NORTH);

		// creating space for file
		middle = new JPanel();
		middle.setLayout(new BorderLayout());
		file = new JLabel("No File Selected");
		open = new JButton("open");
		open.addActionListener(new FOPENER());
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
		jp.add(open);
		jp.setPreferredSize(new Dimension(100, 50));

		middle.add(jp, BorderLayout.EAST);
		JPanel jpfile = new JPanel();
		jpfile.setLayout(new FlowLayout());
		jpfile.add(file);
		jpfile.setPreferredSize(new Dimension(550, 50));
		middle.add(jpfile, BorderLayout.WEST);
		add(middle, BorderLayout.CENTER);

		JPanel bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.setPreferredSize(new Dimension(400, 200));

		JPanel jpsend = new JPanel();

		jpsend.setLayout(new FlowLayout());
		send = new JButton("upload");
		download = new JButton("Download");
		RefreshList = new JButton("Refresh List");
		jpsend.setPreferredSize(new Dimension(100, 200));
		jpsend.add(send);
		jpsend.add(download);
		jpsend.add(RefreshList);
		send.addActionListener(new SendFile());
		download.addActionListener(new DownloadFile());
		RefreshList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				GetList();
			}
		});
		bottom.add(jpsend, BorderLayout.EAST);

		model = new DefaultListModel();
		filelist = new JList(model);
		filelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		
		scrollPane = new JScrollPane(filelist);
		GetList();

		JPanel jppgbar = new JPanel();
		jppgbar.setLayout(new FlowLayout());
		jppgbar.add(scrollPane);
		bottom.add(jppgbar, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void GetList() {
		// TODO Auto-generated method stub
		model.clear();
		try {
			dout.writeUTF("?");
			String s = din.readUTF();

			l.setText("Refershing List");
			StringTokenizer str = new StringTokenizer(s, "?");
			while (str.hasMoreTokens()) {
				model.addElement("       " + str.nextToken() + "       ");
			}
			l.setText("Refreshing List Completed");
		} catch (Exception e) {

		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	static Socket ClientSoc;

	static DataInputStream din;
	static DataOutputStream dout;
	static BufferedReader br;

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		// TODO Auto-generated method stub
		new FTPClient("Client");
		Socket soc = new Socket("127.0.0.1", 5217);
		ClientSoc = soc;
		din = new DataInputStream(ClientSoc.getInputStream());
		dout = new DataOutputStream(ClientSoc.getOutputStream());
		br = new BufferedReader(new InputStreamReader(System.in));
	}

	class FOPENER implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			fc = new JFileChooser();
			int rval = fc.showOpenDialog(FTPClient.this);
			if (rval == JFileChooser.APPROVE_OPTION) {
				file.setText(fc.getCurrentDirectory().toString() + "\\"
						+ fc.getSelectedFile().getName());
				filenameonly = fc.getSelectedFile().getName();
			} else {
				file.setText("No File Selected");
			}

		}

	}; // FOPENER

	class SendFile implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String filename = file.getText();
			File f = new File(filename);

			if (!f.exists()) {
				l.setText("File not Exists...");

				return;
			}

			try {
				dout.writeUTF(filenameonly);
				System.out.println(filename);

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

		}
	};

	class DownloadFile implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String i = (String) filelist.getSelectedValue();
			i = i.trim();
			
			if (i == null) {
				l.setText("Please Select a file");
				return;
			}

			try {
				dout.writeUTF("////" + i);
				String givenFilename = din.readUTF();
				System.out.println("given :"+givenFilename);
				if (!givenFilename.contentEquals(i)) {
					l.setText("The File " + i + "Doesn't Exist..");
					return;
				}
				File f = new File(i);
				l.setText("Downloading file..");

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
				l.setText("File Downloaded");
			} catch (Exception e) {

			}
		}
	};

}; // class

