package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class HTTPClientEcho {
	public static String serverIp = "10.10.13.166";

	public static String disconnectNodeFromClient() {

		Socket smtpSocket = null;
		DataOutputStream os = null;
		DataInputStream is = null;
		String nodeAddress = null;
		try {
			smtpSocket = new Socket(serverIp, 48104);
			os = new DataOutputStream(smtpSocket.getOutputStream());
			is = new DataInputStream(smtpSocket.getInputStream());
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: hostname");
		} catch (IOException e) {
			System.err
					.println("Couldn't get I/O for the connection to: hostname");
			e.printStackTrace();
		}

		if (smtpSocket != null && os != null && is != null) {
			try {
				os.writeBytes("Disconnect me");
				System.out.println(is.readLine());
				is.close();
				os.close();
				smtpSocket.close();
			} catch (UnknownHostException e) {
				System.err.println("Trying to connect to unknown host: " + e);
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
		}
		return nodeAddress;
	}
}
