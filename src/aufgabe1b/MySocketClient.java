package aufgabe1b;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.UUID;

public class MySocketClient implements Runnable {
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	private int id;
	
	MySocketClient(String hostname, int port, int id)
					throws IOException {
		this.id = id;
		socket=new Socket();
		System.out.print("Client"+id+": connecting '"+hostname+
			"' on "+port+" ... ");
		socket.connect(new InetSocketAddress(hostname,port));
		System.out.println("done.");
		objectInputStream=
			new ObjectInputStream(socket.getInputStream());
		objectOutputStream=
			new ObjectOutputStream(socket.getOutputStream());

	}

	public String sendAndReceive(String message) {
		String string="";
			try {

				objectOutputStream.writeObject(message);
				string= "Client: received '" + (String) objectInputStream.readObject() + "'";
			}catch(Exception e){
				e.printStackTrace();
			}
		return string;
	}
	
	public void disconnect() {
		try {
			socket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		String message ="Test-";
		while(true){
			message+= UUID.randomUUID().toString();
			System.out.println("Client"+id+": send "+message);

			try {
				sendAndReceive(message);
				Thread.sleep(1000 + (new Random().nextInt(1000)));
			} catch (Exception e) {
				disconnect();
				break;
			}

		}
	}
}
