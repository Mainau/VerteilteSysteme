package aufg2b;

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
    private static long waitingTimeAVG=0;
    private static long waitingTimeCount=0;
    private static long processingTimeAVG=0;
    private static long processingTimeCount=0;
    private static long completeTimeAVG=0;
    private static long completeTimeCount=0;
    String hostname;
    int port;
    long initialValue, count;
    String requestMode;
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

    public static synchronized long getWaitingTimeAVG(long currentWaitingTime){
        waitingTimeCount++;
        return (waitingTimeAVG+=currentWaitingTime)/waitingTimeCount;
    }

    public static synchronized long getProcessingTimeAVG(long currentProcessingTime){
        processingTimeCount++;
        return (processingTimeAVG+=currentProcessingTime)/processingTimeCount;
    }

    public static synchronized long getCompleteTimeAVG(long currentCompleteTime){
        completeTimeCount++;
        return (completeTimeAVG+=currentCompleteTime)/completeTimeCount;
    }

    public void processNumber(long value) throws IOException, ClassNotFoundException {
        Object[]results=null;
        Boolean isPrime = null;
        long waitingTime, processingTime, completeTime;
        long sendTime, receiveTime;
        sendTime=System.nanoTime();
        communication.send(new Message(hostname, receivePort, value), 1234, true);
        communication.cleanup();

        results = (Object[])communication.receive(receivePort, true, false).getContent();
        receiveTime=System.nanoTime();
        completeTime=receiveTime-sendTime;
        communication.cleanup();
        isPrime=(Boolean)results[0];
        waitingTime=(long)results[1];
        processingTime=(long)results[2];
        synchronized (valueLock) {
            System.out.print("Client-" + clientID + ": " + value + ": ");
            System.out.print(isPrime.booleanValue() ? "prime" : "not prime");
            System.out.println(" | p: "+
                    processingTime +" ("+getProcessingTimeAVG(processingTime)+") ms |  w: "+
                    waitingTime +" ("+getWaitingTimeAVG(waitingTime)+") ms |  c: "+
                    completeTime +" ("+getCompleteTimeAVG(completeTime)+") ms | ");
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

        for (int i = 0; i < 5; i++) {
            new Thread(new PrimeClient(i)).start();
        }


        System.out.println("Welcome to " + CLIENT_NAME + "\n");


    }
}
