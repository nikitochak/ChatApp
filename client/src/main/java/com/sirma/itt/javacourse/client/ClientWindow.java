package com.sirma.itt.javacourse.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Represents the window where the client enters his nickname and if it's valid
 * he can chat to the rest clients.
 * 
 * @author Nikolay Ch
 * 
 */
@SuppressWarnings("serial")
public class ClientWindow extends JFrame {

	private boolean isConnected;
	private Client client;
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu = new JMenu();
	private JTextArea clientsArea = new JTextArea(5, 30);
	private JScrollPane clientsPane = new JScrollPane(clientsArea,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private JTextArea conversationArea = new JTextArea(5, 30);
	private JScrollPane conversationPane = new JScrollPane(conversationArea,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private JPanel mainPanel = new JPanel();
	private JPanel headerPanel = new JPanel();
	private JLabel headerLabel = new JLabel("Welcome");
	private JPanel footerPanel = new JPanel();
	private JButton connectButton = new JButton();
	private JButton stopButton = new JButton();
	private JTextField sendTextField = new JTextField();

	/**
	 * The constructor which gets a title and constructs the window.
	 * 
	 * @param title
	 *            the title for the window
	 */
	public ClientWindow(String title) {
		super(title);
		client = new Client(conversationArea, clientsArea);
		client.start();
		setUnconnectedFrame();

		mainPanel.add(sendTextField);
		mainPanel.add(connectButton);
		add(headerPanel, BorderLayout.NORTH);
		add(footerPanel, BorderLayout.SOUTH);
		add(mainPanel);

		pack();
	}

	/**
	 * Constructs the frame. It contains a button for submitting the nickname
	 * and field where the customer have to write it. If he writes an invalid
	 * nickname he will be returned on this window again.
	 */
	public void setUnconnectedFrame() {
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(200, 250));
		setResizable(false);
		setLocationRelativeTo(null);

		menu = new JMenu("Select language");
		menu.getAccessibleContext().setAccessibleDescription(
				"The menu for the languages.");
		menu.add(new JMenuItem("English"));
		menu.add(new JMenuItem("Bulgarian"));

		menuBar.add(menu);

		headerLabel.setSize(new Dimension(100, 40));
		headerLabel.setFont(new Font("BOLD", 5, 40));
		headerLabel.setForeground(Color.WHITE);

		headerPanel.setBackground(Color.darkGray);
		headerPanel.setPreferredSize(new Dimension(200, 50));
		headerPanel.add(headerLabel);

		footerPanel.setBackground(Color.DARK_GRAY);
		footerPanel.setPreferredSize(new Dimension(200, 50));
		footerPanel.add(menuBar);

		mainPanel.setBackground(Color.gray);

		connectButton.setPreferredSize(new Dimension(100, 30));
		connectButton.setText("Connect");
		connectButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!isConnected) {
					client.connect();
					client.sendToServer(sendTextField.getText());
					ClientsList.getInstance().addClient(sendTextField.getText(), client);
					
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
					}
					if (client.getMessage().equals("False")) {
						headerLabel.setFont(new Font("BOLD", 5, 15));
						headerLabel.setText("Invalid nick. " + "\n"
								+ "Try Again");
						client.stopConnection();
					} else if (client.getMessage().equals("NoServer")) {
						headerLabel.setText("No Server");
					} else {
						setConnectedFrame();
						isConnected = true;
					}
				} else {
					client.sendToServer(sendTextField.getText());
				}
			}
		});

		sendTextField.setPreferredSize(new Dimension(150, 50));
		sendTextField.setText("Write your nickname.");
	}

	/**
	 * This is the window when the client is connected and its nickname is
	 * valid.
	 */
	public void setConnectedFrame() {
		setSize(400, 450);

		headerLabel.setSize(new Dimension(100, 40));
		headerLabel.setFont(new Font("BOLD", 5, 40));
		headerLabel.setForeground(Color.WHITE);
		headerLabel.setText("Welcome");

		clientsArea.setEditable(false);
		conversationArea.setEditable(false);

		stopButton.setPreferredSize(new Dimension(100, 30));
		stopButton.setText("Disconnect");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.sendToServer("Disconnect");
				System.exit(0);
			}
		});

		connectButton.setText("Send");

		sendTextField.setPreferredSize(new Dimension(300, 50));
		sendTextField.setText("Write your message.");

		mainPanel.add(clientsPane);
		mainPanel.add(conversationPane);
		mainPanel.add(sendTextField);
		mainPanel.add(connectButton);
		mainPanel.add(stopButton);
	}

	/**
	 * Starts the client's window.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new ClientWindow("Client");
	}
}
