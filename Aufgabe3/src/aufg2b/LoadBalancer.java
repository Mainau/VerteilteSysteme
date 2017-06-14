package aufg2b;

import rm.requestResponse.Component;
import rm.requestResponse.Message;
import rm.serverAdmin.ServerAdmin;
import rm.serverAdmin.ServerConfig;
import java.io.IOException;


/**
 * Created by felix on 13.06.17.
 */
public class LoadBalancer {
    private Component communication;
    private int anzahlServer, threadAnzahl, port;
    private ServerAdmin serverAdmin;
    private ServerConfig serverConfig;
    private Thread[] servers;

    public LoadBalancer(int port, int anzahlServer, int threadAnzahl) {
        this.port = port;
        this.anzahlServer = anzahlServer;
        this.threadAnzahl = threadAnzahl;
        this.communication=new Component();
        this.servers=new Thread[anzahlServer];

        try {
            this.serverAdmin = new ServerAdmin("/home/felix/Documents/dev/Kore/VerteilteSysteme/Aufgabe3/src/aufg2b/server.conf");
        } catch (IOException e) {
            e.printStackTrace();
        }
        createServers(anzahlServer);
    }

    public void createServers(int anzahl) {
        for (int i = 0; i < anzahl; i++) {

            try {

                servers[i] = new Thread(new PrimeServer(2000 + i, -1, true));
                servers[i].start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void listen() throws Exception{
        int resultClientPort;


        while (true) {
            try {

                Long request = null;
                Message msg = communication.receive(port, true, false);

                long msgrcvTime = System.nanoTime();
                request = (Long) msg.getContent();
                resultClientPort = msg.getPort();
                serverConfig = serverAdmin.bind();
                System.out.println(serverConfig.getReceivePort());
                communication.send(new Message("localhost", resultClientPort, request), serverConfig.getReceivePort(), true);

                serverAdmin.release(serverConfig);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
