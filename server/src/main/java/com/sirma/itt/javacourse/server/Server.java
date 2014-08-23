package com.sirma.itt.javacourse.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Represents the server which waits for a connection from a client and if a
 * client connects it starts a thread that services the client.
 * 
 * @author Admin
 * 
 */
public class Server extends Thread {

	private ServerSocket server;
	private Socket newClient = new Socket();

	public Server() {
		try {
			server = new ServerSocket(1948);
		} catch (IOException e) {
			Helper.write("An error occured when trying to start the server.");
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				newClient = server.accept();
				new ClientThread(newClient).start();
				
			} catch (IOException e) {
				Helper.write("Error occured when trying to accept new client.");
			}
		}
	}

	/**
	 * Stops the server.
	 */
	public void stopServer() {
		System.exit(0);
	}
}
