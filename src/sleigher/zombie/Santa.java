package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
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
	
	BufferedImage leftArmImage = ZombieSleigher.santaLeftArmImage;
	BufferedImage rightArmImage = ZombieSleigher.santaRightArmImage;
	BufferedImage images[] = ZombieSleigher.santaImages;
	
	public Santa(float x, float y) {

		this.x = x;
		this.y = y;
		lastx = x;
		lasty = y;
		vx = 5;
		vy = 6;
		
		health = 100;
		
//		width = (int)(18*2.5);//50;
//		height = (int)(83*2.5);//160;
		width = 50;
		height = 140;

		bounds = new Rectangle((int) x, (int) y, width, height);
	}
	
	float corner = 0.70710678118654752440084436210485f;//45 * cos(45);
	
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
			
		//TODO change bounds to be a polygon more fitting of the shape
		bounds = new Rectangle((int) x, (int) y, width, height);
	}
	
	public void render(Graphics2D g, int mx, int my, float delta, int ticks) {
		int drawx = (int) ((x - lastx) * delta + lastx);
		int drawy = (int) ((y - lasty) * delta + lasty);
		
		//the higher those two magic numbers, the slower. the second one should be 1/4 of the first b/c there are 4 frames
		g.drawImage(images[(ticks % (8)) / 2], drawx, drawy, width, height, null);
		
		double rightAnchorX = drawx + width - width * 0.3;
		double leftAnchorX = drawx + width * 0.3;
		double anchorY = drawy + height * 0.39 + 1;
		
		double angle;
		double dy = anchorY - my;
		
		if (mx > drawx + width / 2) {
			//right shoulder
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
			g.rotate(-angle);
			g.translate(-rightAnchorX, -anchorY);
		} else {
			//left shoulder
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
			g.rotate(-angle);
			g.translate(-leftAnchorX, -anchorY);
		}
	}

}
