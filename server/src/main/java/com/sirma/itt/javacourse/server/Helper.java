package com.sirma.itt.javacourse.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

public class Helper {
	private static JTextArea areaToWrite;
	private static Map<String, Socket> clientsList = new HashMap<String, Socket>();
	private static PrintWriter writer;
	private static Calendar cal = Calendar.getInstance();
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	/**
	 * Initializes the area where to write.
	 * 
	 * @param area
	 *            the area where to write
	 */
	public Helper(JTextArea area) {
		areaToWrite = area;
	}

	/**
	 * Writes on the area.
	 * 
	 * @param message
	 *            the string that must be written.
	 */
	public static void write(String message) {
		areaToWrite.append(message + "\n");
	}

	/**
	 * Adds a client to the list with the all clients.
	 * 
	 * @param name
	 *            the name of the client
	 * @param client
	 *            its socket
	 */
	public static void addToListWithClients(String name, Socket client) {
		clientsList.put(name, client);
	}

	/**
	 * Checks if a client's name is appropriate.
	 * 
	 * @param name
	 *            the name which must be checked
	 * @return true if the name is correct
	 */
	public static boolean checkIfCorrect(String name) {
		if (name.equals("")) {
			return false;
		}
		if (name.indexOf("[") != -1 || name.indexOf("]") != -1) {
			return false;
		}
		for (Map.Entry<String, Socket> entry : clientsList.entrySet()) {
			if (entry.getKey().equals(name)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Sends a message to all the connected clients.
	 * 
	 * @param what
	 *            the message that must be sent
	 */
	public static void sendToAll(String what) {
		if (clientsList.size() != 0) {
			for (Map.Entry<String, Socket> entry : clientsList.entrySet()) {
				try {
					writer = new PrintWriter(
							entry.getValue().getOutputStream(), true);
					writer.println(what);
				} catch (IOException e) {
					write("An error occured while trying to send the message to "
							+ entry.getKey());
				}
			}
		}
	}

	/**
	 * Returns the current time in the format hour:minute:second .
	 * 
	 * @return the string with the time
	 */
	public static String getCurrentTime() {
		cal.getTime();
		return sdf.format(cal.getTime());
	}

	/**
	 * Removes form the list with the clients.
	 * 
	 * @param name
	 *            the name of the client
	 */
	public static void removeClient(String name) {
		clientsList.remove(name);
	}

}
