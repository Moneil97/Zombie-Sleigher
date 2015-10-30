package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import static java.lang.Math.*;

public class Santa{
	
	float x, y;
	float lastx, lasty;
	float ax = .3f, ay = .2f;
	float vx = 0, vy = 0;
	float maxVx = 6, maxVy = 4;
	
	float health;
	
	int width, height;
	
	boolean up, down, left, right;
	
	Rectangle bounds;
	
	BufferedImage images[] = ZombieSleigher.santaImages;
	
	public Santa(float x, float y) {

		this.x = x;
		this.y = y;
		lastx = x;
		lasty = y;
		vx = 5;
		vy = 6;
		
		bounds = new Rectangle((int) x, (int) y, width, height);
		
		health = 100;
		
		width = 50;
		height = 160;
	}
	
	float corner = 0.70710678118654752440084436210485f;//45 * Math.cos(45);
	
	public void update() {
		
		lastx = x;
		lasty = y;
		
		if (right)
			x += up ^ down ? vx * corner : vx;
		if (left)
			x -= up ^ down ? vx * corner : vx;
		if (up) 
			y -= left ^ right ? vy * corner : vy;
		if (down) 
			y += left ^ right ? vy * corner : vy;
			
		bounds = new Rectangle((int) x, (int) y, width, height);
	}
	
	public void render(Graphics2D g, int mx, int my, float delta, int ticks) {
		int drawx = (int) ((x - lastx) * delta + lastx);
		int drawy = (int) ((y - lasty) * delta + lasty);
		
		//the higher those two magic numbers, the slower. the second one should be 1/4 of the first b/c there are 4 frames
		g.drawImage(images[(ticks % (8)) / 2], drawx, drawy, width, height, null);
	}

}
