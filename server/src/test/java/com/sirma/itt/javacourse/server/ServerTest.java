package com.sirma.itt.javacourse.server;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sirma.itt.javacourse.client.Client;

/**
 * Tests the starting of the server and the connection with a client.
 * 
 * @author Nikolay Ch
 */
public class ServerTest {
	private static ServerWindow window;
	private static Client client;

	/**
	 * Initializes the server window.
	 */
	@BeforeClass
	public static void initialize() {
		client = new Client();
	}

	/**
	 * Tries to connect to a server but without starting one. Then starts a
	 * server and checks it's field that keeps whether the server is stopped. Then
	 * connects a client with an invalid name and checks the return. At the end
	 * connects a client with an appropriate name.
	 */
	@Test
	public void testSratingTheServer() {
		client.connect();
		assertEquals(client.getMessage(), "NoServer");
		window = new ServerWindow("Server");
		assertFalse(window.getServer().getIsStopped());
		client.connect();
		client.start();
		client.sendToServer("Won't be connected[]");
		while (client.getMessage().equals("")) {

		}
		assertEquals(client.getMessage(), "False");

		client.connect();
		client.sendToServer("ivan");
		while (client.getMessage().equals("")) {

		}
		assertEquals(client.getMessage().contains("["), true);
	}
}
