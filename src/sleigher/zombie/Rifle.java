package sleigher.zombie;

public class Rifle extends Weapon {

	public Rifle() {
		
		imageRight = ZombieSleigher.rifleRightImage;
		imageLeft = ZombieSleigher.rifleLeftImage;
		
		damage = 5;
		rateOfFire = 10;
		
		index = 1;
		gameWidth = 30;
		gameHeight = 15;
	}

}
