package FileShare;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;


public class View extends JFrame{
	ServerSocket connection;
	Socket  server;
	ObjectOutputStream output;
	ObjectInputStream input;
	public static void main(String arg[]){
		new View();
	}
	public View() {
		try {
			connection = new ServerSocket(48101, 100);
			server = connection.accept();
			output = (ObjectOutputStream) server.getOutputStream();
			input = (ObjectInputStream) server.getInputStream();
			whileConnected();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void whileConnected() {
		
		
	}
}
