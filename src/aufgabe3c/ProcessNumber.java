package aufgabe3c;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;

/**
 * Created by felix on 29.05.17.
 */
public class ProcessNumber implements Runnable {
    long number;
    Component communication;
    String hostname = "localhost";
    private static final Object valueLock = new Object();
    private static final Object portLock = new Object();
    private static final Object counterLock = new Object();
    static int counter=0;
    private int receivePort = 5000;
    private int clientID;

    public ProcessNumber(long number, int clientID) {
        this.number = number;
        receivePort += increaseCounter();
        this.clientID = clientID;
        communication=new Component();


    }

    public static int getCounter() {
        synchronized (counterLock) {
            return counter;
        }
    }

    public int increaseCounter() {
        synchronized (counterLock) {
            return ++counter;
        }
    }

    public void run() {
        try {
            processNumber(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void processNumber(long value) throws IOException, ClassNotFoundException {
        Boolean isPrime = null;

        synchronized (portLock) {

            communication.send(new Message(hostname, receivePort, value), 1234, true);
            communication.cleanup();
        }

        //Thread pc = new Thread(new LoadingThread());
        //pc.start();


        isPrime = (Boolean) communication.receive(receivePort, true, false).getContent();
        communication.cleanup();


        //pc.stop();
        synchronized (valueLock) {
            System.out.print("Client-" + clientID + ": " + value + ": ");
            System.out.println(isPrime.booleanValue() ? "prime" : "not prime");
        }

    }
}