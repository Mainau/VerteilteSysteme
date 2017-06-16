package aufg3;


import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;


public class PrimeServer implements PrimeServerInterface {
    private final static int PORT = 1234;
    private final static Logger LOGGER = Logger.getLogger(PrimeServer.class.getName());
    private int port = PORT;
    private static PrimeServer server;


    public boolean primeService(long number) throws RemoteException {
        for (long i = 2; i < Math.sqrt(number) + 1; i++) {
            if (number % i == 0)
                return false;
        }
        return true;
    }

    public void start(int port) throws RemoteException {
        System.out.println("Server läuft (Port:  "+port+")");
        PrimeServerInterface serverstub = (PrimeServerInterface) java.rmi.server.UnicastRemoteObject.exportObject(server, 0);
        Registry registry= LocateRegistry.createRegistry(port);
        registry.rebind("PrimeServer",serverstub);
    }

    public static void main(String[] args) throws Exception {
        (server=new PrimeServer()).start(1234);

    }





}
