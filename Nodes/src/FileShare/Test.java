package FileShare;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import Synch.SynchronizationThread;

public class Test extends JFrame implements Runnable, ActionListener {
	Server server;
	public static Client client;
	FileSender fileSender;
	String lastIP = "";
	private List<File> droppedFilesParent = null;
	Button setDirectory, banIP, help;
	Label textPad;
	Label saveInfo;
	JPanel buttonPanel;
	String blockIp;
	public static CopyDialog copy;
	static String selectedItem = "";
	private CustomChooserFrame chooser;

	public static void main(String[] args) {
		new Test();
	}

	public Test() {
		setFrameParameter();
		setBounds(WindowProperty.getWidth() - 200,
				WindowProperty.getHeight() - 200, 200, 100);
		copy = new CopyDialog();
		copy.setBounds(WindowProperty.getWidth() - 200,
				WindowProperty.getHeight() - 200, 200, 100);
		client = new Client();
		client.copy = this.copy;
		fileSender = new FileSender();
		fileSender.copy = this.copy;
		chooser = new CustomChooserFrame();
		chooser.setVisible(false);
		new Thread(this).start();
		setVisible(true);
		this.setDropTarget(new DropTarget() {
			public void drop(DropTargetDropEvent evt) {

				evt.acceptDrop(DnDConstants.ACTION_COPY);
				List<File> droppedFiles = null;
				try {
					droppedFiles = (List<File>) evt.getTransferable()
							.getTransferData(DataFlavor.javaFileListFlavor);
					chooser.setVisible(true);
					droppedFilesParent = droppedFiles;
					saveInfo.setText("Saving To" + server.dirPath);
					System.out.println("some files dropped");
				} catch (IOException e) {
				} catch (UnsupportedFlavorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		try {
			server = new Server();
			server.saveInfo = this.saveInfo;
			if (server.dirPath == null) {
				saveInfo.setText("Saving To" + fileSender.baseDirectory);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(),
					"Network Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		new SynchronizationThread();
		new FileBrowserServer();
	}

	private void setFrameParameter() {
		// setUndecorated(true);
		try {
			ImageIcon img = new ImageIcon(
					Test.class.getResource("/org/icon.png"));
			this.setIconImage(img.getImage());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setTitle("File Share Utility 1.0");
		setSize(200, 100);
		setResizable(false);
		setDirectory = new Button("SaveTo");
		help = new Button("Help");
		banIP = new Button("Ban IP");
		textPad = new Label();
		saveInfo = new Label();
		textPad.setText("Drop SOME FILES ON ME");
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 3));
		buttonPanel.add(setDirectory);
		buttonPanel.add(banIP);
		buttonPanel.add(help);
		add(textPad);
		add(saveInfo);
		add(buttonPanel);
		setLayout(new GridLayout(3, 1));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setDirectory.addActionListener(this);
		banIP.addActionListener(this);
		help.addActionListener(this);
	}

	@Override
	public void run() {
		while (true) {
			if (selectedItem != null) {
				if (droppedFilesParent != null
						&& selectedItem.contains("Local Lan Share")) {
					// share in cloud
					setDirectory.setEnabled(false);
					banIP.setEnabled(false);
					client.hostDomain = getDestinationIpByDailogue();
					client.port = 48101;
					fileSender.sendDroppedFiles(droppedFilesParent, client);
					droppedFilesParent = null;
					setDirectory.setEnabled(true);
					banIP.setEnabled(true);
				} else if (droppedFilesParent != null
						&& selectedItem.contains("FTP")) {
					// share in FTPsetDirectory.setEnabled(false);
					banIP.setEnabled(false);
					String userName = JOptionPane.showInputDialog(this,
							"What is user's name?");
					String password = JOptionPane.showInputDialog(this,
							"What is password");
					// FTPuploader.uploadFiles(droppedFilesParent,CustomChooserFrame.ipField.getText());

					droppedFilesParent = null;
					setDirectory.setEnabled(true);
					banIP.setEnabled(true);
					System.out.println("FTP");
				} else if (droppedFilesParent != null
						&& selectedItem.contains("Cloud")) {
					// cloud sharing
					System.out.println("Cloud");
					// get available IP address from main server
					// System.out.println("Communicating with: "
					// +HTTPClientEcho.getNodeIPAdress("127.0.0.1"));

				} else if (droppedFilesParent != null
						&& selectedItem.contains("Database")) {
					// database sharing
					System.out.println("Database");
				}
				selectedItem = null;
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(),
						"Network Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
	}

	private String getDestinationIpByDailogue() {
		while (true) {
			String s = (String) JOptionPane.showInputDialog(this,
					"Enter IP address of destination", "Destination IP",
					JOptionPane.PLAIN_MESSAGE, null, null, lastIP);

			System.out.println(s);
			if (s == null) {
				return "10";
			}
			try {
				testAddress(s);
			} catch (Exception e) {

			}
			lastIP = s;
			return s;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Button pressedButton = (Button) e.getSource();
		if (pressedButton.equals(setDirectory)) {
			setServerDirectory();
		} else if (pressedButton.equals(banIP)) {
			String s = (String) JOptionPane.showInputDialog(this,
					"Enter IP address To be blocked", "Block IP",
					JOptionPane.PLAIN_MESSAGE, null, null, lastIP);
			blockIp += s;
		} else if (pressedButton.equals(help)) {
			JOptionPane.showMessageDialog(this,
					"Please refer ReadMe file.. Credits - Sagar,Ankit,Sumit");
		}
	}

	private void setServerDirectory() {
		JFileChooser chooser = new JFileChooser();
		chooser.setLocation(this.getLocation());
		// chooser.setCurrentDirectory(new java.io.File(""));
		chooser.setDialogTitle("Set Save Directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//
		// disable the "All files" option.
		//
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			if (chooser.getSelectedFile().isDirectory())
				server.tmpPath = chooser.getSelectedFile().getAbsolutePath();
			else if (chooser.getSelectedFile().isFile()) {
				server.tmpPath = chooser.getCurrentDirectory().getParentFile()
						.getAbsolutePath();
			}
		}

		// server.dirPath = chooser.getCurrentDirectory().getAbsolutePath();
		System.out.println(server.tmpPath);
		saveInfo.setText(server.tmpPath);
		textPad.setText("Drop Files To Send");
	}

	public static InetAddress testAddress(String str)
			throws UnknownHostException {
		InetAddress add = InetAddress.getByName(str);

		// Check if IP address was simply returned, instead of host.
		if (add.getCanonicalHostName().equals(add.getHostAddress())) {
			throw new UnknownHostException(str + "is not a known host.");
		}
		return add;
	}

}