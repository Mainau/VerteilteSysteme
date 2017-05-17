package aufgabe1b;

import java.io.*;
import java.net.*;
 
public class MySocketServerConnection extends Thread {
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;
	
	public MySocketServerConnection(Socket socket) 
					throws IOException {
		this.socket=socket;
		objectOutputStream=
			new ObjectOutputStream(socket.getOutputStream());
		objectInputStream=
			new ObjectInputStream(socket.getInputStream());
		System.out.println("Server: incoming connection accepted.");
	}

	private void disconnect(){
		try {
			socket.close();
			System.out.println("Server: Socked closed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		System.out.println("Server: waiting for message ...");
		try {
			while (true) {
				try {
					String string = (String) objectInputStream.readObject();
					System.out.println("Server: received '" + string + "'");
					objectOutputStream.writeObject("server received " + string);


				}catch (EOFException e) {
					disconnect();
					break;
				}catch (IOException e) {
					e.printStackTrace();
				}catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}

		}catch(Exception e){
			disconnect();
		}
	}
}
