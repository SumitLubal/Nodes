package FileShare;
import java.net.*;
import java.util.ArrayList;
import java.awt.Frame;
import java.awt.Label;
import java.io.*;

import javax.swing.JFileChooser;

import communication.HTTPClientEcho;
import database.DBManager;

public class Server implements Runnable {
	ServerSocket serverSocket;
	Socket server;
	public static String tmpPath = new File(System.getProperty("user.dir")).getParentFile()
			.getAbsolutePath() + "/files";
	String dirPath = tmpPath;
	public Label saveInfo;

	public Server() throws IOException {
		serverSocket = new ServerSocket(48101, 100);
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		while (true) {
			while (true) {
				saveInfo.setText(dirPath);
				connect();
				recieve();
				saveInfo.setText(dirPath);
				closeCrap();
			}
		}

	}

	private void closeCrap() {
		try {
			server.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void recieve() {
		try {
			System.out.println("Recieving file");
			BufferedInputStream bis = new BufferedInputStream(
					server.getInputStream());
			DataInputStream dis = new DataInputStream(bis);
			int filesCount = dis.readInt();
			File[] files = new File[filesCount];

			for (int i = 0; i < filesCount; i++) {

				long fileLength = dis.readLong();
				String userName = dis.readUTF();

				dirPath = tmpPath + "/" + userName;

				System.out.println("UserName is : " + userName);
				String fileName = dis.readUTF();
				String name = dis.readUTF();
				System.out.println(fileName + " and a name" + name);
				resolveDirectory(fileName, name);// added user directory

				files[i] = new File(dirPath + fileName);

				FileOutputStream fos = new FileOutputStream(files[i]);
				BufferedOutputStream bos = new BufferedOutputStream(fos);

				for (int j = 0; j < fileLength; j++) {
					if (!server.isConnected()) {
						System.out.println("Connection closed server");
						break;
					}
					bos.write(bis.read());

				}

				System.out.println("file recieved " + fileName);
				bos.close();
				synchronise(dirPath + fileName, HTTPClientEcho.serverIp,userName);
			}

			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void synchronise(String filepath, String serverIp, String userName) {
		System.out.println("Synchronising");
		InetAddress IP = null;
		try {
			IP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String address = IP.getHostAddress();
		DBManager
				.fireQuerry("INSERT INTO `cluster`.`syn` (`host`, `filepath`, `whodone`,'user') VALUES ('"
						+ address + "', '" + filepath + "','"+userName+"')");
	}

	private void resolveDirectory(String fileName, String name) {
		String directory = fileName.replaceAll(name, "");
		System.out.println(directory);
		new File(dirPath + directory).mkdirs();
	}

	private void connect() {
		try {
			HTTPClientEcho.disconnectNodeFromClient();
			System.out.println("Waiting to connect");
			server = serverSocket.accept();
			System.out.println("connected");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
