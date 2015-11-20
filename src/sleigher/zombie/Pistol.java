package sleigher.zombie;

public class Pistol extends Weapon {
	
	public Pistol() {
		imageRight = ZombieSleigher.pistolRightImage;
		imageLeft = ZombieSleigher.pistolLeftImage;
		
		damage = 8;
		rateOfFire = 1.5f;
		
		index = 0;
		gameWidth = 10;
		gameHeight = 6;
	}

}
