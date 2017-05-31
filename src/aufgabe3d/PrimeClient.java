package aufgabe3d;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;

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
    Boolean concurrent;
    static int counter = 0;

    private Component communication;
    private int receivePort = 5000;
    private int clientID;

    public PrimeClient(int id) {
        hostname = HOSTNAME;
        port = PORT;
        initialValue = INITIAL_VALUE;
        count = COUNT;
        concurrent = true;
        receivePort += increaseCounter();
        clientID=id;
        communication = new Component();
    }

    public void processNumber(long value) throws IOException, ClassNotFoundException {

        Boolean isPrime = null;
        communication.send(new Message(hostname, receivePort, value), 1234, true);
        communication.cleanup();

        isPrime = (Boolean) communication.receive(receivePort, true, false).getContent();
        communication.cleanup();
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
