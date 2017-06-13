
public class MyThread extends Thread {
	private static final int threadMax = 10;
	private static int runCount = 0;

	public void run() {
		while (runCount++ < 100) {

			printThread();

		}

	}
	
//	public synchronized void printThread() {
//		System.out.println(runCount + ": " + Thread.currentThread().getName());
//		try {
//			Thread.currentThread().sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public synchronized static void printThread(){
		
			System.out.println(runCount + ": " + Thread.currentThread().getName());
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	

	

	public static void main(String args[]) {
		
		for (int i = 0; i < threadMax; i++) {
			new MyThread().start();
		}
	}

}
