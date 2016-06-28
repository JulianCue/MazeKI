package de.fhaachen.mazenet.Client;

import java.io.IOException;
import java.net.Socket;

import de.fhaachen.mazenet.config.Settings;
import de.fhaachen.mazenet.networking.MazeComMessageFactory;
import de.fhaachen.mazenet.networking.XmlOutStream;

public class Client {
	private Socket clientSocket;
	private ListenerThread listenerThread;
	private MazeComMessageSender mazeComSender;
	private MazeComHandler mchandler;
	private String port;

	public Client(String port) {
		this.port = port;
	}

	public void init() {
		Logic logic = new CleverLogic((new AdvancedLogic(new RandomLogic())));
		try {
			clientSocket = new Socket(port, Settings.PORT);
			MazeComMessageFactory of = new MazeComMessageFactory();
			mazeComSender = new MazeComMessageSender(new XmlOutStream(
					clientSocket.getOutputStream()), of, logic);
			mchandler = new MazeComHandler(mazeComSender);
			listenerThread = new ListenerThread(clientSocket.getInputStream(),
					mchandler);
		} catch (IOException e) {
			e.printStackTrace();
		}

		listenerThread.start();
		mazeComSender.login("hehexd"); //$NON-NLS-1$
	}

	public static void main(String args[]) {
		if (args.length == 0) {
			Client c = new Client("127.0.0.1"); //$NON-NLS-1$
			c.init();
		}

		else {
			Client c = new Client(args[0]);
			c.init();
		}

	}
}
