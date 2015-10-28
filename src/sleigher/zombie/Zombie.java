package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Zombie {

	float x, y;
	float hillSpeed;
	float xs, ys;
	int width, height;
	boolean dead;
	Rectangle bounds;
	
	BufferedImage image = ZombieSleigher.zombieImage;
	BufferedImage deadImage = ZombieSleigher.zombieDeadImage;
	
	public Zombie(float ys) {
		int zone = (int) getRandomDouble(0.0, 10.0);
		
		dead = false;
		
		if (zone < 3) {
			x = 850;
			y = 50 + 200 * zone;
		} else if (zone < 7) {
			x = 50 + 200 * (zone - 3);
			y = 650;
		} else {
			x = -100;
			y = 50 + 200 * (zone - 7);
		}
		
		this.hillSpeed = ys;
		
		bounds = new Rectangle((int) x, (int) y, width, height);
		
		width = 40;
		height = 60;
	}
	
	public void update(float hillSpeed, float santax, float santay, int santawidth, int santaheight) {
		this.hillSpeed = hillSpeed;
		y -= hillSpeed;
		
		if (y > santay && y + height < santay + santaheight) {
			ys = hillSpeed;
		} else if (y < santay) {
			ys = hillSpeed + 2;
		} else {
			ys = hillSpeed - 2;
		}
		
		int gap = 30;
		if (x + width > santax - gap && x < santax + santawidth + gap) {
			xs = 0;
		} else if (x + width < santax - gap) {
			xs = 2;
		} else {
			xs = -2;
		}
		
		x += xs;
		y += ys;
		
		bounds = new Rectangle((int) x, (int) y, width, height);
	}
	
	/**
	 * TODO 
	 * incorporate delta
	 */
	public void render(Graphics2D g, float delta) {
		if (dead) {
			g.drawImage(deadImage, (int) x, (int) y, width, height, null);
		} else {
			g.drawImage(image, (int) x, (int) y, width, height, null);
		}
	}
	
	//	[lower, upper)
	public double getRandomDouble(double lower, double upper) {
		return lower + Math.random() * (upper - lower);
	}

}
