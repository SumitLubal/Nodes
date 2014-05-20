package Synch;

import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.util.*;

import database.DBManager;

public class ft2client extends Thread {
	public ft2client() {
		start();
	}

	public void run() {
		InetAddress IP = null;
		try {
			IP = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String address = IP.getHostAddress();
		while (true) { // checks for new File
			try {
				ResultSet rs = DBManager.readFromTable("syn");
				while (rs.next()) {
					String ipField = rs.getString("whodone");
					String remoteIP = rs.getString("hostip");
					String username = rs.getString("user");
					String filePath = rs.getString("filepath");
					if (!ipField.contains(address)
							&& !remoteIP.contains(address)) {
						askForData(remoteIP, filePath, username);
						DBManager
								.fireQuerry("UPDATE `cluster`.`syn` SET `whodone` = '"
										+ ipField
										+ ","
										+ address
										+ "' WHERE `syn`.`filepath` = '"+filePath+"';");
					}

				}
				Thread.sleep(50);
			} catch (Exception e) {

			}
		}

	}

	private void askForData(String remoteIP, String filePath, String username) {
		// TODO Auto-generated method stub
		Socket s = null;
		BufferedReader get = null;
		PrintWriter put = null;
		try {
			s = new Socket(remoteIP, 8085);
			get = new BufferedReader(new InputStreamReader(s.getInputStream()));
			put = new PrintWriter(s.getOutputStream(), true);

			String u, f;
			System.out.println("FileName from server " + filePath);
			put.println(filePath);
			String name = get.readLine();
			
			
			String str = FileShare.Server.tmpPath + "/" + username + "/";// saves
																			// to
																			// specified
																			// folder
			FileOutputStream fs = new FileOutputStream(new File(str,
					name));

			while ((u = get.readLine()) != null) {
				System.out.println(u);
				byte jj[] = u.getBytes();
				fs.write(jj);
			}
			fs.close();
			System.out.println("File received");
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void main(String srgs[]) throws IOException {
		new ft2client();
	}
}