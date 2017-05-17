package aufgabe1b;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;

import static sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap.Byte0.runnable;

public class ClientMain {
	private static final int port=1234;
	private static final String hostname="localhost";
	private static MySocketClient client;
	private static BufferedReader reader;
	private String uniqueID;


	private class ClientInput implements Runnable{

		@Override
		public void run() {
			while (true){

			}
		}
	}

	public static void main(String args[]) {
		try {
			client=new MySocketClient(hostname,port);
			//System.out.print("Client: Enter name> ");
			reader=new BufferedReader(new InputStreamReader(System.in ));
			String clientName=reader.readLine();
			do{
				System.out.println(client.sendAndReceive("Test-"+uniqueID));
				clientName=reader.readLine();
			}while(clientName!="exit");

			client.disconnect();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
