package sleigher.zombie;

import java.awt.Graphics2D;
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
	
	int width, height;
	
	boolean up, down, left, right;
	
	BufferedImage image = ZombieSleigher.santaImage;
	
	public Santa(float x, float y) {

		this.x = x;
		this.y = y;
		lastx = x;
		lasty = y;
		vx = 5;
		vy = 6;
		width = 50;
		height = 160;
	}
	
	/**
	 * TODO feature creep:
	 * max speed (right now diagonals are faster) using corner
	 */
	//Won't work with acceleration implemented
	float corner = 0.70710678118654752440084436210485f;//45 * Math.cos(45);
	
	public void update() {
		
		lastx = x;
		lasty = y;
		
		if (right)
			x += up ^ down ? vx*corner:vx;
		if (left)
			x -= up ^ down ? vx*corner:vx;
		if (up) 
			y -= left ^ right ? vy*corner:vy;
		if (down) 
			y += left ^ right ? vy*corner:vy;
			
	}
	
	/**
	 * TODO feature creep:
	 * sleigh slightly turns left or right
	 */
	public void render(Graphics2D g, float delta) {
		int drawx = (int) ((x - lastx) * delta + lastx);
		int drawy = (int) ((y - lasty) * delta + lasty);
		
		g.drawImage(image, drawx, drawy, width, height, null);
	}

}
