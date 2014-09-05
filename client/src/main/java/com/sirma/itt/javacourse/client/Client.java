package com.sirma.itt.javacourse.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTextArea;

/**
 * Represents the client thread which waits for messages form the server and
 * processes them.
 * 
 * @author Nikolay Ch
 * 
 */
public class Client extends Thread {
	private JTextArea messArea = null;
	private JTextArea clientsArea = null;
	private Socket clientSocket;
	private PrintWriter writer;
	private BufferedReader reader;
	private AtomicBoolean isFirst = new AtomicBoolean(true);
	private AtomicBoolean isConnected = new AtomicBoolean(false);
	private String message = "";
	private StringBuilder clients = new StringBuilder("");
	private List<String> clientsList = new ArrayList<>();
	
	/**
	 * Constructor by default which is designed to work without GUI.
	 */
	public Client() {

	}

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
		try {
			messArea.append(what + "\n");
		} catch (NullPointerException e) {

		}
	}

	/**
	 * Rewrites the connected clients.
	 * 
	 * @param what
	 *            the string with the connected clients
	 */
	public void rewriteClients(String what) {
		try {
			clientsArea.setText(what);
		} catch (NullPointerException e) {

		}
	}

	/**
	 * Connects the client to the server if there is active one. Else notifies
	 * the client. Sets the values of the field that keep if the client is
	 * connected and if its first message to true.
	 */
	public void connect() {
		try {
			if (!isConnected.get()) {
				clientSocket = new Socket("localhost", 1948);
				writer = new PrintWriter(clientSocket.getOutputStream(), true);
				reader = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
				isConnected.set(true);
				isFirst.set(true);
				message = "";
			}
		} catch (IOException e) {
			message = "NoServer";
		}
	}

	/**
	 * Stops the connection, closes the socket and sets the message field its
	 * beginning value.
	 * 
	 */
	public void stopConnection() {
		try {
			writer.close();
			reader.close();
			clientSocket.close();
			message = "";
		} catch (IOException | NullPointerException ne) {
			writeMessage("Error occured when trying to close the socket.");
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

	/**
	 * Removes a client from the list with the clients. Then deletes the old
	 * content of the builder and adds the available.
	 * 
	 * @param name
	 *            the name of the client
	 */
	public void removeClient(String name) {
		clientsList.remove(name);
		clients.setLength(0);
		for (int i = 0; i < clientsList.size(); i++) {
			clients.append(clientsList.get(i) + "\n");
		}
		rewriteClients(clients.toString());
	}

	/**
	 * Adds new client to the list with all the clients. The first message from
	 * the server represents all connected clients'nicknames separated by [
	 * which is invalid character and can not be contained in them.
	 * 
	 * @param name
	 *            the name of the client
	 * @param client
	 *            the client's thread
	 */
	public void addClient(String name) {
		int before = 0;
		if (name.contains("[")) {
			StringBuilder buildedClients = new StringBuilder(name);
			for (int i = 0; i < name.length(); i++) {
				if (name.charAt(i) == '[') {
					clientsList.add(name.substring(before, i));
					before = i + 1;
					buildedClients.setCharAt(i, '\n');
				}
			}
			clients.append(buildedClients.toString());
		} else {
			clients.append(name + "\n");
			clientsList.add(name);
		}
		rewriteClients(clients.toString());
	}

	@Override
	public void run() {
		try {
			/*
			 * When the client's window is started its client's thread also
			 * starts. It loops until an exception occurs. First it check if the
			 * client is connected which is kept in a boolean variable. if its
			 * true reads the message form the server. It should contains the
			 * nicknames of the connected clients or false if the client's
			 * nickname is not correct. If it is not the first checks if the
			 * message start with N of D which means that new client has
			 * connected or old one has disconnected(All other messages starts
			 * with <theNicknameOFtheSender>).Else just writes the message on
			 * the window.
			 */
			while (true) {
				if (isConnected.get()) {
					message = reader.readLine();
					if (isFirst.get()) {

						if (!message.equals("False")) {
							addClient(message);
							writeMessage("Welcome " + this.getName());
						} else {
							isConnected.set(false);
						}
						isFirst.set(false);
					} else {
						writeMessage(message);
						if (message.charAt(0) == 'N') {
							addClient(message.substring(17));
						}
						if (message.charAt(0) == 'D') {
							removeClient(message.substring(13));
						}
					}
				}
			}
		} catch (NullPointerException e) {

		} catch (IOException e) {
			writeMessage("The server has stopped.");
		}
	}

	/**
	 * Getter for the message field. It is used form the client's window for
	 * some information.
	 * 
	 * @return the value of the field
	 */
	public String getMessage() {
		return message;
	}
}
