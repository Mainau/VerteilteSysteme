package aufg2a;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by felix on 28.05.17.
 */
public class PrimeServerConnection implements Runnable{

    private Logger LOGGER;
    private Component communication;
    private int port;
    private Long request;
    private long msgrcvTime;

    public PrimeServerConnection(Long request, long msgrcvTime, int port, Logger logger) {
        communication = new Component();
        if (port > 0)
            this.port = port;
        this.LOGGER = logger;
        this.request = request;
        this.msgrcvTime=msgrcvTime;
        setLogLevel(Level.FINER);

    }



    private boolean primeService(long number) {
        for (long i = 2; i < Math.sqrt(number) + 1; i++) {
            if (number % i == 0)
                return false;
        }
        return true;
    }

    void setLogLevel(Level level) {
        for (Handler h : Logger.getLogger("").getHandlers())
            h.setLevel(level);
        // LOGGER.setLevel(level);
        // LOGGER.info("Log level set to " + level);
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
            communication.send(new Message("localhost",port, values), port, true);//"localhost",port,new Boolean(primeService(request.longValue()))),true);
            PrimeServer.decreaseCounter();
            //System.out.println("erfolgreich");

        } catch (IOException e) {
            e.printStackTrace();
        }
        // LOGGER.fine("message sent.");
    }
}

