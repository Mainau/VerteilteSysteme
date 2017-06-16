package aufg3;

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

    private Component communication;
    private int port;
    private Long request;


    public PrimeServerConnection(Long request, int port) {
        communication = new Component();
        if (port > 0)
            this.port = port;

        this.request = request;


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


        Boolean isPrime;

        try {
            isPrime=new Boolean(primeService(request.longValue()));

            communication.send(new Message("localhost",port, isPrime), port, true);//"localhost",port,new Boolean(primeService(request.longValue()))),true);

            //System.out.println("erfolgreich");

        } catch (IOException e) {
            e.printStackTrace();
        }
        // LOGGER.fine("message sent.");
    }
}

