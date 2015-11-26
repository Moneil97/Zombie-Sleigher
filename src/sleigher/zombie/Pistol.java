package sleigher.zombie;

import java.awt.image.BufferedImage;

public class Pistol extends Weapon {
	
	public Pistol() {
		imageRight = ZombieSleigher.pistolRightImage;
		imageLeft = ZombieSleigher.pistolLeftImage;
		
		firing = new BufferedImage[2];
		firing[0] = ZombieSleigher.rifleFireImage;
		firing[1] = ZombieSleigher.rifleFireImage;
		
		fireSound = ZombieSleigher.pistolSound;
		
		frames = firing.length;
		frameSpeed = 2;
		
		damage = 8;
		rateOfFire = 1.5f;
		
		index = 0;
		
		gamex = 15;
		gamey = -2;
		gameWidth = 10;
		gameHeight = 6;
		
		firex = 25;
		firey = -5;
		firewidth = 20;
		fireheight = 10;
	}

}
