package aufgabe3c;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;

public class PrimeClient implements Runnable {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 20;
    private static final String CLIENT_NAME = PrimeClient.class.getName();




    public Component communication;
    String hostname;
    int port;
    long initialValue, count;
    String requestMode;
    Boolean concurrent;
    int id;

    public PrimeClient(String hostname, int port, String requestMode, Boolean concurrent, long initialValue,
                       long count) {
        this.hostname = hostname;
        this.port = port;
        this.initialValue = initialValue;
        this.count = count;
        this.requestMode = requestMode;
        this.concurrent = concurrent;
        this.communication = new Component();
    }


    public PrimeClient(int id) {
        hostname = HOSTNAME;
        port = PORT;
        initialValue = INITIAL_VALUE;
        count = COUNT;
        concurrent = true;
        this.id = id;
        this.communication = new Component();
    }

    public void run() {

        for (long i = initialValue; i < initialValue + count; i++) {

                new Thread(new ProcessNumber(i,id)).start();

        }

    }

    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {

        for (int i = 0; i < 2; i++) {
            new Thread(new PrimeClient(i)).start();
        }


        System.out.println("Welcome to " + CLIENT_NAME + "\n");


    }
}
