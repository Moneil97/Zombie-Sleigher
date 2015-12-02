package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Tree {
	
	float x, y;
	float lasty; //no lastx because no horizontal movement
	int width, height;
	
	boolean dead;
	
	Polygon bounds;
	BufferedImage image = ZombieSleigher.treeImage;
	
	public Tree() {
		
		x = (float) getRandomDouble(100.0, 650.0);
		y = 700;
				
		//TODO make a polygon, not a rectangle
		int[] x = {20, 40, 0};
		int[] y = {5, 60, 60};
		for (int i = 0; i < 3; i++) {
			x[i] += this.x;
			y[i] += this.y;
		}
		bounds = new Polygon(x, y, 3);
		
		width = 40;
		height = 60;
	}
	
	public void update(float hillSpeed) {
		
		lasty = y;

//		bounds.translate(0, (int) -y);
		y -= hillSpeed;
		bounds.translate(0, (int) (y - lasty));
		
	}
	
	public void render(Graphics2D g, float delta) {
		int drawy = (int) ((y - lasty) * delta + lasty);
		g.drawImage(image, (int) x, drawy, width, height, null);
	}
	
	//	[lower, upper)
	public double getRandomDouble(double lower, double upper) {
		return lower + Math.random() * (upper - lower);
	}

}
