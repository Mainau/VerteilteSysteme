package aufgabe2a;
import aufgabe1b.MySocketServerConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    private ServerSocket socket;
    private int port;
    String loggerfilepath;

    public WebServer(int port, String loggerfilepath)
            throws IOException {
        this.port = port;
        socket = new ServerSocket(port);
        this.loggerfilepath=loggerfilepath;
    }

    public void listen() {
        while (true) {
            try {
                System.out.println("Server: listening on port " + port);
                Socket incomingConnection = socket.accept();
                WebServerConnection connection =
                        new WebServerConnection(incomingConnection, loggerfilepath);
                connection.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}