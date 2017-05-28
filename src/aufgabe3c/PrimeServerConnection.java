package aufgabe3c;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by felix on 28.05.17.
 */
public class PrimeServerConnection implements Runnable {

    private Logger LOGGER;
    private static final Object valueLock = new Object();

    private Component communication;
    private int port;
    private Long request;

    PrimeServerConnection(Long request, int port, Logger logger) {
        communication = new Component();
        if (port > 0)
            this.port = port;
        this.LOGGER = logger;
        this.request = request;
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
        LOGGER.setLevel(level);
        LOGGER.info("Log level set to " + level);
    }


    @Override
    public void run() {

        try {
            communication.send(new Message("localhost",port, new Boolean(primeService(request.longValue()))), 3000, true);//"localhost",port,new Boolean(primeService(request.longValue()))),true);
            System.out.println("erfolgreich");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.fine("message sent.");
    }
}

