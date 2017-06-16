package aufg3;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PrimeClient implements Runnable {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 20;
    private static final String CLIENT_NAME = PrimeClient.class.getName();
    private static final Object valueLock = new Object();
    private static final Object counterLock = new Object();
    String hostname;
    int port;
    long initialValue, count;
    String requestMode;
    Boolean concurrent;
    static int counter = 0;
    private PrimeServerInterface server;
    private Registry registry;


    private int receivePort = 5000;
    private int clientID;

    public PrimeClient(int id) {
        hostname = HOSTNAME;
        port = PORT;
        initialValue = INITIAL_VALUE;
        count = COUNT;
        concurrent = true;
        receivePort += increaseCounter();
        clientID = id;
        try {
            registry= LocateRegistry.getRegistry(hostname, port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void processNumber(long value) throws IOException, ClassNotFoundException {

        try {
            server=(PrimeServerInterface)registry.lookup("PrimeServer");
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        Boolean isPrime = null;
        isPrime=server.primeService(value);
        synchronized (valueLock) {
            System.out.print("Client-" + clientID + ": " + value + ": ");
            System.out.println(isPrime.booleanValue() ? "prime" : "not prime");
        }

    }

    public int increaseCounter() {
        synchronized (counterLock) {
            return ++counter;
        }
    }


    public void run() {
        try {

            for (long i = initialValue; i < initialValue + count; i++) {

                // new Thread(new ProcessNumber(i,id)).start();

                processNumber(i);


            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {

        for (int i = 0; i < 10; i++) {
            new Thread(new PrimeClient(i)).start();
        }


        System.out.println("Welcome to " + CLIENT_NAME + "\n");


    }
}
