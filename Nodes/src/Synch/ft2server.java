package Synch;

import java.io.*;
import java.net.*;
import java.util.*;

public class ft2server extends Thread {
	private ServerSocket ss;
	private Socket cs;

	public ft2server() {
		try {
			ss = new ServerSocket(8085);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (true) {
			try {
				cs = null;
				try {
					cs = ss.accept();
					System.out.println("Connection established" + cs);
				} catch (Exception e) {
					System.out.println("Accept failed");
					System.exit(1);
				}
				PrintWriter put = new PrintWriter(cs.getOutputStream(), true);
				BufferedReader st = new BufferedReader(new InputStreamReader(
						cs.getInputStream()));
				String s = st.readLine();
				put.println(new File(s).getName());//gets name of file
				System.out.println("asking for file path");
				String path = s;
				System.out.println("The requested file is path: " + path);
				System.out.println("The requested file is : " + s);
				File f = new File(path);
				if (f.isFile()) {
					BufferedReader d = new BufferedReader(new FileReader(f));
					String line;
					while ((line = d.readLine()) != null) {
						put.write(line);
						put.flush();
					}
					d.close();
					System.out.println("File transfered");
					cs.close();
					ss.close();
				}

			} catch (Exception e) {

			}

		}
	}

	public static void main(String args[]) throws IOException {
		new ft2server();
	}
}