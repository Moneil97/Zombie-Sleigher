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
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

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
    	PAUSE,
    	GAMEOVER
    }
    private Gamestate gamestate = Gamestate.TITLE;
    
    private BufferedImage[] gameBackground = new BufferedImage[3];
    static BufferedImage precentImage;
    static BufferedImage zombieImage;
    static BufferedImage zombieDeadImage;
    static BufferedImage santaImages[] = new BufferedImage[4];
    static BufferedImage santaLeftArmImage;
    static BufferedImage santaRightArmImage;
    static BufferedImage titleImage;
    static BufferedImage santaTitleImage;
    static BufferedImage treeImage;
    static BufferedImage treeOtherImage;
    static BufferedImage shopTitleImage;
    static BufferedImage sleighedImage;
    static BufferedImage pistolRightImage;
    static BufferedImage rifleRightImage;
    static BufferedImage bazookaRightImage;
    static BufferedImage pistolLeftImage;
	static BufferedImage rifleLeftImage;
	static BufferedImage bazookaLeftImage;
    
    private BoxButton[] menuButtons = new BoxButton[3];
    private BoxButton resumeButton;
    private BoxButton quitButton;
    private BoxButton gameoverButton;
    private BoxButton instructionsButton;
    private BoxButton shopButton;
    private BoxButton[] weaponButtons = new BoxButton[2];
    
    private Santa santa;
    private boolean godMode = false;
    
    private Weapon weapon;
    private Pistol pistol;
    private Rifle rifle;
    private Bazooka bazooka;
    private Line2D bullet;
    
    private List<Zombie> zombies = new ArrayList<Zombie>();
    private int zombiesKilled = 0;
    private double zombieSpawnChance = 0.0;
    private double zombieSpawnChanceIncrement = 0.02;
    private int zombieSpawnRate = UPS / 2;
    
    private List<Tree> trees = new ArrayList<Tree>();
    private int treesDodged = 0;
    private double treeSpawnChance = 0.3;
    private double treeSpawnChanceIncrement = 0.02;
    private int treeSpawnRate = UPS * 2;
    
    private int ticks = 0; //ticks since thread started;
    private int seconds = 0; //seconds since thread started
    
    private float hillSpeed = 1;
    private float maxHillSpeed = 30;
    private float hillSpeedIncrement = 0.0f;
    private float hillDistance = 0;
    private int distance = 0;
    private int bestDistance = 0;
    
    private int precents = 0;
    private int savedPrecents = 0;
    
    private int bgIndex = 0;
    private float hillY[] = new float[3];
    private float lastHillY[] = new float[3];
    
    private int mx;
    private int my;
    
    private boolean gameOver;
    
    private List<String> instructions = new ArrayList<String>();
    
    int statSize = 14;
    private int[] statValues = new int[statSize];
    private String[] statUnits = new String[statSize];
    
    private String[] statNames = {"Distance traveled: ",	//1
    		"Furthest distance traveled: ",	//2
    		"Total distance traveled: ",	//3
    		"Zombies killed: ",				//4
    		"Total zombies killed: ",		//5
    		"Bullets fired: ",				//6
    		"Trees dodged: ",				//7
    		"Accuracy: ",					//8
    		"Overall Accuracy: ",			//9
    		"Time of run: ",				//10
    		"Total play time: ",			//11
    		"Precents earned: ",			//12
    		"Precents currently owned: ",	//13
    		"Total precents earned: "		//14
    };
	
    
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
    	
    	for (int i = 0; i < statSize; i++) {
    		statValues[i] = 0;
    		statUnits[i] = "";
    	}
    	
    	//TODO make sure this matches the list
    	statUnits[0] = "m";
    	statUnits[1] = "m";
    	statUnits[2] = "m";
    	
    	statUnits[9] = "s";
    	statUnits[10] = "s";

    	gamestate = Gamestate.TITLE;
    	
    	for (int i = 0; i < 3; i++) { 
    		hillY[i] = HEIGHT * i;
    		lastHillY[i] = hillY[i];
    	}
    	
    	String root = "/res/";
    	for (int i = 0; i < 3; i++) gameBackground[i] = load(root + "background.jpg");
    	
    	for (int i=1; i <=4; i++)
    		santaImages[i-1] = load(root + "santa" + i + ".png");
    	santaRightArmImage = load(root + "santa_Arm_Right.png");
    	santaLeftArmImage = flip(santaRightArmImage);// = load(root + "santa_Arm_Right.png");
    	zombieImage = load(root + "zombie.png");
    	zombieDeadImage = load(root + "zombie_Dead.png");
    	titleImage = load(root + "title.png");
    	santaTitleImage = load(root + "santa_Title.png");
    	treeImage = load(root + "tree1.png");
    	treeOtherImage = load(root + "tree2.png");
    	shopTitleImage = load(root + "shop.png");
    	sleighedImage = load(root + "sleighed.png");
    	precentImage = load(root + "precent.png");
    	pistolRightImage = load(root + "pistol.png");
    	pistolLeftImage = flip(pistolRightImage);
    	rifleRightImage = load(root + "rifle.png");
    	rifleLeftImage = flip(rifleRightImage);
    	bazookaRightImage = load(root + "bazooka.png");
    	bazookaLeftImage = flip(bazookaRightImage);
    	
    	bullet = new Line2D.Double();
    	pistol = new Pistol();
    	rifle = new Rifle();
    	bazooka = new Bazooka();
    	weapon = pistol;
    	
    	//I can't believe I'm actually using this. I've never used it outside of AP comp sci
    	Scanner s = new Scanner(getClass().getResourceAsStream(root + "instructions.txt"));
    	while (s.hasNextLine()) instructions.add(s.nextLine());
    	s.close();
    	
    	instantiateButtons();
    	
    	//Needs to be added after buttons are created
    	canvas.addMouseMotionListener(new MouseMotion());
    	canvas.addMouseListener(new Mouse());
    	canvas.addKeyListener(new Key());
    }
    
    /** TODO (in class, things we need to discuss together)
     * shop layout
     * actual upgrades
     * weapon stats
     * weapon upgrades
     */
    
    /**
     * TODO (actual things we have to add)
     * sound: bullet firing, zombies dying
     * music, christmas at ground zero, baila fleck and the flecktones
     * zombie worth part of zombiesleigher, not zombie?
     * speed increases damage done by zombies
     * weapons
     * weapon animations
     * shop
     * precents
     */
    
    /** TODO known bugs
     * trees and dead zombies jitter down
     * size of frame is not size of canvas, santa can go over the right and bottom sides a tiny bit
     * above issue may be operating system dependent
     */
    
    /**
     * TODO (feature creep)
     * precents drop and must be collected?
     * precents fly towards counter
     * grenades
     * more zombie species
     * dashed line follows best distance
     * powerups
     * alternate tree images
     * zombies catch on fire when hit by engine flame
     * key bindings not key listener
     * make distance based on sleigh position on hill, not just hill
     * change large distances to km from m
     * grenade upgrades
     * draw bullets
     */
        
    int closestZombieIndex = -1; //make this an arraylist
    public void update() {
    	if (gamestate == Gamestate.GAME) {
    		
        	hillDistance += hillSpeed;
        	
        	float base = -hillDistance % gameBackground[0].getHeight();
        	if (base == 0) bgIndex = (bgIndex + 1) % 3;
        	for (int i = bgIndex; i < bgIndex + 3; i++) {
        		int k = i % 3;
        		lastHillY[k] = hillY[k];
        		hillY[k] = base + (HEIGHT * (i - bgIndex));
        	}
        	
        	ticks++;
        	
        	if (ticks % UPS == 0) {
        		seconds++;
        		if (hillSpeed < maxHillSpeed) hillSpeed += hillSpeedIncrement;
        		
        		zombieSpawnChance += zombieSpawnChanceIncrement;
        		treeSpawnChance += treeSpawnChanceIncrement;
        	}
        	
        	if (ticks % zombieSpawnRate == 0) {
        		if (zombieSpawnChance > getRandomDouble(0.0, 1.0)) {
        			zombies.add(new Zombie(hillSpeed));
        		}
        	}
        	
        	if (ticks % treeSpawnRate == 0) {
        		if (treeSpawnChance > getRandomDouble(0.0, 1.0)) {
        			trees.add(new Tree());
        		}
        	}
        	
    		santa.update();
    		weapon.update();
    		 
    		if (weapon.fired) {
        		double hypotenuse = 1000;
        		//TODO angle variation
        		double angle = santa.angle;
    			double edgex = hypotenuse * Math.cos(angle);
    			double edgey = hypotenuse * Math.sin(angle);
    			if (santa.weaponOnRight) {
    				bullet = new Line2D.Double(santa.rightAnchorX, santa.anchorY, 
    						santa.rightAnchorX + edgex, santa.anchorY + edgey);
    			} else {
    				bullet = new Line2D.Double(santa.leftAnchorX, santa.anchorY, 
    						santa.leftAnchorX + edgex, santa.anchorY + edgey);
    			}
    		}
    		
    		for (int i = 0; i < zombies.size(); i++) {
    			Zombie z = zombies.get(i);
    			
    			z.update(hillSpeed, santa.x, santa.y, santa.width, santa.height);
    			
    			if (!z.dead && z.bounds.intersectsLine(bullet)) {
					float dy = (float) (z.y - santa.anchorY);
					float dx;
					if (santa.weaponOnRight) {
						dx = (float) (z.x - santa.rightAnchorX);
					} else {
						dx = (float) (santa.leftAnchorX - z.x);
					}
					z.distance = (float) (Math.sqrt((dy * dy) + (dx * dx)));
					
					if (closestZombieIndex > -1) {
						if (z.distance < zombies.get(closestZombieIndex).distance) closestZombieIndex = i;
					} else {
						closestZombieIndex = i;
					}
				}
    			
    			if (!z.dead && santa.bounds.intersects(z.bounds)) {
    				if (!godMode) santa.health -= z.collisionDamage;
    				z.dead = true;
    				precents += z.precentWorth;
    				zombiesKilled++;
    			}
    			
    			if (z.y + z.height < 0)
    				zombies.remove(i);
    		}
    		
    		//bang bang (do this as much as once per tick, 
    		if (weapon.fired && closestZombieIndex > -1) {
    			zombies.get(closestZombieIndex).dead = true;
    		}
    		//do this every tick to account for death of the closest zombie
    		closestZombieIndex = -1;
    		
    		for (int i = 0; i < trees.size(); i++) {
    			Tree t = trees.get(i);
    			t.update(hillSpeed);
    			
    			if (t.y + t.height < 0) {
    				trees.remove(i);
    				if (!t.dead) treesDodged++;
    			}
    		}
    		
    		if (santa.health <= 0) {
    			gameOver = true;
    		}
    		
    		//track best distance this run
    		distance = (int) hillDistance / 10;
//    		distance = distance > hillDistance + (int) santa.x + (int) santa.height ? 
//    				distance : hillDistance + (int) santa.x + (int) santa.height;
    		
    		if (gameOver) {
    			gamestate = Gamestate.GAMEOVER;
    		}
    	} else if (gamestate == Gamestate.PAUSE) {
    		santa.lastx = santa.x;
    		santa.lasty = santa.y;
    	} else if (gamestate == Gamestate.SHOP) {
    	
    	} else if (gamestate == Gamestate.GAMEOVER) {
    		if (gameOver) {
    			bestDistance = bestDistance > distance ? bestDistance : distance;
    			savedPrecents += precents;
    			
    			//make sure this is in order that matches with statNames
    			statValues[0] = distance;		//previous distance
    			statValues[1] = bestDistance;	//record distance
    			statValues[2] += distance;		//furthest distance traveled
    			
    			statValues[3] = zombiesKilled;	//kamizombies killed
    			statValues[4] += zombiesKilled;	//total kamizombies killed
    			
    			statValues[5] = 0;				//bullets fired
    			statValues[6] = treesDodged;	//trees dodged
    			
    			statValues[7] = 0;				//accuracy of run
    			statValues[8] = 0;				//lifetime accuracy
    			
    			statValues[9] = seconds;		//time of run
    			statValues[10] += seconds;		//total play time
    			
    			statValues[11] = precents;		//precents earned that run
    			statValues[12] = savedPrecents;	//saved precents
    			statValues[13] += precents;		//lifetime precents earned
    			
    			gameOver = false;
    		}
    		santa.lastx = santa.x;
    		santa.lasty = santa.y;
    		
    	} else if (gamestate == Gamestate.TITLE) {
    		
    	}
    }

	public void renderGame(Graphics2D g, float delta) {
		
		//only draw two backgrounds at a time so delta doesn't fuck up when you move the top background to the bottom
		for (int i = bgIndex; i < bgIndex + 2; i++) {
			int k = i % 3;
			int draw = (int) ((hillY[k] - lastHillY[k]) * delta + lastHillY[k]);
			g.drawImage(gameBackground[k], 0, draw, null);
		}
    	
    	for (int i = 0; i < zombies.size(); i++)
			zombies.get(i).render(g, delta);
    	
    	for (int i = 0; i < trees.size(); i++)
    		trees.get(i).render(g, delta);

    	santa.render(g, mx, my, delta, ticks);
    	
    	//distance
    	g.setColor(Color.red);
    	g.setFont(new Font("helvetica", Font.PLAIN, 18));
    	g.drawString("" + distance + "m", 
    			790 - (g.getFontMetrics().stringWidth("" + distance + "m")), santa.y + santa.height / 2);
    	
    	//health label
    	g.setFont(new Font("helvetica", Font.PLAIN, 20));
    	g.drawString("HEALTH ", 400 - (g.getFontMetrics().stringWidth("HEALTH ") + 100) / 2, 23);
    	
    	//health bar
    	g.setColor(new Color(0, 255, 0, 125));
    	g.fillRect(400 - g.getFontMetrics().stringWidth("HEALTH ") / 2 + 32, 5, (int) santa.health, 20);
    	g.setColor(Color.red);
    	g.drawRect(400 - g.getFontMetrics().stringWidth("HEALTH ") / 2 + 32, 5, 100, 20);
    	
    	//precents
    	g.drawImage(precentImage, 767, 5, 25, 25, null);
    	g.setColor(new Color(150, 50, 150));
    	g.setFont(new Font("helvetica", Font.PLAIN, 22));
    	g.drawString("" + precents, 770 - 7 - g.getFontMetrics().stringWidth("" + precents), 25);
    }
    
    public void renderTitle(Graphics2D g, float delta) {
    	
    	g.setColor(Color.white);
    	g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.drawImage(titleImage, 50, 30, 420, 200, null);
		g.drawImage(santaTitleImage, 520, 30, 230, 50, null);
		
		for (BoxButton b : menuButtons)
			b.render(g);
		
		g.setColor(Color.red);
		g.setFont(new Font("helvetica", Font.BOLD, 16));
		g.drawString("Previous Run Stats", 90, 280);
		
		g.drawLine(50, 275, 80, 275); //top, left half
		g.drawLine(265, 275, 750, 275); //top, right half
		g.drawLine(50, 275, 50, 530); //left
		g.drawLine(50, 530, 750, 530); //bottom
		g.drawLine(750, 275, 750, 530); //right
		
		int firstColumnSize = 7; //number of statistics that appear in the first column
		
		g.setFont(new Font("helvetica", Font.PLAIN, 16));
		for (int i = 0; i < firstColumnSize; i++) {
			g.drawString(statNames[i], 80, 310 + i * 32);
			g.drawString(statValues[i] + statUnits[i], 320, 310 + i * 32);
		}
		for (int i = firstColumnSize; i < statSize; i++) {
			g.drawString(statNames[i], 430, 310 + (i - firstColumnSize) * 32);
			g.drawString(statValues[i] + statUnits[i], 670, 310 + (i - firstColumnSize) * 32);
		}
    }
    
    public void renderPause(Graphics2D g, float delta) {
    	renderGame(g, delta);
    	
    	resetLastPositions();

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
    
    //TODO just bookmarking this method so I can find it easy while I'm working on it
    public void renderShop(Graphics2D g, float delta) {
    	g.setColor(Color.white);
    	g.fillRect(0, 0, WIDTH, HEIGHT);
    	
    	g.drawImage(shopTitleImage, 20, 20, 500, 200, null);
    	
    	g.drawImage(precentImage, 550, 60, 25, 25, null);
    	g.setColor(new Color(150, 50, 150));
    	g.setFont(new Font("helvetica", Font.PLAIN, 22));
    	g.drawString("" + savedPrecents, 750 - g.getFontMetrics().stringWidth("" + savedPrecents), 82);
    	
    	shopButton.render(g);
    }
    
    public void renderGameover(Graphics2D g, float delta) {
    	renderGame(g, delta);
    	
    	resetLastPositions();
    	
		g.setColor(new Color(0, 0, 0, 170));
		g.fillRect(0, 0, 800, 600);
    	
    	g.drawImage(sleighedImage, 200, 60, 100*4, 60*4, null);
		
		gameoverButton.render(g);
    }
    
    public void renderInstructions(Graphics2D g, float delta) {
    	
    	g.setColor(Color.WHITE);
    	g.fillRect(0, 0, WIDTH, HEIGHT);
    	
    	g.setColor(Color.red);
    	g.setFont(new Font("helvetica", Font.BOLD, 20));
    	g.drawString("INSTRUCTIONS", 400 - g.getFontMetrics().stringWidth("INSTRUCTIONS") / 2, 30);
    	
    	g.setFont(new Font("helvetica", Font.PLAIN, 16));
    	for (int i = 0; i < instructions.size(); i++) {
    		String s = instructions.get(i);
    		int x = WIDTH / 2 - g.getFontMetrics().stringWidth(s) / 2;
    		int y = 110 + (g.getFontMetrics().getHeight() + 2) * i;
    		g.drawString(s, x, y);
    	}
    	
    	instructionsButton.render(g);
    }
    
    private void gameReset(){
    	//TODO make sure to reset all stat variables, not just the stats themselves
    	santa = new Santa(weapon, 375,150);
		gameOver = false;
		trees.clear();
		zombies.clear();
		treeSpawnChance = 0.3;
		zombieSpawnChance = 0.0;
		hillSpeed = 5;
		hillDistance = 0;
		distance = 0;
		seconds = 0;
		zombiesKilled = 0;
		treesDodged = 0;
    }
    
    private void resetLastPositions() {
    	santa.lastx = santa.x;
    	santa.lasty = santa.y;
    	
    	for (int i = 0; i < 3; i++) lastHillY[i] = hillY[i];
    	
    	for (Tree t : trees) 
    		t.lasty = t.y;
    	
    	for (Zombie z : zombies) {
    		z.lastx = z.x;
    		z.lasty = z.y;
    	}
    }
    
    /**
     * Input Adapter Classes
     */
    
    private class MouseMotion extends MouseMotionAdapter {
    	public void mouseMoved(MouseEvent e) {
    		if (gamestate == Gamestate.GAME) {
    			mx = e.getX();
    			my = e.getY();
    		} else if (gamestate == Gamestate.TITLE) {
    			for (BoxButton b : menuButtons)
    				b.mouseMoved(e.getX(), e.getY()); 
    		} else if (gamestate == Gamestate.PAUSE) {
    			resumeButton.mouseMoved(e.getX(), e.getY());
    			quitButton.mouseMoved(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.GAMEOVER) {
    			gameoverButton.mouseMoved(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.INSTRUCTIONS) {
    			instructionsButton.mouseMoved(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.SHOP) {
    			shopButton.mouseMoved(e.getX(), e.getY());
    			for (BoxButton b : weaponButtons)
    				b.mouseMoved(e.getX(), e.getY()); 
    		}
    	}
    	
    	public void mouseDragged(MouseEvent e) {
    		if (gamestate == Gamestate.GAME) {
    			mx = e.getX();
    			my = e.getY();
    		} else if (gamestate == Gamestate.TITLE) {
    			for (BoxButton b : menuButtons)
    				b.mouseMoved(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.PAUSE) {
    			resumeButton.mouseMoved(e.getX(), e.getY());
    			quitButton.mouseMoved(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.GAMEOVER) {
    			gameoverButton.mouseMoved(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.INSTRUCTIONS) {
    			instructionsButton.mouseMoved(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.SHOP) {
    			shopButton.mouseMoved(e.getX(), e.getY());
    			for (BoxButton b : weaponButtons)
    				b.mouseMoved(e.getX(), e.getY()); 
    		}
    	}
    }
    
    private class Mouse extends MouseAdapter {
    	public void mousePressed(MouseEvent e) {
    		if (gamestate == Gamestate.GAME) {
    			weapon.mousePressed();
    		} else if (gamestate == Gamestate.TITLE) {
    			for (BoxButton b : menuButtons)
    				b.mousePressed(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.PAUSE) {
    			resumeButton.mousePressed(e.getX(), e.getY());
    			quitButton.mousePressed(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.GAMEOVER) {
    			gameoverButton.mousePressed(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.INSTRUCTIONS) {
    			instructionsButton.mousePressed(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.SHOP) {
    			shopButton.mousePressed(e.getX(), e.getY());
    			for (BoxButton b : weaponButtons)
    				b.mousePressed(e.getX(), e.getY());
    		}
    	} 
    	
    	public void mouseReleased(MouseEvent e) {
    		if (gamestate == Gamestate.GAME) {
    			weapon.mouseReleased();
    		} else if (gamestate == Gamestate.TITLE) {
    			for (BoxButton b : menuButtons)
    				b.mouseReleased(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.PAUSE) {
    			resumeButton.mouseReleased(e.getX(), e.getY());
    			quitButton.mouseReleased(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.GAMEOVER) {
    			gameoverButton.mouseReleased(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.INSTRUCTIONS) {
    			instructionsButton.mouseReleased(e.getX(), e.getY());
    		} else if (gamestate == Gamestate.SHOP) {
    			shopButton.mouseReleased(e.getX(), e.getY());
    			for (BoxButton b : weaponButtons)
    				b.mouseReleased(e.getX(), e.getY());
    		}
    			
    	}
    }
    
    private class Key extends KeyAdapter {
    	public void keyReleased(KeyEvent e) {
    		int key = e.getKeyCode();
    		if (gamestate == Gamestate.GAME) {
	    		switch(key) {
	    		case KeyEvent.VK_P:
	    		case KeyEvent.VK_ESCAPE:
	    			gamestate = Gamestate.PAUSE;
	    			break;
	    		case KeyEvent.VK_LEFT:
	    		case KeyEvent.VK_A:
	    			santa.left = false;
	    			break;
	    		case KeyEvent.VK_RIGHT:
	    		case KeyEvent.VK_D:
	    			santa.right = false;
	    			break;
	    		case KeyEvent.VK_UP:
	    		case KeyEvent.VK_W:
	    			santa.up = false;
	    			break;
	    		case KeyEvent.VK_DOWN:
	    		case KeyEvent.VK_S:
	    			santa.down = false;
	    			break;
	    		case KeyEvent.VK_COMMA:
	    			santa.health -= 10;
	    			break;
	    		case KeyEvent.VK_PERIOD:
	    			santa.health += 10;
	    			break;
	    		case KeyEvent.VK_G:
	    			godMode = !godMode;
	    			break;
	    		}		
	    		
    		} else if (gamestate == Gamestate.PAUSE) {
    			switch (key) {
    			case KeyEvent.VK_P:
    			case KeyEvent.VK_ESCAPE:
    				resumeButton.hovering = false;
    				quitButton.hovering = false;
    				gamestate = Gamestate.GAME;
    				break;
    			}
    		} 
    	}
    	
    	
    	@Override
    	public void keyPressed(KeyEvent e) {
    		super.keyPressed(e);
    		int key = e.getKeyCode();
    		if (gamestate == Gamestate.GAME) {
	    		switch(key) {
	    		case KeyEvent.VK_LEFT:
	    		case KeyEvent.VK_A:
	    			santa.left = true;
	    			break;
	    		case KeyEvent.VK_RIGHT:
	    		case KeyEvent.VK_D:
	    			santa.right = true;
	    			break;
	    		case KeyEvent.VK_UP:
	    		case KeyEvent.VK_W:
	    			santa.up = true;
	    			break;
	    		case KeyEvent.VK_DOWN:
	    		case KeyEvent.VK_S:
	    			santa.down = true;
	    			break;
	    		case KeyEvent.VK_MINUS:
	    			santa.health--;
	    			break;
	    		case KeyEvent.VK_PLUS:
	    			santa.health++;
	    			break;
	    		}
    		}
    	}
    }
    
    /**
     * Worker Methods
     */
    
    public static BufferedImage flip(BufferedImage image){
    	// Flip the image vertically
    	AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
    	tx.translate(0, -image.getHeight(null));
    	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    	return op.filter(image, null);
    }
    
    public static void main(String[] args) {
        new ZombieSleigher();
    }
   
    public void say(Object o) {
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
			
	    	backgroundGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
	    			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    	backgroundGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    			RenderingHints.VALUE_ANTIALIAS_ON);
			
			if (gamestate == Gamestate.GAME) {
				renderGame(backgroundGraphics, delta);
			} else if (gamestate == Gamestate.PAUSE) {
				renderPause(backgroundGraphics, delta);
			} else if (gamestate == Gamestate.GAMEOVER) {
				renderGameover(backgroundGraphics, delta);
    		} else if (gamestate == Gamestate.TITLE) {
				renderTitle(backgroundGraphics, delta);
			} else if (gamestate == Gamestate.SHOP) {
				renderShop(backgroundGraphics, delta);
			} else if (gamestate == Gamestate.INSTRUCTIONS) {
				renderInstructions(backgroundGraphics, delta);
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
    

    private void instantiateButtons() {
    	menuButtons[0] = new BoxButton("PLAY", 520, 80, 230, 40){
    		@Override
    		void onPress() {
    			gameReset();
    			gamestate = Gamestate.GAME;
    		}
    	};
    	menuButtons[1] = new BoxButton("SHOP", 520, 135, 230, 40){
    		@Override
    		void onPress() {
    			gamestate = Gamestate.SHOP;
    		}
    	};
    	menuButtons[2] = new BoxButton("INSTRUCTIONS", 520, 190, 230, 40){
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
    	gameoverButton = new BoxButton("BACK TO MENU", 300, 350, 200, 40) {
    		@Override
    		void onPress() {
    			gamestate = Gamestate.TITLE;
    		}
    	};
    	instructionsButton = new BoxButton("BACK TO MENU", 300, 40, 200, 30) {
    		@Override
    		void onPress() {
    			gamestate = Gamestate.TITLE;
    		}
    	};
    	shopButton = new BoxButton("BACK TO MENU", 550, 20, 200, 30) {
    		@Override
    		void onPress() {
    			gamestate = Gamestate.TITLE;
    		}
    	};
    }
    
}
