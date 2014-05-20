package FileShare;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.util.ArrayList;
import java.nio.file.attribute.*;

public class FileBrowserServer extends Thread {
	public static void main(String a[]) {
		new FileBrowserServer();
	}

	private ServerSocket socketServer;
	private Socket socket;
	private String userName;
	ArrayList <String> files;
	private String baseDir;
	String str;
	FileBrowserServer() {
		try {
			socketServer = new ServerSocket(2400);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				connect();
				sendData();
				disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void disconnect() throws IOException {
		// TODO Auto-generated method stub
		socket.close();
	}

	private void sendData() throws IOException {
		files = new ArrayList<String>();
		ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
		ObjectOutputStream output = new ObjectOutputStream(
				socket.getOutputStream());
		//userName = input.readUTF();
		System.out.println(userName);
		createArrayOfRecursion((baseDir = "f:/test/"));
		output.writeInt(files.size());
		for(int i=0;i<files.size();i++){
			output.writeUTF(files.get(i));
		}
		output.close();
		input.close();
	}

	private ArrayList<String> createArrayOfRecursion(String string) throws IOException {
		Path startPath = Paths.get(string);
		Process(new File(string));
		return null;
		// TODO Auto-generated method stub

	}

	static int spc_count = -1;

	void Process(File aFile) throws IOException {
		spc_count++;
		String spcs = "";
		for (int i = 0; i < spc_count; i++)
			spcs += " ";
		if (aFile.isFile()){
			System.out.println(spcs + "[FILE] " + aFile.getName());
			files.add(aFile.getPath().replace(baseDir, ""));
		}
		else if (aFile.isDirectory()) {
			System.out.println(spcs + "[DIR] " + aFile.getName());
			File[] listOfFiles = aFile.listFiles();
			if (listOfFiles != null) {
				for (int i = 0; i < listOfFiles.length; i++)
					Process(listOfFiles[i]);
			} else {
				System.out.println(spcs + " [ACCESS DENIED]");
			}
		}
		spc_count--;
	}

	private void connect() throws IOException {
		socket = socketServer.accept();
	}
}
