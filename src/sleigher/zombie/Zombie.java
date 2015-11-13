package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Zombie {

	float x, y;
	float lastx, lasty;
	float hillSpeed;
	float xs, ys;
	float health;
	int width, height;
	int precentWorth;
	
	boolean dead;
	boolean onBulletPath;
	
	float collisionDamage;
	Rectangle bounds;
	
	BufferedImage image = ZombieSleigher.zombieImage;
	BufferedImage deadImage = ZombieSleigher.zombieDeadImage;
	
	public Zombie(float ys) {
		int zone = (int) getRandomDouble(0.0, 5.0);
		
		health = 10;
		
		precentWorth = 1;
		
		collisionDamage = 5;
		dead = false;
		
		if (zone < 1) { //lower right of the screen
			x = 850;
			y = 300 + (float) getRandomDouble(0.0, 250.0);
		} else if (zone < 2) {
			x = -100;
			y = 300 + (float) getRandomDouble(0.0, 250.0);
		} else { //bottom of the screen
			x = (float) getRandomDouble(100.0, 650.0);
			y = 700;
		}
		
		this.hillSpeed = ys;
		
		bounds = new Rectangle((int) x, (int) y, width, height);
		
		width = 40;
		height = 60;
	}
	
	public void update(float hillSpeed, float santax, float santay, int santawidth, int santaheight) {
		this.hillSpeed = hillSpeed;
		
		lastx = x;
		lasty = y;
		
		if (!dead) {
			if (y+ height > santay + santaheight) ys = -2;
			else if (y + height / 2 < santay) ys = 2;
			else ys = 0;
			
			if (x + width / 2 > santax + santawidth) xs = -2;
			else if (x + width / 2 < santax) xs = 2;
			else xs = 0;
			
			x += xs;
			y += ys;
		} else {
			y -= hillSpeed;
		}
		
		bounds = new Rectangle((int) x, (int) y, width, height);
	}
	
	/**
	 * TODO 
	 * incorporate delta
	 */
	public void render(Graphics2D g, float delta) {
		
		int drawx = (int) ((x - lastx) * delta + lastx);
		int drawy = (int) ((y - lasty) * delta + lasty);
		
		if (dead) {
			g.drawImage(deadImage, drawx, drawy, width, height, null);
		} else {
			g.drawImage(image, drawx, drawy, width, height, null);
		}
	}
	
	//	[lower, upper)
	public double getRandomDouble(double lower, double upper) {
		return lower + Math.random() * (upper - lower);
	}

}
