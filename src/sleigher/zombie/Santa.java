package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Santa {
	
	float x, y;
	float xs, ys;
	
	boolean up, down, left, right;
	
	BufferedImage image;
	
	public Santa() {
		//TODO read buffered image
	}
	
	/**
	 * feature creep:
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
	 * feature creep:
	 * sleigh slightly turns left or right
	 */
	public void render(Graphics2D g, float delta) {
		
	}

}
