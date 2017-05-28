package aufgabe3c;

import aufgabe2a.WebServerConnection;
import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrimeServer {
	private final static int PORT = 1234;
	private final static Logger LOGGER = Logger.getLogger(PrimeServer.class.getName());
	private static final Object valueLock = new Object();
	private Component communication;
	private int port = PORT;


	public PrimeServer(int port)
			throws IOException {
		communication = new Component();
		if (port > 0)
			this.port = PORT;

		setLogLevel(Level.FINER);
	}
	void setLogLevel(Level level) {
		for (Handler h : Logger.getLogger("").getHandlers())
			h.setLevel(level);
		LOGGER.setLevel(level);
		LOGGER.info("Log level set to " + level);
	}

	public void listen() {
		LOGGER.finer("Receiving ...");
		while (true) {
			try {
				Long request = null;
				System.out.println("Server: listening on port " + port);
				LOGGER.finer("Receiving ...");
				request=(Long)(communication.receive(port, true, false).getContent());
				LOGGER.fine(request.toString()+" received.");
				//communication.send(new Message("localhost",1234,true),1234,true);
				Thread connection =
					new Thread(new PrimeServerConnection(request, port, LOGGER));
				connection.start();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (ClassNotFoundException e){
				e.printStackTrace();
			}
		}
	}

}
