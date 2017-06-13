package aufg1;

import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Created by felix on 28.05.17.
 */
public class ServerMain {
    private final static Logger LOGGER = Logger.getLogger(PrimeServer.class.getName());

    public static void main(String[] args) throws Exception {
        int port = 1234;



        System.out.println("Dynamisch?");
        Scanner sc = new Scanner(System.in);
        String s = null;
        int anzahl = 0;
        s = sc.nextLine();
        if (!s.equals("")) {
            System.out.println("Anzahl der Threads:");
            anzahl = sc.nextInt();
            new PrimeServer(port, anzahl, false).listen();

        } else {
            System.out.println("DYNAMISCH");

            new PrimeServer(port, -1, true).listen();
        }
    }

}
