package sleigher.zombie;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.jackdahms.Controllable;
import com.jackdahms.ControllableThread;

public class ZombieSleigher implements Controllable{ //we want to make this a canvas and use bufferstrategy, not jpanel

    public static int WIDTH = 800;
    public static int HEIGHT = 600;
    
    private ControllableThread controllableThread;
    
    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy strategy;
    private BufferedImage background;
    private Graphics2D backgroundGraphics;
    private Graphics2D graphics;
    //helps canvas, tells it where to buffer
    private GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment()
    										.getDefaultScreenDevice()
    										.getDefaultConfiguration();
    
    public ZombieSleigher() {
    	
    	//create JFrame
    	System.out.println("cameron");
    	frame = new JFrame("Santa Sleigher");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		exit();
        	}
        });
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    	frame.setVisible(true);
    	
    	//create the canvas and add it to the frame
    	canvas = new Canvas(config);
    	canvas.setSize(WIDTH, HEIGHT);
    	frame.add(canvas, 0); //adds canvas at index 0
    	
    	//create background image and buffer
    	background = create(WIDTH, HEIGHT, false);
    	canvas.createBufferStrategy(2);
    	do {
    		strategy = canvas.getBufferStrategy();
    	} while (strategy == null);
    	
    	//Update will be called 60 fps, render will be called default 60 fps
    	controllableThread = new ControllableThread(this);
    	controllableThread.setTargetUps(60);
    	
    	//and awaaaaay we go!
    	init();
    	controllableThread.start();
    }
    
    public void init() {
    	backgroundGraphics = (Graphics2D) background.getGraphics();
    }
    
    public void update() {
    	
    }
    
    int counter = 0;
    public void renderGame(Graphics2D g, float delta) {
    	//wipe the screen. we ain't usin' swing anymore, boys
    	g.setColor(Color.white);
    	g.fillRect(0, 0, WIDTH, HEIGHT);
    	
    	//tilt sleigh right or left based on movement
    	int swidth = 50;
    	int sheight = 60;
    	g.setColor(Color.red);
    	g.fillRect(50, 50, swidth, sheight);
    	
    	int rwidth = swidth;
    	int rheight = 100;
    	g.setColor(Color.blue);
    	g.fillRect(50, 50 + sheight, rwidth, rheight);
    	
    	int zwidth = 40;
    	int zheight = 50;
    	g.setColor(Color.green);
    	g.fillRect(150, 50, zwidth, zheight);
    }
    
    //don't draw here, draw in renderGame
    public void render(float delta) {
    	do {
			Graphics2D bg = getBuffer();
			
			renderGame(backgroundGraphics, delta);
			
			bg.drawImage(background, 0, 0, null);
			
			bg.dispose();
		} while (!updateScreen());
    }
    
    public static void main(String[] args) {
        new ZombieSleigher();
    }
    
    /**
     * Worker Methods
     */
    
    //when the frame exits
    public void exit() {
    	controllableThread.stop();
    	frame.dispose();
    }
    
    //create a hardware accelerated image
    public BufferedImage create(int width, int height, boolean alpha) {
    	return config.createCompatibleImage(width, height, alpha ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
    }
    
    //screen and buffer stuff
    private Graphics2D getBuffer() {
    	if (graphics == null) {
    		try {
    			graphics = (Graphics2D) strategy.getDrawGraphics();
    		} catch (IllegalStateException e) {
    			return null;
    		}
    	}
    	return graphics;
    }
    
    private boolean updateScreen() {
    	graphics.dispose();
    	graphics = null;
    	try {
    		strategy.show();
    		Toolkit.getDefaultToolkit().sync();
    		return !strategy.contentsLost();
    	} catch (NullPointerException e) {
    		return true;

    	} catch (IllegalStateException e) {
    		return true;
    	}
    }

}
