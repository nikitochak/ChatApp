package com.sirma.itt.javacourse.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Locale;
import java.util.ResourceBundle;

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
 */
@SuppressWarnings("serial")
public class ClientWindow extends JFrame {

	private String headerCondition = "headerLabel";
	private Locale enLocale = new Locale("en");
	private Locale bgLocale = new Locale("bg");
	private ResourceBundle bundle = ResourceBundle.getBundle(
			"com.sirma.itt.javacourse.client.Language", enLocale);
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
	private JMenuItem bgButton = new JMenuItem("Bulgarian");
	private JMenuItem enButton = new JMenuItem("English");
	private PreviousMessages prevMessages;

	/**
	 * The constructor which gets a title and constructs the window.
	 * 
	 * @param title
	 *            the title for the frame
	 */
	public ClientWindow(String title) {
		super(title);

		prevMessages = new PreviousMessages();

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

		menu = new JMenu("Select Language");
		menu.getAccessibleContext().setAccessibleDescription(
				"The menu for the languages.");

		enButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rewrite("en");
			}
		});
		menu.add(enButton);

		bgButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rewrite("bg");
			}
		});
		menu.add(bgButton);
		menuBar.add(menu);

		headerLabel.setSize(new Dimension(100, 19));
		headerLabel.setFont(new Font("BOLD", 5, 19));
		headerLabel.setForeground(Color.WHITE);

		headerPanel.setBackground(Color.darkGray);
		headerPanel.setPreferredSize(new Dimension(200, 50));
		headerPanel.add(headerLabel);

		footerPanel.setBackground(Color.DARK_GRAY);
		footerPanel.setPreferredSize(new Dimension(200, 50));
		footerPanel.add(menuBar);

		mainPanel.setBackground(Color.gray);
		
		sendTextField.setPreferredSize(new Dimension(150, 50));
		sendTextField.setText("Write your nickname.");
		
		connectButton.setPreferredSize(new Dimension(100, 30));
		connectButton.setText("Send");
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (sendTextField.getText().length() > 200) {
					headerCondition = "tooLong";
					headerLabel.setText(bundle.getString(headerCondition));
				} else if(sendTextField.getText().equals("Write your nickname.")){
					headerCondition = "invalidName";
					headerLabel.setText(bundle.getString(headerCondition));
				}else {
					if (!isConnected) {
						client.connect();

						client.sendToServer(sendTextField.getText());
						client.setName(sendTextField.getText());

						// waits until the client receives an message
						while (client.getMessage().equals("")) {

						}
						if (client.getMessage().equals("False")) {
							headerLabel.setFont(new Font("BOLD", 5, 15));
							headerCondition = "invalidName";
							headerLabel.setText(bundle
									.getString(headerCondition));
							client.stopConnection();
						} else if (client.getMessage().equals("NoServer")) {
							headerCondition = "noServer";
							headerLabel.setText(bundle
									.getString(headerCondition));
						} else {
							setConnectedFrame();
							isConnected = true;
						}
					} else {
						if (!sendTextField.getText().equals("")
								&& !sendTextField.getText().equals(
										"Write your message.")) {
							client.sendToServer(sendTextField.getText());
							prevMessages.saveToMemento(sendTextField.getText());
							sendTextField.setText("");
						}
					}
				}
			}
		});
	}

	/**
	 * This window appears when the client's nickname is correct. It contains an
	 * area for observing the conversation and another one for the available
	 * clients. Also there are two buttons - first for sending the message form
	 * the text field and the second for disconnecting form the server.
	 */
	public void setConnectedFrame() {
		setSize(400, 450);
		setTitle(bundle.getString("frame"));

		headerLabel.setSize(new Dimension(100, 20));
		headerLabel.setFont(new Font("BOLD", 5, 20));
		headerLabel.setForeground(Color.WHITE);
		headerCondition = "headerLabel";
		headerLabel.setText(bundle.getString(headerCondition));

		clientsArea.setEditable(false);
		conversationArea.setEditable(false);

		stopButton.setPreferredSize(new Dimension(100, 30));
		stopButton.setText(bundle.getString("stopButton"));
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.sendToServer("Disconnect");
				System.exit(0);
			}
		});

		connectButton.setText(bundle.getString("connectButton"));

		sendTextField.setPreferredSize(new Dimension(300, 50));
		sendTextField.setText("Write your message.");
		sendTextField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					String mess = prevMessages.restoreFromMemento();
					if (!mess.equals(null)) {
						sendTextField.setText(mess);
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		menu.setText(bundle.getString("menuName"));
		bgButton.setText(bundle.getString("bgButton"));
		enButton.setText(bundle.getString("enButton"));
		mainPanel.add(clientsPane);
		mainPanel.add(conversationPane);
		mainPanel.add(sendTextField);
		mainPanel.add(connectButton);
		mainPanel.add(stopButton);
	}

	/**
	 * Rewrites the names in the window depending on the chosen language.
	 * 
	 * @param string
	 *            the language.
	 */
	private void rewrite(String language) {
		if (language.equals("bg")) {
			bundle = ResourceBundle.getBundle(
					"com.sirma.itt.javacourse.client.Language", bgLocale);
		} else {
			bundle = ResourceBundle.getBundle(
					"com.sirma.itt.javacourse.client.Language", enLocale);
		}
		menu.setText(bundle.getString("menuName"));
		enButton.setText(bundle.getString("enButton"));
		bgButton.setText(bundle.getString("bgButton"));
		headerLabel.setText(bundle.getString(headerCondition));
		stopButton.setText(bundle.getString("stopButton"));
		connectButton.setText(bundle.getString("connectButton"));
		setTitle(bundle.getString("frame"));
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
