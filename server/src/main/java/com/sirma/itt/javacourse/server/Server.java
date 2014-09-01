package com.sirma.itt.javacourse.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents the server which waits for a connection from a client and if a
 * client connects it starts a thread that services the client.
 * 
 * @author Nikolay Ch
 * 
 */
public class Server extends Thread {

	private ServerSocket serverSocket;
	private Socket newClient = new Socket();
	private AtomicBoolean isStopped = new AtomicBoolean(false);
	private static Server server = null;

	/**
	 * Creates and starts the server.
	 */
	private Server() {
		try {
			serverSocket = new ServerSocket(1948);
		} catch (IOException e) {
			Helper.write("An error occured when trying to start the server.");
		}
	}

	@Override
	public void run() {
		/*
		 * Starts a loop where it waits for a connection from a client. Then
		 * starts a ClientThread for servicing it.
		 */
		while (!isStopped.get()) {
			try {
				newClient = serverSocket.accept();
				new ClientThread(newClient).start();

			} catch (IOException e) {
				Helper.write("Error occured when trying to accept new client.");
			}
		}
	}

	/**
	 * Getter for the boolean field that keeps if the server is stopped.
	 * 
	 * @return the value of the field
	 */
	public boolean getIsStopped() {
		return isStopped.get();
	}

	/**
	 * Stops the server.
	 */
	public void stopServer() {
		isStopped.set(true);
	}

	/**
	 * Returns an instance of the server's type.
	 * 
	 * @return a server
	 */
	public static Server startServer() {
		if (server == null) {
			server = new Server();
		}
		return server;
	}

}
