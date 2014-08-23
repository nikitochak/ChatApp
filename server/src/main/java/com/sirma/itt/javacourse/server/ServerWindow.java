package com.sirma.itt.javacourse.server;

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
	private Server server;

	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu = new JMenu();
	private JPanel mainPanel = new JPanel();
	private JPanel headerPanel = new JPanel();
	private JLabel headerLabel = new JLabel("Welcome");
	private JPanel footerPanel = new JPanel();
	private JTextArea area = new JTextArea(7, 25);
	private JScrollPane scroll = new JScrollPane(area,
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private JButton stopButton = new JButton();

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
		server = new Server();
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
		menu.add(new JMenuItem("English"));
		menu.add(new JMenuItem("Bulgarian"));

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
			}

		});
		stopButton.setPreferredSize(new Dimension(200, 30));
	}

	/**
	 * Starts a server window.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new ServerWindow("Server Window");
	}
}
