package aufgabe2a;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
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

    public WebServer(int port, String filename){
        this.port = port;
        this.filename = filename;
        setupLogging();
    }

    public void listen() throws IOException {
        server = new ServerSocket(port);
        System.out.println("Listening for connection on port 8080 ....");
        while (true) {
                socket = server.accept();
                Date today = new Date();
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
                socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));

        }
    }

    public void sendFile(String path) throws Exception{
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {

            File myFile = new File(path);
            byte [] mybytearray  = new byte [(int)myFile.length()];
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
            bis.read(mybytearray,0,mybytearray.length);
            os = socket.getOutputStream();
            logger.info("Sending " + path + "(" + mybytearray.length + " bytes)");
            os.write(mybytearray,0,mybytearray.length);
            os.flush();
            logger.info("Done.");
        }
        finally {
            if (bis != null) bis.close();
            if (os != null) os.close();
            if (socket!=null) socket.close();
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
        WebServer web = new WebServer(8080,"/home/felix/Documents/dev/Kore/VerteilteSystemeAufg2/resources/aufgabe2a/WebServer.log");
        web.listen();


    }


}
