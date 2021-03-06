package aufg2a;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.Executors.newFixedThreadPool;

public class PrimeServer {
    private final static int PORT = 1234;
    private final static Logger LOGGER = Logger.getLogger(PrimeServer.class.getName());
    private Component communication;
    private int port = PORT;
    private int resultClientPort;
    private static final Object counterLock = new Object();
    //private static int counter;
    private static final AtomicLong counter = new AtomicLong();
    private int threadAnzahl;
    private Boolean dynamic;


    public PrimeServer(int port, int threadAnzahl, boolean dynamic)
            throws IOException {
        communication = new Component();
        this.threadAnzahl = threadAnzahl;
        this.dynamic = dynamic;

        if (port > 0)
            this.port = PORT;

        setLogLevel(Level.FINER);
    }

    void setLogLevel(Level level) {
        for (Handler h : Logger.getLogger("").getHandlers())
            h.setLevel(level);
        LOGGER.setLevel(level);
        LOGGER.info("Log level set to " + level);
    }

    public static long increaseCounter() {
        //synchronized (counterLock) {
          //  return ++counter;
        //}
        return counter.incrementAndGet();
    }

    public static long getCounter() {

        return counter.get();
    }

    public static long decreaseCounter() {
        //synchronized (counterLock) {
          //  return --counter;
        //}
        return counter.decrementAndGet();
    }

    private class CounterUpdate implements Runnable {
       long counter;
        public void run() {
            do{
                counter= PrimeServer.getCounter();
                System.out.println("Threadanzahl: "+counter);

            }while (counter>0);
        }

    }


    public void listen() {
        ExecutorService executorService;
        //LOGGER.finer("Receiving ...");
        if (dynamic) {
            executorService = newCachedThreadPool();
        }else{
            executorService=newFixedThreadPool(threadAnzahl);
        }
        while (true) {
            try {

                Long request = null;
               // System.out.println("Server: listening on port " + port);
               // LOGGER.finer("Receiving ...");
                Message msg = communication.receive(port, true, false);
                long msgrcvTime= System.nanoTime();
                //Thread counterUpdate= new Thread(new CounterUpdate());
                //counterUpdate.start();
                request = (Long) msg.getContent();
                resultClientPort = msg.getPort();
                executorService.execute(new PrimeServerConnection(request, msgrcvTime, resultClientPort, LOGGER));
               // LOGGER.fine(request.toString() + " received.");
               // System.out.println("Server"+counter.get());





            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
