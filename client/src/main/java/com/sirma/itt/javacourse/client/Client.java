package com.sirma.itt.javacourse.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Represents the client.
 * 
 * @author Nikolay Ch
 * 
 */
public class Client extends Thread {
	private JTextArea messArea;
	private JTextArea clientsArea;
	private Socket clientSocket;
	private PrintWriter writer;
	private BufferedReader reader;
	private AtomicBoolean isAlive = new AtomicBoolean(true);
	private AtomicBoolean isConnected = new AtomicBoolean(false);
	private String message = "";
	

	/**
	 * Gets a text area, where it writes the messages.
	 * 
	 * @param area
	 *            the JTextArea
	 */
	public Client(JTextArea messArea, JTextArea clientsArea) {
		this.messArea = messArea;
		this.clientsArea = clientsArea;
	}

	/**
	 * Writes on the area.
	 * 
	 * @param what
	 *            the string which must be written
	 */
	public void writeMessage(String what) {
		messArea.append(what + "\n");
	}

	/**
	 * Writes on the area for the clients.
	 * 
	 * @param what
	 *            the string with the connected clients
	 */
	public void rewriteClients(final String what) {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				clientsArea.setText(what);
			}
		});
	}

	/**
	 * Connects the client to the server if there is active one. Else notifies
	 * the client.
	 */
	public void connect() {
		try {
			if (!isConnected.get()) {
				clientSocket = new Socket("localhost", 1948);
				writer = new PrintWriter(clientSocket.getOutputStream(), true);
				reader = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
				isConnected.set(true);
			}
		} catch (IOException e) {
			message = "NoServer";
		}
	}

	/**
	 * Sends a message to the server.
	 * 
	 * @param what
	 *            the message that must be send
	 */
	public void sendToServer(String what) {
		try {
			writer.println(what);
		} catch (NullPointerException e) {
			writeMessage("There is no server.");
		}

	}

	@Override
	public void run() {
		try {
			while (isAlive.get()) {
				if (isConnected.get()) {

					message = reader.readLine();

					if (!message.equals("False")) {
						writeMessage(message);
						
						
					} else {
						isConnected.set(false);
					}

				}
			}
		} catch (NullPointerException e) {

		} catch (IOException e) {
			writeMessage("The server has stopped.");
		}
	}

	/**
	 * Getter for the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Stops the connection.
	 */
	public void stopConnection() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			writeMessage("Error occured when trying to close the socket.");
		}
	}
}
