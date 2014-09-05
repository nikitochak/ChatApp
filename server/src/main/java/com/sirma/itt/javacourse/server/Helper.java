package com.sirma.itt.javacourse.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Static class that maintains a list with the connected clients'names.Also has
 * methods for sending messages to them.
 * 
 * @author Nikolay Ch
 * 
 */
public class Helper {
	private static final Logger LOGGER = Logger.getLogger(Helper.class);
	private static JTextArea areaToWrite;
	private static Map<String, Pair<Socket, PrintWriter>> clientsData = Collections
			.synchronizedMap(new HashMap<String, Pair<Socket, PrintWriter>>());
	private static Calendar cal = Calendar.getInstance();
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private static StringBuilder clientsString = new StringBuilder("");

	/**
	 * Initializes the area where to write.
	 * 
	 * @param area
	 *            the server's area
	 */
	public Helper(JTextArea area) {
		areaToWrite = area;
		PropertyConfigurator.configure(Helper.class
				.getResourceAsStream("log4j.properties"));
	}

	/**
	 * Writes on the area, to a file and to the console.
	 * 
	 * @param message
	 *            the string that must be written.
	 */
	public static void write(String message) {
		areaToWrite.append(message + "\n");
		LOGGER.info(message);
	}

	/**
	 * Adds a client to the list with the all clients.
	 * 
	 * @param name
	 *            the name of the client
	 * @param client
	 *            its socket
	 * @throws IOException
	 *             when an error occurs
	 */
	public static void addToListWithClients(String name, Socket client)
			throws IOException {
		synchronized (clientsData) {
			clientsData
					.put(name,
							new Pair<Socket,PrintWriter>(client, new PrintWriter(client
									.getOutputStream(), true)));
		}
	}

	/**
	 * Checks if a client's name is appropriate.
	 * 
	 * @param name
	 *            the name which must be checked
	 * @return true if the name is correct
	 */
	public static boolean checkIfCorrect(String name) {
		synchronized (clientsData) {
			if (name.equals("")) {
				return false;
			}
			if (name.indexOf("[") != -1 || name.indexOf("]") != -1) {
				return false;
			}
			if (!clientsData.containsKey(name)) {
				return true;
			}
			return false;
		}
	}

	/**
	 * Sends a message to all the connected clients.
	 * 
	 * @param what
	 *            the message that must be sent
	 */
	public static void sendToAll(String what){
		synchronized (clientsData) {
			if (clientsData.size() != 0) {
				for (Map.Entry<String, Pair<Socket,PrintWriter> > entry : clientsData.entrySet()) {
					entry.getValue().getValue().println(what);
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
		synchronized (clientsData) {
			clientsData.remove(name);
		}
	}

	/**
	 * Makes a string with the all clients which is appropriate for sending. The
	 * clients are separated by [ because it can not be contained in them.
	 * 
	 * @return the string with the clients.
	 */
	public static String clientsToString() {
		clientsString.setLength(0);
		for (String key : clientsData.keySet()) {
			clientsString.append(key + "[");
		}
		return clientsString.toString();
	}

	/**
	 * Capitalizes the first character of a string.
	 * 
	 * @param what
	 *            the string
	 * @return the string with capitalized first character
	 */
	public static String upperFirst(String what) {
		return (what.substring(0, 1).toUpperCase() + what.substring(1));
	}
}
