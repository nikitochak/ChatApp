package com.sirma.itt.javacourse.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
	private Socket client;
	private String name = "";
	private boolean isAlive = true;
	private StringBuilder message = new StringBuilder("");
	private StringBuilder allMessage = new StringBuilder("");

	public ClientThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {

		BufferedReader reader = null;
		PrintWriter writer;

		try {
			reader = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			writer = new PrintWriter(client.getOutputStream(), true);
			while (!reader.ready()) {

			}

			name = reader.readLine();
			Helper.write(Helper.getCurrentTime() + ": The client " + name
					+ " is trying to connect.");
			if (Helper.checkIfCorrect(name)) {
				writer.println("Welcome " + name);
				Helper.addToListWithClients(name, client);
				Helper.write(Helper.getCurrentTime() + ": The client " + name
						+ " has connected.");
				Helper.sendToAll("The client " + name + " has connected.");
				while (isAlive) {
					message.setLength(0);
					allMessage.setLength(0);

					

					message.append("<").append(name).append(">:")
							.append(reader.readLine());
					if (message.toString().equals("<" + name + ">:Disconnect")) {
						client.close();
						Helper.removeClient(name);
						isAlive = false;
					} else {
						allMessage.append(Helper.getCurrentTime()).append(
								message);
						Helper.write(allMessage.toString());
						Helper.sendToAll(message.toString());
					}
				}

			} else {
				writer.println("False");
				Helper.write(Helper.getCurrentTime() + ": The client " + name
						+ " has disconnected.");
				isAlive = false;
			}
		} catch (IOException e) {

		}
		Helper.write(Helper.getCurrentTime() + ":The client " + name
				+ " has disconnected.");
		Helper.sendToAll("The client " + name + " has disconnected.");

	}

}
