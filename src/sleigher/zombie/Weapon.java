package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Weapon {
	
	float x, y;
	int width, height;
	
	//in shots per second
	float rateOfFire;
	float damage;
	
	boolean fired = false;
	
	BufferedImage image;
	
	public void update() {
		
	}
	
	public void render(Graphics2D g, float delta) {
		g.drawImage(image, (int) x, (int) y, width, height, null);
	}

}
