package com.sirma.itt.javacourse.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

/**
 * Represents the window for the server. It contains an area for observing the
 * conversation, a button for stopping the server and a menu for choosing the
 * language.
 * 
 * @author Nikolay Ch
 * 
 */
@SuppressWarnings("serial")
public class ServerWindow extends JFrame {

	private Locale bgLocale = new Locale("bg");
	private Locale enLocale = new Locale("en");
	private ResourceBundle bundle = ResourceBundle.getBundle(
			"com.sirma.itt.javacourse.server.Language", enLocale);
	private Server server;
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu = new JMenu();
	private JPanel mainPanel = new JPanel();
	private JPanel headerPanel = new JPanel();
	private JLabel headerLabel = new JLabel("Welcome!");
	private JPanel footerPanel = new JPanel();
	private JTextArea area = new JTextArea(7, 25);
	private JScrollPane scroll = new JScrollPane(area,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private JButton stopButton = new JButton();
	private JMenuItem enButton = new JMenuItem("English");
	private JMenuItem bgButton = new JMenuItem("Bulgarian");

	/**
	 * Gets a title for the frame constructs its components and initializes the
	 * server.
	 * 
	 * @param title
	 *            the title for the frame
	 */
	public ServerWindow(String title) {
		super(title);
		constructFrame();
		server = Server.startServer();
		server.start();
		new Helper(area);

		add(headerPanel, BorderLayout.NORTH);
		add(footerPanel, BorderLayout.SOUTH);
		mainPanel.add(scroll);
		mainPanel.add(stopButton);

		add(mainPanel);
		pack();
	}

	/**
	 * Constructs the frame and its components. It contains a text area for the
	 * messages and a button for stopping the server.
	 */
	public void constructFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(300, 300));
		setResizable(false);
		setBackground(Color.gray);
		setVisible(true);
		setLocationRelativeTo(null);

		menu = new JMenu("Select language");
		menu.getAccessibleContext().setAccessibleDescription(
				"The menu for the languages.");
		bgButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rewrite("bg");
			}
		});
		enButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rewrite("en");
			}
		});
		menu.add(bgButton);
		menu.add(enButton);

		menuBar.add(menu);

		mainPanel.setPreferredSize(new Dimension(300, 200));

		headerLabel.setSize(new Dimension(100, 40));
		headerLabel.setFont(new Font("BOLD", 5, 40));
		headerLabel.setForeground(Color.WHITE);

		headerPanel.setBackground(Color.darkGray);
		headerPanel.setPreferredSize(new Dimension(200, 50));
		headerPanel.add(headerLabel);

		footerPanel.setBackground(Color.DARK_GRAY);
		footerPanel.setPreferredSize(new Dimension(200, 50));
		footerPanel.add(menuBar);

		area.setEnabled(false);

		stopButton.setText("Stop Server");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server.stopServer();
				System.exit(0);
			}
		});
		stopButton.setPreferredSize(new Dimension(200, 30));
	}

	/**
	 * Rewrites all names depending on the chosen language.
	 * 
	 * @param language
	 *            the language
	 */
	public void rewrite(String language) {
		if (language.equals("bg")) {
			bundle = ResourceBundle.getBundle(
					"com.sirma.itt.javacourse.server.Language", bgLocale);
		} else {
			bundle = ResourceBundle.getBundle(
					"com.sirma.itt.javacourse.server.Language", enLocale);
		}
		setTitle(bundle.getString("title"));
		menu.setText(bundle.getString("menu"));
		enButton.setText(bundle.getString("enButton"));
		bgButton.setText(bundle.getString("bgButton"));
		headerLabel.setText(bundle.getString("headerLabel"));
		stopButton.setText(bundle.getString("stopButton"));
	}

	/**
	 * Getter for the server thread.
	 * 
	 * @return the server thread
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * Starts a server window.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new ServerWindow("Server");
		} catch (NullPointerException e) {
			System.exit(0);
		}
	}
}
