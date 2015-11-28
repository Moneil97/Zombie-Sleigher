package sleigher.zombie;

import java.awt.image.BufferedImage;

public class Bazooka extends Weapon {
	
	public Bazooka() {
		imageRight = ZombieSleigher.bazookaRightImage;
		imageLeft = ZombieSleigher.bazookaLeftImage;
		imageFire = ZombieSleigher.muzzleSmokeImages[0];
		
		//temp
		frameSpeed = 2;
		firing = new BufferedImage[2];
		firing[0] = ZombieSleigher.muzzleSmokeImages[0];
		firing[1] = ZombieSleigher.muzzleSmokeImages[1];
		
		frames = firing.length;
		
		fireSound = ZombieSleigher.pistolSound;
		
		damage = 10;
		rateOfFire = 0.2f;
		
		index = 2;
		
		gamex = 10;
		gamey = -7;
		gameWidth = 30;
		gameHeight = 15;
		
		firex = 45;
		firey = -5;
		firewidth = 20;
		fireheight = 10;
	}

}
