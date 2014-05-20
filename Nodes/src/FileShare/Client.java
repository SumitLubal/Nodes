package FileShare;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

	private DataOutputStream dos;
	Socket socket;
	CopyDialog copy;
	String hostDomain = "";
	int port = 48101;
	public void sendMultiFiles(File fil) throws IOException {
		String directory;
		directory = fil.getAbsolutePath();
		File[] files = new File(directory).listFiles();
		int count = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].canRead()) {
				count++;
			}
		}
		String hostDomain = null;
		int port = 48101;
		Socket socket = new Socket(InetAddress.getByName(hostDomain), port);
		BufferedOutputStream bos = new BufferedOutputStream(
				socket.getOutputStream());
		dos = new DataOutputStream(bos);

		dos.writeInt(count);

		for (File file : files) {
			System.out.println("For file " + file.getName());
			if (file.isDirectory()) {
				try {
					this.send(file, "");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				if (!file.isFile() && !file.isDirectory()) {
					System.out
							.println("Can't send files starting with . or \\ or / Symbols s");
					continue;
				}
			}
			long length = file.length();
			dos.writeLong(length);

			String name = file.getName();
			dos.writeUTF(name);
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);

			} catch (Exception e) {
				System.out.println(e.getMessage());
				continue;
			}
			BufferedInputStream bis = new BufferedInputStream(fis);
			int theByte = 0;
			while ((theByte = bis.read()) != -1) {
				bos.write(theByte);
			}
			bis.close();
		}
	}

	public void send(File fil, String path) throws Exception {
		try {
			String directory = null;
			if (fil.isFile()) {
				System.out.println("Sending single file");
				socket = new Socket(InetAddress.getByName(hostDomain), port);
				BufferedOutputStream bos = new BufferedOutputStream(
						socket.getOutputStream());
				dos = new DataOutputStream(bos);

				dos.writeInt(1);

				String name = fil.getName();

				dos.writeLong(fil.length());
				dos.writeUTF(path);
				dos.writeUTF(name);
				System.out.println("Client sending " + path);
				// dos.writeUTF(path);

				FileInputStream fis = new FileInputStream(fil);
				BufferedInputStream bis = new BufferedInputStream(fis);

				int theByte = 0;

				copy.status.setValue(0);
				copy.status.setName("Sending File"+path);
				copy.status.setMaximum((int) fil.length());
				copy.info.setText("Sending "+path);
				while ((theByte = bis.read()) != -1) {
					bos.write(theByte);
					copy.status.setValue(copy.status.getValue() + 1);
				}

				bis.close();
				dos.close();
				return;
			}

		} catch (Exception e) {
		} finally {
			if (socket == null) {
				dos.close();
				System.out.println("Connection closed client");
			}
		}
	}
}
