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
			ticks = 1; //set ticks to one, not zero, because ticks increment at end of render method
			frameIndex = frames - 1; //set the frame to last frame in array
			imageFire = firing[frameIndex]; //draw that frame
			cooldown = (int) (ZombieSleigher.UPS / rateOfFire);
		} else {
			fired = false;
		}
	}
	
	public void render(Graphics2D g, float delta) {
		//drawn in the santa class
		//TODO muzzle animation
		if (ticks % frameSpeed == 0) { //every frameSpeed ticks
			frameIndex--; //move back by one in array
			if (frameIndex > -1) imageFire = firing[frameIndex];
		}
		ticks++;
	}
	
	public void mousePressed() {
		triggered = true;
	}
	
	public void mouseReleased() {
		triggered = false;
	}

}
