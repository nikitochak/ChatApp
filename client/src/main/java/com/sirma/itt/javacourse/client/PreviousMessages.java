package com.sirma.itt.javacourse.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which represents the Mamento design pattern.
 * 
 * @author Nikolay Ch
 */
public class PreviousMessages {

	private List<String> previousMessages = new ArrayList<>();
	private int toWhereIndex;

	/**
	 * Clears the list if there are some elements and sets the index to be zero,
	 * because the list is empty.
	 */
	public PreviousMessages() {
		toWhereIndex = 0;
		previousMessages.clear();
	}

	/**
	 * Saves to the list with the previous messages.
	 * 
	 * @param sentMessage
	 *            the message that have to be saved
	 */
	public void saveToMemento(String sentMessage) {
		previousMessages.add(sentMessage);
		toWhereIndex=previousMessages.size();
	}

	/**
	 * Restores the previous message.
	 * 
	 * @return the previous message
	 */
	public String restoreFromMemento() {
		if (toWhereIndex != 0) {
			toWhereIndex--;
			return previousMessages.get(toWhereIndex);
		} else {
			return null;
		}
	}

}
