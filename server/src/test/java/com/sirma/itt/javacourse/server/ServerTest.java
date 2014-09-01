package com.sirma.itt.javacourse.server;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sirma.itt.javacourse.client.Client;

/**
 * Tests the starting and the stopping of the server.
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
	 * Tests the client connection without active server.
	 */
	@Test
	public void testClient() {
		client.connect();
		assertEquals(client.getMessage(), "NoServer");
	}

	/**
	 * Starts a server and checks it field that keeps whether the server is
	 * stopped. Then connects a client with an invalid name and checks the
	 * return. At the end connects a client with an appropriate name.
	 */
	@Test
	public void testSratingTheServer() {
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
