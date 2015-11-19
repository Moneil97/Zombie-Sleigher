package sleigher.zombie;

import static java.lang.Math.PI;
import static java.lang.Math.atan;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Santa{
	
	float x, y;
	float lastx, lasty;
	float ax = .3f, ay = .2f;
	float vx = 0, vy = 0;
	float maxVx = 6, maxVy = 4;
	
	Weapon weapon;
	
	float health;
	float scalex = 2.5f;
	float scaley = 1.6f;
	
	int width, height;
	
	boolean up, down, left, right;
	
	Polygon bounds;
	
	Rectangle rightBound, topBound, leftBound, bottomBound;
	
	BufferedImage leftArmImage = ZombieSleigher.santaLeftArmImage;
	BufferedImage rightArmImage = ZombieSleigher.santaRightArmImage;
	BufferedImage images[] = ZombieSleigher.santaImages;
	
	double rightAnchorX;
	double leftAnchorX;
	double anchorY;
	double angle;
	
	boolean weaponOnRight;
	
	public Santa(Weapon weapon, float x, float y) {

		this.weapon = weapon;
		this.x = x;
		this.y = y;
		lastx = x;
		lasty = y;
		vx = 5;
		vy = 6;
		
		health = 100;
		
		width = (int) (18 * scalex);
		height = (int) (83 * scaley);
		
		rightBound = new Rectangle(0, 0, 0, 600);
		topBound = new Rectangle(0, 0, 800, 0);
		leftBound = new Rectangle(800, 0, 800, 600);
		bottomBound = new Rectangle(0, 600, 800, 600);
		
		bounds = getBounds();
	}
	
	float corner = 0.70710678118654752440084436210485f;//45 * cos(45);
	
	public void update() {
		
		lastx = x;
		lasty = y;
		
		if (right && x + width < 800)
			x += up ^ down ? vx * corner : vx;
		if (left && x > 0)
			x -= up ^ down ? vx * corner : vx;
		if (up && y > 0) 
			y -= left ^ right ? vy * corner : vy;
		if (down && y + height < 600) 
			y += left ^ right ? vy * corner : vy;
			
		bounds = getBounds();
	}
	
	public void render(Graphics2D g, int mx, int my, float delta, int ticks) {
		int drawx = (int) ((x - lastx) * delta + lastx);
		int drawy = (int) ((y - lasty) * delta + lasty);
		
		//the higher those two magic numbers, the slower. the second one should be 1/4 of the first b/c there are 4 frames
		g.drawImage(images[(ticks % (8)) / 2], drawx, drawy, width, height, null);
		
		rightAnchorX = drawx + width - width * 0.3;
		leftAnchorX = drawx + width * 0.3;
		anchorY = drawy + height * 0.39 + 1;
		
		double dy = anchorY - my;
		
		if (mx > drawx + width / 2) {
			//right shoulder
			weaponOnRight = true;
			double dx = mx - rightAnchorX;
			
			if (dx == 0) { //avoid divide by zero error
				if (dy < 0) angle = PI / 2; //if cursor is below
				else angle = -PI / 2;
			} else { 
				if (dx < 0) { //if cursor is between anchor and middle of sleigh
					if (dy < 0) angle = PI / 2 + atan(dx / dy); //yes, I know dx / dy. That's correct
					else angle = -PI / 2 + atan(dx / dy);
				} else {
					angle = -atan(dy / dx);
				}
			}
			
			g.translate(rightAnchorX, anchorY);
			g.rotate(angle);
			g.drawImage(rightArmImage, -2, -2, 20, 7, null);
			g.drawImage(weapon.imageRight, 15, -2, weapon.gameWidth, weapon.gameHeight, null);
			g.rotate(-angle);
			g.translate(-rightAnchorX, -anchorY);
		} else {
			//left shoulder
			weaponOnRight = false;
			double dx = mx - leftAnchorX;
			
			if (dx == 0) { //avoid divide by zero error
				if (dy < 0) angle = PI / 2; //if cursor is below
				else angle = -PI / 2;
			} else { 
				if (dx < 0) { //if cursor is between anchor and middle of sleigh
					if (dy < 0) angle = PI / 2 + atan(dx / dy); //yes, I know dx / dy. That's correct
					else angle = -PI / 2 + atan(dx / dy);
				} else {
					angle = -atan(dy / dx);
				}
			}
			
			g.translate(leftAnchorX, anchorY);
			g.rotate(angle);
			g.drawImage(leftArmImage, -3, -3, 20, 7, null);
			g.drawImage(weapon.imageLeft, 15, -2, weapon.gameWidth, weapon.gameHeight, null);
			g.rotate(-angle);
			g.translate(-leftAnchorX, -anchorY);
		}
	}
	
	public Polygon getBounds() {
		int[] x = {0,  17, 17, 9,  8,  0};
		int[] y = {17, 17, 48, 82, 82, 48};
		for (int i = 0; i < x.length; i++) {
			x[i] *= scalex;
			x[i] += this.x;
			
			y[i] *= scaley;
			y[i] += this.y;
		}
		
		return new Polygon(x, y, x.length);
	}

}
