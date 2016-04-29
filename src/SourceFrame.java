
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SourceFrame extends JFrame {
	private JTextArea textArea;
	private JTextField inputField;
	private JPanel panel;
	private JScrollPane scrollPane;
	private ServerSocket serverSocket;
	private Socket connection;
	private Thread acceptThread;
	private ObjectOutputStream output;

	public SourceFrame() {
		super("Source");

		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.CENTER);
		panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(new JLabel("輸入網址", JLabel.CENTER));
		inputField = new JTextField();
		panel.add(inputField);
		add(panel, BorderLayout.SOUTH);
		
		inputField.addActionListener(new textFieldListener());

		acceptThread = new Thread() {
			public void run() {
				try {
					connection = serverSocket.accept();
					textArea.append("至閎端已連線\n");
					output = new ObjectOutputStream(
							connection.getOutputStream());
					output.flush();
					textArea.append("輸出串流已建立\n");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		try {
			serverSocket = new ServerSocket(9896);
			textArea.append("伺服器端已上線\n");
			acceptThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class textFieldListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			// TODO Auto-generated method stub
			sendData(event.getActionCommand());
			displayMessage(event.getActionCommand());
			inputField.setText("");
		}
	}

	public void sendData(String message) {
		try {
			output.writeObject(message);
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void displayMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.append(message+"\n");
			}
		});
	}

}
