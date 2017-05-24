package aufgabe2a;

import aufgabe1b.MySocketServer;

import java.io.IOException;

public class ServerMain {
    private static final int port = 1234;
    private static MySocketServer server;

    public static void main(String args[]) {
        try {
            WebServer web = new WebServer(8080, "/home/felix/Documents/dev/Kore/VerteilteSystemeAufg2/resources/aufgabe2a/WebServer.log");
            web.listen();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
