package com.sirma.itt.javacourse.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Represent a list with the clients. Contains a methods for inserting new
 * client and removing old one. Realizes the singleton design pattern.
 * 
 * @author Nikolay Ch
 * 
 */
public class ClientsList {
	private static Map<String, Client> clientsList;
	private static StringBuilder clients;
	private static ClientsList instance = new ClientsList();

	/**
	 * Initializes the map with the clients.
	 */
	private ClientsList() {
		clientsList = new HashMap<>();
		clients = new StringBuilder("");
	}

	/**
	 * Adds new client to the list with all the clients.
	 * 
	 * @param name
	 *            the name of the client
	 * @param client
	 *            the client's thread
	 */
	public void addClient(String name, Client client) {
		clientsList.put(name, client);
		clients.append(name + "\n");
		rewriteAll(clients.toString());
	}

	/**
	 * Removes a client from the list with the clients.
	 * 
	 * @param name
	 *            the name of the client
	 */
	public void removeClient(String name) {
		clientsList.remove(name);
		clients.delete(clients.indexOf("\n" + name),
				clients.lastIndexOf("\n" + name));
		rewriteAll(clients.toString());
	}

	/**
	 * Rewrites the clients list on the clients's area if there is change.
	 */
	public void rewriteAll(String what) {
		for (String key : clientsList.keySet()) {
			clientsList.get(key).rewriteClients(what);
		}
	}

	/**
	 * Returns an instance of the class which realizes the singleton design
	 * pattern.
	 * 
	 * @return the instance of the class
	 */
	public static ClientsList getInstance() {
		return instance;
	}
}
