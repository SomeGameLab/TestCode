package testCode;

public class TestMain {
		
	
	
	private static Screen screen;
	private static Thread screenThread;
	
	public static void main(String[] args) {
		screen = new Screen();
		screenThread = screen.start();
		
	}
	
	public static void stop() {
		screen.stop();
		try {
			screenThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	

	
	
}
