package com.sirma.itt.javacourse.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * This thread is started from the server for each connected client. It receives
 * the messages form the client and progresses them.
 * 
 * @author Nikolay Ch
 * 
 */
public class ClientThread extends Thread {
	private Socket client;
	private String name = "";
	private boolean isAlive = true;
	private boolean isAdded = false;
	private StringBuilder message = new StringBuilder("");
	private StringBuilder allMessage = new StringBuilder("");

	/**
	 * Gets the client's socket.
	 * 
	 * @param client
	 *            the socket
	 */
	public ClientThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		/*
		 * Started when a client connects. It receives the first message which
		 * is the client's nickname. Notifies the server that a client with that
		 * name is trying to connect by the Helper's static method for writing
		 * on the server's area. Then checks if the client's name is correct and
		 * available and if it is true sends him the connected clients'nicknames.
		 * Then starts a loop for reading a messages from him and every time it
		 * receives one, notifies the server and sends it to the all clients.Else
		 * sends him false and stops the thread. If the message is Disconnect
		 * closes the connection.
		 */
		BufferedReader reader;
		PrintWriter writer;

		try {
			reader = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			writer = new PrintWriter(client.getOutputStream(), true);

			name = reader.readLine();
			Helper.write(Helper.getCurrentTime() + ": The client " + name
					+ " is trying to connect.");
			if (Helper.checkIfCorrect(name)) {
				Helper.sendToAll("New connection : " + name);
				Helper.addToListWithClients(name, client);
				writer.println(Helper.clientsToString());
				Helper.write(Helper.getCurrentTime() + ": The client " + name
						+ " has connected.");
				isAdded = true;
				while (isAlive) {
					message.setLength(0);
					allMessage.setLength(0);

					message.append("<").append(name).append(">:")
							.append(Helper.upperFirst(reader.readLine()));
					if (message.toString().equals("<" + name + ">:Disconnect")) {
						client.close();
						Helper.removeClient(name);
						isAlive = false;
						throw new IOException();
					} else {
						allMessage.append(Helper.getCurrentTime()).append(
								message);
						Helper.write(allMessage.toString());
						Helper.sendToAll(message.toString());
					}

				}

			} else {
				writer.println("False");
				isAlive = false;
			}
		} catch (IOException e) {
			Helper.write(Helper.getCurrentTime() + ":The client " + name
					+ " has disconnected.");
		}
		if (isAdded) {
			Helper.removeClient(name);
			Helper.sendToAll("Disconnect : " + name);
		} else {
			Helper.write(Helper.getCurrentTime() + ":The client " + name
					+ " has disconnected.");
		}
	}
}
