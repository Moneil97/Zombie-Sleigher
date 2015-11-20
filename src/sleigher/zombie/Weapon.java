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
	
	int gamex;
	int gamey;
	int gameWidth;
	int gameHeight;
	
	//TODO this is going to suck if it has to be animated
	int firex;
	int firey;
	int firewidth;
	int fireheight;
	
	BufferedImage imageRight;
	BufferedImage imageLeft;
	BufferedImage imageFire;
	
	BufferedImage[] firing;
	int frames;
	int frameIndex;
	int ticks;
	int frameSpeed; //amount of ticks between frame
	
	public void update() {
		cooldown--;
		if (cooldown < 0 && triggered) {
			fired = true;
			ticks = 0;
			frameIndex = frames - 1;
			cooldown = (int) (ZombieSleigher.UPS / rateOfFire);
		} else {
			fired = false;
		}
	}
	
	public void render(Graphics2D g, float delta) {
		//drawn in the santa class
		//TODO muzzle animation
		ticks++;
		if (ticks % frameSpeed == 0) {
			frameIndex--;
			if (frameIndex > -1) imageFire = firing[frameIndex];
		}
	}
	
	public void mousePressed() {
		triggered = true;
	}
	
	public void mouseReleased() {
		triggered = false;
	}

}
