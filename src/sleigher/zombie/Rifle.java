package sleigher.zombie;

public class Rifle extends Weapon {

	public Rifle() {
		
		imageRight = ZombieSleigher.rifleRightImage;
		imageLeft = ZombieSleigher.rifleLeftImage;
		imageFire = ZombieSleigher.rifleFireImage;
		
		damage = 2;
		rateOfFire = 5;
		
		index = 1;
		
		gameWidth = 30;
		gameHeight = 15;
		
		firex = 45;
		firey = -3;
		firewidth = 20;
		fireheight = 10;
	}

}
