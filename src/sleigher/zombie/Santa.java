package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Santa {
	
	float x, y;
	float lastx, lasty;
	float ax = .3f, ay = .2f;
	float vx = 0, vy = 0;
	float maxVx = 6, maxVy = 4;
	
	int width, height;
	
	boolean up, down, left, right;
	
	BufferedImage image;
	
	public Santa(BufferedImage image, float x, float y) {
		this.image = image;
		
		this.x = x;
		this.y = y;
		width = 50;
		height = 160;
	}
	
	/**
	 * TODO feature creep:
	 * Figure out best acceleration and max velocity values.
	 * Friction
	 * max speed (right now diagonals are faster)
	 */
	//Won't work with acceleration implemented
	float corner = 0.70710678118654752440084436210485f;//45 * Math.cos(45);
	
	public void update() {
		
		if (right || left)
			vx += (right? ax : -ax);
		if (up || down)
			vy += (down? ay : -ay);
		
		x += vx;
		y += vy;
		
		if (vx > maxVx)
			vx = maxVx;
		else if (vx < -maxVx)
			vx = -maxVx;
		if (vy > maxVy)
			vy = maxVy;
		else if (vy < -maxVy)
			vy = -maxVy;

	}
	
	/**
	 * TODO feature creep:
	 * sleigh slightly turns left or right
	 * incorporate delta
	 */
	public void render(Graphics2D g, float delta) {
		g.drawImage(image, (int) x, (int) y, width, height, null);
	}

}
