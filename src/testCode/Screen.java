package testCode;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import javax.swing.JFrame;

public class Screen extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	BufferedImage layer;
	Graphics g;
	
	private static boolean running = true;
	private static long then;
	private static long now;
	private static int cycles = 0;
	private static final long REFRESH_RATE = 1000 / 24;
	
	private double x, y;
	private double speed = 1.0;
	
	private JFrame frame;
	private Dimension frameSize;
	
	
	private static Color clearColor = new Color(0, true);
	private int [] clearPixels;
	private Raster clearRaster;
	private BufferedImage clearImage;
	AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
	
	public Screen() {
		then = System.currentTimeMillis();
		initFrame();
	}
	
	public void initFrame() {		
		frameSize = new Dimension(960, 540);
		
		setPreferredSize(frameSize);
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Test Program");
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
		
		layer = new BufferedImage(frameSize.width, frameSize.height, BufferedImage.TYPE_INT_ARGB);
		x = y = 0;
	}
	
	public Thread start() {
		System.out.println("Starting ScreenThread..");
		Thread result = new Thread(this, "testThread");
		result.start();
		return result;
	}
	public void stop() {running = false;}
	
	public void run() {
		System.out.println("Running..");
		while(running) {
			now = System.currentTimeMillis();
			cycles++;				
			if(now - then >= REFRESH_RATE) {
				then = now;
				cycles = 0;
				update();
			}
		}	
	}
	
	public void update(){
		System.out.println("Updating..");
		move();
		clear();
		compose();
		draw();
	}
	public void move() { // Update x, y coordinates
		x += speed;
		y += speed / 2;
	}
	
	public void clear() {     // Should clear the layer and make it transparent
		//Method 1 - try create a new buffered image
		//layer = new BufferedImage(frameSize.width, frameSize.height, BufferedImage.TYPE_INT_ARGB);
		
		//Method 2 - clear with array
		/*
		g = layer.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, frameSize.width, frameSize.height);
		g.dispose();
		 */
		
		
		//Arrays.fill(clearPixels, 0);
		//raster.setRGB(0, 0, raster.getWidth(), raster.getHeight(), clearPixels, 0, raster.getWidth());				
		//clearRaster = clearImage.getData().createCompatibleWritableRaster();
		//raster.setData(clearRaster);
				
		//raster = new BufferedImage(size.x, size.y, FrameworkProperties.BUFFERED_IMAGE_TYPE);
				
		Graphics2D g = layer.createGraphics();				
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0,0, frameSize.width, frameSize.height);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		g.dispose();
						
		//g.setColor(clearColor);
		//g.fillRect(0, 0, size.x, size.y);
				
		//g.setBackground(clearColor);
		//g.clearRect(0, 0, size.x, size.y);		
	}
	public void compose() {
		g = layer.getGraphics();
		g.setColor(Color.RED);
		System.out.println("x , y " + x + ", " + y);
		g.fillRect((int)x, (int)y, 150, 150);
		g.dispose();
	}
	public void draw() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g.setColor(Color.MAGENTA);
		g.fillRect(0, 0, frameSize.width, frameSize.height);
		g.drawImage(layer, 0, 0, frameSize.width, frameSize.height, null);
		bs.show();
		g.dispose();
	}
	

	
	

}
