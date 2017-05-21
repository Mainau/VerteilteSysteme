package aufgabe2a;

import java.io.*;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Created by felix on 18.05.17.
 */
public class WebServer {
    private int port;
    private String filename;
    private Logger logger;
    private Socket socket;
    private ServerSocket server;
    private PrintStream out;
    private BufferedReader in;

    public WebServer(int port, String filename) {
        this.port = port;
        this.filename = filename;
        setupLogging();
    }

    public void listen() throws IOException {
        Boolean validURL = false;
        server = new ServerSocket(port);
        logger.info("Listening for connection on port 8080 ....");
        while (true) {
            socket = server.accept();
            logger.info("Verbindung akzeptiert.");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            String path = readRequest();
            sendFile(path);
            socket.close();
        }


    }

    public String readRequest() {
        try {
            String request = in.readLine(); // Now you get GET index.html HTTP/1.1`
            logger.info("HTTP-Request: " + request);
            String[] requestParam = request.split(" ");
            String path = requestParam[1];
            if (isCorrectURL(path)) return path;
            else return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isCorrectURL(String path) {
        try {
            URL url = new URL("http:\\\\" + path);
            logger.info("Korrekte URL");
            return true;
        } catch (MalformedURLException e) {
            out.println("HTTP/1.1 404");
            out.println("Content-Type: text/html");
            out.println("\r\n");
            out.println("<h1> Ungültige URL </h1>");
            // out.close();
            logger.info("URL nicht korrekt");
            e.printStackTrace();
            return false;
        }
    }


    public void sendFile(String path) {
        if (!path.equals(null)) {
            try {

                FileInputStream is = new FileInputStream(path);
                //HTTP-Header senden
                out.print("HTTP/1.0 200 OK\r\n");
                byte[] buf = new byte[256];
                int len;
                while ((len = is.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                logger.info("HTML-Dokument versendet.");
                // is.close();
            } catch (FileNotFoundException e) {
                out.println("HTTP/1.1 404");
                out.println("Content-Type: text/html");
                out.println("\r\n");
                out.println("<h1> File not found </h1>");
                logger.info("HTML-Dokument nicht vorhanden.");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setupLogging() {
        logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {

            // This block configure the logger with handler and formatter
            fh = new FileHandler(filename);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages
            logger.info("My first log");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String args[]) throws Exception {
        WebServer web = new WebServer(8080, "/home/felix/Documents/dev/Kore/VerteilteSystemeAufg2/resources/aufgabe2a/WebServer.log");
        web.listen();


    }


}
