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
        int port = 1234;
        String concurrencyMode="FIX_POOLED";
        Scanner sc = new Scanner(System.in);
        String s = null;
        System.out.println("Port[1234]>");
        s = sc.nextLine();
        if (!s.isEmpty()) {
            port = Integer.parseInt(s);
        }
        System.out.println("Concurrency mode [FIX_POOLED] >");
        s = sc.nextLine();
        if (!s.isEmpty()) {
             concurrencyMode= s;
        }


        if (concurrencyMode=="FIX_POOLED") {
            int anzahl=0;
            System.out.println("Anzahl der Threads:");
            anzahl = sc.nextInt();
            new PrimeServer(port, anzahl, false).listen();

        } else {
            System.out.println("DYNAMISCH");

            new PrimeServer(port, -1, true).listen();
        }


    }
}
