package aufg2b;

import rm.serverAdmin.ServerConfig;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Created by felix on 28.05.17.
 */
public class ServerMain {
    private final static Logger LOGGER = Logger.getLogger(PrimeServer.class.getName());

    public static void main(String[] args) throws Exception {
/*
        Scanner sc = new Scanner(System.in);
        int anzahl = 0;
        System.out.println("Anzahl der Threads:");
        anzahl = sc.nextInt();

*/
        new LoadBalancer(1234, 3, -1).listen();


    }
}
