package de.fhaachen.mazenet.Client;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.UnmarshalException;

import de.fhaachen.mazenet.generated.MazeCom;
import de.fhaachen.mazenet.networking.XmlInStream;

public class ListenerThread extends Thread {
	private XmlInStream in;
	private MazeComHandler mch;

	public ListenerThread(InputStream in, MazeComHandler mch) {
		this.in = new XmlInStream(in);
		this.mch = mch;
	}

	public void run() {
		while (true) {
			MazeCom mes = null;
			try {
				mes = in.readMazeCom();

			} catch (IOException e) {
				System.out.println("Client beenden"); //$NON-NLS-1$
				break;
			} catch (UnmarshalException e) {
				e.printStackTrace();
			}
			try {
				mes.getMcType();
			} catch (NullPointerException e) {
				System.out.println("MazeCom konnte nicht gelesen werden!"); //$NON-NLS-1$
				e.printStackTrace();
				System.out.println("Client beenden"); //$NON-NLS-1$
				break;
			}
			mch.processMazeCom(mes);

		}
	}

}
