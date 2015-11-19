package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Weapon {
	
	//in shots per second
	float rateOfFire; //max thirty
	float damage;
	
	//TODO automatic, semi auto
	boolean triggered = false;
	boolean fired = false;
	
	//whether or not the weapon is available to use
	boolean purchased = false;
	
	int cooldown = 0;
	int index; //weapon index
	
	int gameWidth;
	int gameHeight;
	
	BufferedImage imageRight;
	BufferedImage imageLeft;
	
	public void update() {
		cooldown--;
		if (cooldown < 0 && triggered) {
			fired = true;
			cooldown = (int) (ZombieSleigher.UPS / rateOfFire);
		} else {
			fired = false;
		}
	}
	
	public void render(Graphics2D g, float delta) {
		//drawn in the santa class
	}
	
	public void mousePressed() {
		triggered = true;
	}
	
	public void mouseReleased() {
		triggered = false;
	}

}
