package sleigher.zombie;

import java.awt.image.BufferedImage;

public class Rifle extends Weapon {

	public Rifle() {
		
		imageRight = ZombieSleigher.rifleRightImage;
		imageLeft = ZombieSleigher.rifleLeftImage;
		imageFire = ZombieSleigher.muzzleFireImages[0];
		
		//temp
		frameSpeed = 2;
		firing = new BufferedImage[2];
		firing[0] = ZombieSleigher.muzzleFireImages[0];
		firing[1] = ZombieSleigher.muzzleFireImages[1];
		
		frames = firing.length;
		
		fireSound = ZombieSleigher.pistolSound;
		
		damage = 2;
		rateOfFire = 5;
		
		index = 1;
		
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
