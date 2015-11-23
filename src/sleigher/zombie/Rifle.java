package sleigher.zombie;

import java.awt.image.BufferedImage;

public class Rifle extends Weapon {

	public Rifle() {
		
		imageRight = ZombieSleigher.rifleRightImage;
		imageLeft = ZombieSleigher.rifleLeftImage;
		imageFire = ZombieSleigher.rifleFireImage;
		
		//temp
		frameSpeed = 2;
		firing = new BufferedImage[2];
		firing[0] = ZombieSleigher.rifleFireImage;
		firing[1] = ZombieSleigher.rifleFireImage;
		
		frames = firing.length;
		
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
