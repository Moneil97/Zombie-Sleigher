package sleigher.zombie;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Zombie {

	float x, y;
	float lastx, lasty;
	float hillSpeed;
	float xs, ys;
	float health;
	float maxHealth;
	float distance; //dist from santa anchor
	int width, height;
	int precentWorth;
	
	boolean dead;
	
	float collisionDamage;
	Rectangle bounds;
	
	BufferedImage image = ZombieSleigher.zombieImage;
	BufferedImage deadImage = ZombieSleigher.zombieDeadImage;
	
	public Zombie(float ys, float distance) {
		int zone = (int) getRandomDouble(0.0, 5.0);
		
		maxHealth = 3 + distance / 100; //base health of 3 plus one for every 100 meters
		System.out.println(maxHealth);
		health = maxHealth;
		
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
			
			//g.setColor(Color.red); 
			//g.fillRect(drawx, drawy - 5, width, 3);
			g.setColor(new Color(0, 255, 0, 150));
			int barWidth = (int) ((health / maxHealth) * width);
			g.fillRect(drawx + width/2 - barWidth/2 , drawy - 5, barWidth, 3);
		}
		
	}
	
	public void damage(float damage) {
		health -= damage;
		if (health <= 0) {
			dead = true;
			health = 0;
		}
	}
	
	//	[lower, upper)
	public double getRandomDouble(double lower, double upper) {
		return lower + Math.random() * (upper - lower);
	}

}
