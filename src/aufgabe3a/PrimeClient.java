package aufgabe3a;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import rm.requestResponse.Component;
import rm.requestResponse.Message;

public class PrimeClient {
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

	public PrimeClient(String hostname, int port, String requestMode, Boolean concurrent, long initialValue,
			long count) {
		this.hostname = hostname;
		this.port = port;
		this.initialValue = initialValue;
		this.count = count;
		this.requestMode = requestMode;
		this.concurrent = concurrent;
	}

	public void run() throws ClassNotFoundException, IOException, InterruptedException {
		communication = new Component();
		for (long i = initialValue; i < initialValue + count; i++) {
			processNumber(i);
		}

	}

	public void processNumber(long value) throws IOException, ClassNotFoundException {
		Boolean isPrime = null;
		int counter = 0;

		if (requestMode == "BLOCKING") {
			//Aufgabe 2d
			if (concurrent) {
				communication.send(new Message(hostname, port, new Long(value)));
				System.out.print(value + ": ");
				Thread pc = new Thread(new LoadingThread());
				pc.start();
				isPrime = (Boolean) communication.receive(port, true, true).getContent();
				pc.stop();
				System.out.println(isPrime.booleanValue() ? "prime" : "not prime");
			} else {
				communication.send(new Message(hostname, port, new Long(value)), false);
				isPrime = (Boolean) communication.receive(port, true, true).getContent();
				System.out.println(value + ": " + (isPrime.booleanValue() ? "prime" : "not prime"));
			}
			//Aufgabe 2c)
		} else {
			counter=0;
			communication.send(new Message(hostname, port, new Long(value)), false);
			System.out.print(value + ": ");
//			Thread pc = new Thread(new LoadingThread());
//			pc.start();
			while (isPrime == null) {
				try{
				isPrime = (Boolean) communication.receive(port, false, true).getContent();
				}catch(NullPointerException e){
					if(counter++>0)System.out.print(".");
					try {
						Thread.sleep(1000);
						
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					continue;
				}
			}
//			pc.stop();
			System.out.println(isPrime.booleanValue() ? "prime" : "not prime");
		}

	}

	public class PrimeClientThread implements Runnable {
		private long value;
		private Boolean isPrime = null;

		public PrimeClientThread(long value) {
			this.value = value;
		}

		@Override
		public void run() {

		
			synchronized (valueLock) {
				System.out.print(value + ": ");

				try {
					communication.send(new Message(hostname, port, new Long(value)), false);
					isPrime = (Boolean) communication.receive(port, true, true).getContent();

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println((isPrime ? "prime" : "not prime"));

				try {
					valueLock.notifyAll();
					valueLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		String hostname = HOSTNAME;
		int port = PORT;
		long initialValue = INITIAL_VALUE;
		long count = COUNT;
		String requestMode = REQUEST_MODE;
		Boolean concurrent = false;

		boolean doExit = false;

		String input;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Welcome to " + CLIENT_NAME + "\n");

		while (!doExit) {
			System.out.print("Server hostname [" + hostname + "] > ");
			input = reader.readLine();
			if (!input.equals(""))
				hostname = input;

			System.out.print("Server port [" + port + "] > ");
			input = reader.readLine();
			if (!input.equals(""))
				port = Integer.parseInt(input);

			System.out.print("Request mode [" + requestMode + "] > ");
			input = reader.readLine();
			if (!input.equals(""))
				requestMode = input;

			if (requestMode == "BLOCKING") {
				System.out.print("Concurrent [" + concurrent + "] > ");
				input = reader.readLine();
				if (!input.equals("")) {
					concurrent = true;
				}
			}

			System.out.print("Prime search initial value [" + initialValue + "] > ");
			input = reader.readLine();
			if (!input.equals(""))
				initialValue = Integer.parseInt(input);

			System.out.print("Prime search count [" + count + "] > ");
			input = reader.readLine();
			if (!input.equals(""))
				count = Integer.parseInt(input);

			new PrimeClient(hostname, port, requestMode, concurrent, initialValue, count).run();

			System.out.println("Exit [n]> ");
			input = reader.readLine();
			if (input.equals("y") || input.equals("j"))
				doExit = true;

		}
	}
}
