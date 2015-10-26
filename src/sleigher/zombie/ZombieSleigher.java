package sleigher.zombie;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jackdahms.Controllable;
import com.jackdahms.ControllableThread;

public class ZombieSleigher implements Controllable {

    public static int WIDTH = 800;
    public static int HEIGHT = 600;
    public static int UPS = 30;
    
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
    static BufferedImage zombieImage, santaImage;
    
    private BoxButton[] menuButtons = new BoxButton[3];
    private BoxButton resumeButton;
    private BoxButton quitButton;
    
    private Santa santa;
    
    private List<Zombie> zombies = new ArrayList<Zombie>();
    private int zombieCount = 0;
    private double zombieSpawnChance = 0.0;
    private double zombieSpawnChanceIncrement = 0.02;
    private int zombieSpawnRate = UPS / 2;
    
    private int ticks = 0; //ticks since thread started;
    private int seconds = 0; //seconds since thread started
    
    private int hillSpeed = 1;
    private int hillDistance = 0;
    private int distance = 0;
    private int bestDistance = 0;
    
    private boolean gameOver;
    
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
    	
    	//and awaaaaay we go!
    	init();

    	//Update will be called UPS
    	controllableThread = new ControllableThread(this);
    	controllableThread.setTargetUps(UPS);
    	controllableThread.start();
    }
    
    public void init() {
    	backgroundGraphics = (Graphics2D) background.getGraphics();

    	gamestate = Gamestate.TITLE;
    	
    	String root = "/res/";
    	gameBackground = load(root + "background.jpg");
    	santaImage = load(root + "santa.png");
    	zombieImage = load(root + "zombie.png");
    	
    	santa = new Santa(100, 100);
    	zombies.add(new Zombie(200, 400, hillSpeed));
    	
    	instantiateButtons();
    	
    	//Needs to be added after buttons are created
    	canvas.addMouseMotionListener(new MouseMotion());
    	canvas.addMouseListener(new Mouse());
    	canvas.addKeyListener(new Key());
    }
        
    public void update() {
    	
    	if (gamestate == Gamestate.GAME) {

        	hillDistance += hillSpeed;
        	ticks++;
        	
        	if (ticks % UPS == 0) {
        		seconds++;
        		zombieSpawnChance += zombieSpawnChanceIncrement;
        	}
        	
        	if (ticks % zombieSpawnRate == 0) {
        		if (zombieSpawnChance > getRandomDouble(0.0, 1.0)) {
        			spawnZombie();
        		}
        	}
        	
    		santa.update();
    		
    		//track best distance this run
    		distance = distance > hillDistance + (int) santa.x + (int) santa.height ? 
    				distance : hillDistance + (int) santa.x + (int) santa.height;
    		
    		for (int i = 0; i <zombies.size(); i++) {
    			Zombie z = zombies.get(i);
    			z.update(santa.x, santa.y, santa.width, santa.height);
    			if (z.y + z.height < 0)
    				zombies.remove(i);
    		}
    		
    		if (gameOver) {
    			//TODO display game over image
    			gamestate = Gamestate.TITLE;
    			bestDistance = bestDistance > distance ? bestDistance : distance;
    		}
    	} else if (gamestate == Gamestate.TITLE) {
    		
    	}
    }

	public void renderGame(Graphics2D g, float delta) {
    	
    	g.drawImage(gameBackground, 0, -hillDistance % gameBackground.getHeight(), null);
    	g.drawImage(gameBackground, 0, HEIGHT - hillDistance % gameBackground.getHeight(), null);
    	
    	for (int i = 0; i <zombies.size(); i++)
			zombies.get(i).render(g, delta);

    	santa.render(g, delta);
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
    
    //TODO on hover over menu, include tidbit saying the current run will be forgotten
    public void renderPause(Graphics2D g, float delta) {
    	renderGame(g, delta);
    	
    	g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g.setColor(new Color(0, 0, 0, 120));
		g.fillRect(0, 0, 800, 600);
		
		resumeButton.render(g);
		quitButton.render(g);
		

		if (quitButton.hovering) {
			g.setColor(Color.red);
			g.setFont(new Font("helvetica", Font.PLAIN, 16));
			g.drawString("If you quit, your progress", 300, 335);
			
			
			g.drawString("will not be saved", 400 - g.getFontMetrics().stringWidth("will not be saved") / 2, 355);
		}
		
		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
		attributes.put(TextAttribute.TRACKING, 0.54);
		Font font = new Font("helvetica", Font.PLAIN, 30).deriveFont(attributes);
		g.setFont(font);
		
		g.setColor(Color.red);
		g.drawString("PAUSED", 300 - 2, 280);
    }
    
    private void instantiateButtons() {
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
    	
    	resumeButton = new BoxButton("RESUME", 300, 285, 100, 30) {
    		@Override
    		void onPress() {
    			gamestate = Gamestate.GAME;
    		}
    	};
    	quitButton = new BoxButton("QUIT", 424, 285, 76, 30) {
    		@Override
    		void onPress() {
    			gamestate = Gamestate.TITLE;
    		}
    	};
    }
    
    /**
     * Input Adapter Classes
     */
    
    private class MouseMotion extends MouseMotionAdapter {
    	public void mouseMoved(MouseEvent e) {
    		if (gamestate == Gamestate.TITLE) {
    			for (BoxButton b : menuButtons)
    				b.mouseMoved(e.getX(), e.getY()); 
    		} else if (gamestate == Gamestate.PAUSE) {
    			resumeButton.mouseMoved(e.getX(), e.getY());
    			quitButton.mouseMoved(e.getX(), e.getY());
    		}
    	}
    	
    	public void mouseDragged(MouseEvent e) {
    		if (gamestate == Gamestate.TITLE) {
    			for (BoxButton b : menuButtons)
    				b.mouseMoved(e.getX(), e.getY());
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
	    			else if (gamestate == Gamestate.PAUSE) {
	    				resumeButton.hovering = false;
	    				quitButton.hovering = false;
	    				gamestate = Gamestate.GAME;
	    			}
	    			break;
	    		case KeyEvent.VK_LEFT:
	    		case KeyEvent.VK_A:
	    			if (gamestate == Gamestate.GAME)
	    				santa.left = false;
	    			break;
	    		case KeyEvent.VK_RIGHT:
	    		case KeyEvent.VK_D:
	    			if (gamestate == Gamestate.GAME)
	    				santa.right = false;
	    			break;
	    		case KeyEvent.VK_UP:
	    		case KeyEvent.VK_W:
	    			if (gamestate == Gamestate.GAME)
	    				santa.up = false;
	    			break;
	    		case KeyEvent.VK_DOWN:
	    		case KeyEvent.VK_S:
	    			if (gamestate == Gamestate.GAME)
	    				santa.down = false;
	    			break;
    		}
    	}
    	
    	
    	@Override
    	public void keyPressed(KeyEvent e) {
    		super.keyPressed(e);
    		int key = e.getKeyCode();
    		switch(key) {
	    		case KeyEvent.VK_LEFT:
	    		case KeyEvent.VK_A:
	    			if (gamestate == Gamestate.GAME)
	    				santa.left = true;
	    			break;
	    		case KeyEvent.VK_RIGHT:
	    		case KeyEvent.VK_D:
	    			if (gamestate == Gamestate.GAME)
	    				santa.right = true;
	    			break;
	    		case KeyEvent.VK_UP:
	    		case KeyEvent.VK_W:
	    			if (gamestate == Gamestate.GAME)
	    				santa.up = true;
	    			break;
	    		case KeyEvent.VK_DOWN:
	    		case KeyEvent.VK_S:
	    			if (gamestate == Gamestate.GAME)
	    				santa.down = true;
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
    
    public void spawnZombie() {
    	//determine which of the fourteen spawn zones
		int zone = (int) getRandomDouble(0.0, 10.0);
		
		if (zone < 3) {
			zombies.add(new Zombie(850, 50 + 200 * zone, hillSpeed));
		} else if (zone < 7) {
			zombies.add(new Zombie(50 + 200 * (zone - 3), 650, hillSpeed));
		} else {
			zombies.add(new Zombie(-100, 50 + 200 * (zone - 7), hillSpeed));
		}
		
    	zombieCount++;
    }
   
    private void say(Object o) {
		System.out.println(o);
	}
    
    //						[lower, upper)
    public double getRandomDouble(double lower, double upper) {
    	return lower + Math.random() * (upper - lower);
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
			System.err.println("Could not load image!");
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
