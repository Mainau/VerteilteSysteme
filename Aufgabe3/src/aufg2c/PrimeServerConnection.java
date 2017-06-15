package aufg2c;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;

/**
 * Created by felix on 28.05.17.
 */
public class PrimeServerConnection implements Runnable{


    private Component communication;
    private int port;
    private Long request;
    private long msgrcvTime;
    private static final Object valueLock2 = new Object();

    public PrimeServerConnection(Long request, long msgrcvTime, int port) {
        communication = new Component();
        if (port > 0)
            this.port = port;
        this.request = request;
        this.msgrcvTime=msgrcvTime;


    }



    private boolean primeService(long number) {
        for (long i = 2; i < Math.sqrt(number) + 1; i++) {
            if (number % i == 0)
                return false;
        }
        return true;
    }


    @Override
    public void run() {
        Object[] values;
        PrimeServer.increaseCounter();

        Boolean isPrime;

        try {
            long processing_start_time = System.nanoTime();
            isPrime=new Boolean(primeService(request.longValue()));
            long processing_end_time = System.nanoTime();
            long waitingTime=(processing_start_time-msgrcvTime);
            long processingTime = (processing_end_time - processing_start_time);
            values=new Object[]{isPrime, waitingTime, processingTime};
            communication.cleanup();
            synchronized (valueLock2) {
                communication.send(new Message("localhost", port, values), port, true);
            } communication.cleanup();
            PrimeServer.decreaseCounter();
            //System.out.println("erfolgreich");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

