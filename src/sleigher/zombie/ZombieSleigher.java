package sleigher.zombie;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.jackdahms.Controllable;
import com.jackdahms.ControllableThread;

public class ZombieSleigher implements Controllable {

    public static int WIDTH = 800;
    public static int HEIGHT = 600;
    
    private ControllableThread controllableThread;
    
    private JFrame frame;
    private Canvas canvas;
    private BufferStrategy strategy;
    private BufferedImage background;
    private Graphics2D backgroundGraphics;
    private Graphics2D graphics;
    private GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment()
    										.getDefaultScreenDevice()
    										.getDefaultConfiguration();
    
    private enum Gamestate {
    	TITLE,
    	GAME, 
    	SHOP,
    	INSTRUCTIONS,
    	PAUSE
    }
    private Gamestate gamestate = Gamestate.TITLE;
    
    private BufferedImage gameBackground;
    
    private BoxButton[] menuButtons = new BoxButton[3];
    private BoxButton resumeButton;
    private BoxButton quitButton;
    
    public ZombieSleigher() {
    	
    	//create JFrame
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

    	gamestate = Gamestate.TITLE;
    	
    	gameBackground = load("/res/background.jpg");
    	
    	menuButtons[0] = new BoxButton("PLAY", 500, 200, 200, 40){
    		@Override
    		void onPress() {
    			gamestate = Gamestate.GAME;
    		}
    	};
    	menuButtons[1] = new BoxButton("SHOP", 500, 260, 200, 40){
    		@Override
    		void onPress() {
    			gamestate = Gamestate.SHOP;
    		}
    	};
    	menuButtons[2] = new BoxButton("INSTRUCTIONS", 500, 320, 200, 40){
    		@Override
    		void onPress() {
    			gamestate = Gamestate.INSTRUCTIONS;
    		}
    	};
    	
    	resumeButton = new BoxButton("RESUME", 400, 300, 60, 20) {
    		@Override
    		void onPress() {
    			gamestate = Gamestate.GAME;
    		}
    	};
    	quitButton = new BoxButton("MENU", 400, 380, 60, 20) {
    		@Override
    		void onPress() {
    			gamestate = Gamestate.TITLE;
    		}
    	};
    	
    	//Needs to be added after buttons are created
    	canvas.addMouseMotionListener(new MouseMotion());
    	canvas.addMouseListener(new Mouse());
    	canvas.addKeyListener(new Key());
    }
    
    public void update() {
    	if (gamestate == Gamestate.GAME) {
    		
    	} else if (gamestate == Gamestate.TITLE) {
    		
    	}
    }
    
    public void renderGame(Graphics2D g, float delta) {
    	
    	g.drawImage(gameBackground, 0, 0, null);
    	
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
    
    public void renderTitle(Graphics2D g, float delta) {
    	
    	g.drawImage(gameBackground, 0, 0, null);

		//the title
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(Color.red);
		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
		attributes.put(TextAttribute.TRACKING, 0.3);
		Font font = new Font("helvetica", Font.PLAIN, 60).deriveFont(attributes);
		g.setFont(font);
		g.drawString("Zombie Sleigher", 25, 100);
		
		for (BoxButton b : menuButtons)
			b.render(g);
    }
    
    public void renderPause(Graphics2D g, float delta) {
    	renderGame(g, delta);

		g.setColor(new Color(0, 0, 0, 120));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		resumeButton.render(g);
		quitButton.render(g);
    }
    
    /**
     * Input Adapter Classes
     */
    
    private class MouseMotion extends MouseMotionAdapter {
    	public void mouseMoved(MouseEvent e) {
    		if (gamestate == Gamestate.TITLE) {
    			for (BoxButton b : menuButtons)
    				b.mouseMoved(e.getX(), e.getY()); //TODO throws exceptions before buttons instantiated
    		} else if (gamestate == Gamestate.PAUSE) {
    			resumeButton.mouseMoved(e.getX(), e.getY());
    			quitButton.mouseMoved(e.getX(), e.getY());
    		}
    	}
    	
    	public void mouseDragged(MouseEvent e) {
    		if (gamestate == Gamestate.TITLE) {
    			for (BoxButton b : menuButtons)
    				b.mouseMoved(e.getX(), e.getY()); //TODO throws exceptions before buttons instantiated
    		} else if (gamestate == Gamestate.PAUSE) {
    			resumeButton.mouseMoved(e.getX(), e.getY());
    			quitButton.mouseMoved(e.getX(), e.getY());
    		}
    	}
    }
    
    private class Mouse extends MouseAdapter {
    	public void mousePressed(MouseEvent e) {
    		if (gamestate == Gamestate.TITLE) {
    			for (BoxButton b : menuButtons)
    				b.mousePressed(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.PAUSE) {
    			resumeButton.mousePressed(e.getX(), e.getY());
    			quitButton.mousePressed(e.getX(), e.getY());
    		}
    	} 
    	
    	public void mouseReleased(MouseEvent e) {
    		if (gamestate == Gamestate.TITLE) {
    			for (BoxButton b : menuButtons)
    				b.mouseReleased(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.PAUSE) {
    			resumeButton.mouseReleased(e.getX(), e.getY());
    			quitButton.mouseReleased(e.getX(), e.getY());
    		}
    	}
    }
    
    private class Key extends KeyAdapter {
    	public void keyReleased(KeyEvent e) {
    		int key = e.getKeyCode();
    		switch(key) {
    		case KeyEvent.VK_P:
    			if (gamestate == Gamestate.GAME) gamestate = Gamestate.PAUSE;
    			break;
    		}
    	}
    }
    
    /**
     * Worker Methods
     */
    
    public static void main(String[] args) {
        new ZombieSleigher();
    }
    
    //don't draw here, draw in renderGame
    public void render(float delta) {
    	do {
			Graphics2D bg = getBuffer();
			
			if (gamestate == Gamestate.GAME) {
				renderGame(backgroundGraphics, delta);
			} else if (gamestate == Gamestate.PAUSE) {
				renderPause(backgroundGraphics, delta);
			} else if (gamestate == Gamestate.TITLE) {
				renderTitle(backgroundGraphics, delta);
			} else if (gamestate == Gamestate.SHOP) {
				
			} else if (gamestate == Gamestate.INSTRUCTIONS) {
				
			}
			
			bg.drawImage(background, 0, 0, 800, 600, null);
			
			bg.dispose();
		} while (!updateScreen());
    }
    
    
    //when the frame exits
    public void exit() {
    	controllableThread.stop();
    	frame.dispose();
    }
    
    public BufferedImage load(String path) {
    	try {
			return ImageIO.read(this.getClass().getResource(path));
		} catch (Exception e) {
			e.printStackTrace();
			BufferedImage missingTexture = create(800, 600, false);
			
			//the "missing texture" texture from garry's mod
			Graphics2D g = (Graphics2D) missingTexture.getGraphics();
			g.setColor(Color.PINK);
			g.fillRect(0, 0, 400, 300);
			g.fillRect(400, 300, 400, 300);
			g.setColor(Color.BLACK);
			g.fillRect(400, 0, 400, 300);
			g.fillRect(0, 300, 400, 300);
			g.dispose();
			
			return missingTexture;
		}
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
