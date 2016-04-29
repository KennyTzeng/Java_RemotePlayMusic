
import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TargetFrame extends JFrame {
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private Socket connection;
	private ObjectInputStream input;
	private String receivedMessage;
	private Thread connectThread;
	private Runtime r;
	private Process p;

	public TargetFrame() {
		super("Target");

		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		add(scrollPane, BorderLayout.CENTER);

		r = Runtime.getRuntime();
		
		 try {
			p = r.exec("C:/Program Files (x86)/Google/Chrome/Application/chrome.exe");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		connectThread = new Thread() {
			public void run() {
				while (true) {
					try {
						receivedMessage = (String) input.readObject();
						displayMessage(receivedMessage);
						p.destroy();
						p = r.exec("C:/Program Files (x86)/Google/Chrome/Application/chrome.exe "+ receivedMessage);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		try {
			connection = new Socket(InetAddress.getByName("127.0.0.1"),
					9896);
			textArea.append("已連接Server端\n");
			input = new ObjectInputStream(connection.getInputStream());
			textArea.append("輸入串流已建立\n");
			connectThread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void displayMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				textArea.append(message + "\n");
			}
		});
	}

}
