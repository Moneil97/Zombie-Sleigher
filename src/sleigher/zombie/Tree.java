package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Tree {
	
	float x, y;
	int width, height;
	float collisionDamage;
	Rectangle bounds;
	BufferedImage image = ZombieSleigher.treeImage;
	
	public Tree() {
		collisionDamage = 5;
		
		x = (float) getRandomDouble(100.0, 650.0);
		y = 700;
		
		bounds = new Rectangle((int) x, (int) y, width, height);
		
		width = 40;
		height = 60;
	}
	
	public void update(float hillSpeed) {
		
		y -= hillSpeed;
		
		bounds = new Rectangle((int) x, (int) y, width, height);
	}
	
	public void render(Graphics2D g, float delta) {
		g.drawImage(image, (int) x, (int) y, width, height, null);
	}
	
	//	[lower, upper)
	public double getRandomDouble(double lower, double upper) {
		return lower + Math.random() * (upper - lower);
	}

}
