package aufgabe3c;

import java.util.logging.Logger;

/**
 * Created by felix on 28.05.17.
 */
public class ServerMain {
    private final static Logger LOGGER = Logger.getLogger(PrimeServer.class.getName());
    public static void main(String[] args) throws Exception{
        int port = 1234;

        new PrimeServer(port).listen();
    }
}
