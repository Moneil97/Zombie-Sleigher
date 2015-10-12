package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Santa {
	
	float x, y;
	float lastx, lasty;
	float xs, ys;
	
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
	 * acceleration
	 * max speed (right now diagonals are faster)
	 */
	public void update() {
		if (up) y -= ys;
		if (down) y += ys;
		if (left) x -= xs;
		if (right) x += xs;
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
