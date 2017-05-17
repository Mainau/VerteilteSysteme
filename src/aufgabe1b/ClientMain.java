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




	public static void main(String args[]) {
		String input="";
		try {
			//for(int i=0;i<5;i++) {
				Thread th =new Thread(new MySocketClient(hostname, port,1 ));
				th.start();
			//}
			while(!input.equals("-1")){
				reader=new BufferedReader(new InputStreamReader(System.in ));
				input=reader.readLine();
				System.out.println(input);
				}

			th.interrupt();


		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
