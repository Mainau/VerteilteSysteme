package aufgabe3b;

import java.io.IOException;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

public class PrimeClient implements Runnable {
    private static final String HOSTNAME = "localhost";
    private static final int PORT = 1234;
    private static final long INITIAL_VALUE = (long) 1e17;
    private static final long COUNT = 20;
    private static final String CLIENT_NAME = PrimeClient.class.getName();
    private static final String REQUEST_MODE = "BLOCKING";
    private static final Object valueLock = new Object();

    private Component communication;
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
    }

    public PrimeClient(int id) {
        hostname = HOSTNAME;
        port = PORT;
        initialValue = INITIAL_VALUE;
        count = COUNT;
        requestMode = REQUEST_MODE;
        concurrent = true;
        this.id = id;
    }

    public void run() {
        communication = new Component();
        for (long i = initialValue; i < initialValue + count; i++) {
            try {
                processNumber(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void processNumber(long value) throws IOException, ClassNotFoundException {
        Boolean isPrime = null;
        int counter = 0;
        synchronized (valueLock) {

            if (requestMode == "BLOCKING") {

                if (concurrent) {
                    communication.send(new Message(hostname, port, new Long(value)), port, true);
                    System.out.print("Client-" + id + ": " + value + ": ");
                    Thread pc = new Thread(new LoadingThread());
                    pc.start();
                    isPrime = (Boolean) communication.receive(port, true, true).getContent();
                    pc.stop();
                    System.out.println(isPrime.booleanValue() ? "prime" : "not prime");
                } else {
                    communication.send(new Message(hostname, port, new Long(value)), port, false);
                    isPrime = (Boolean) communication.receive(port, true, true).getContent();
                    System.out.println("Client-" + id + ": " + value + ": " + (isPrime.booleanValue() ? "prime" : "not prime"));
                }
            } else {
                counter = 0;
                communication.send(new Message(hostname, port, new Long(value)), port, false);
                System.out.print(value + ": ");

                while (isPrime == null) {
                    try {
                        isPrime = (Boolean) communication.receive(port, false, true).getContent();
                    } catch (NullPointerException e) {
                        if (counter++ > 0) System.out.print(".");
                        try {
                            Thread.sleep(1000);

                        } catch (InterruptedException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        continue;
                    }

                    System.out.println(isPrime.booleanValue() ? "prime" : "not prime");
                }


            }
        }
    }


    public class LoadingThread implements Runnable {

        @Override
        public void run() {

            Boolean isPrime = null;
            while (true) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.print(".");
            }

        }
    }


    public static void main(String args[]) throws IOException, ClassNotFoundException, InterruptedException {

        for (int i = 0; i < 10; i++) {
            new Thread(new PrimeClient(i)).start();
        }


        System.out.println("Welcome to " + CLIENT_NAME + "\n");


    }
}
