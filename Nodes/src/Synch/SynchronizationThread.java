package Synch;

public class SynchronizationThread extends Thread {
	ft2client client;
	ft2server server;
	public SynchronizationThread() {
		client = new ft2client();
		server = new ft2server();
	}
}
