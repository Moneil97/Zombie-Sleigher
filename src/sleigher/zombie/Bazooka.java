package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Bazooka extends Weapon {
	
	int ex;
	int ey;
	
	int eframes = 3;
	int eframeSpeed = 2;
	int countdown;
	BufferedImage[] explosions = new BufferedImage[eframes];
	
	public Bazooka() {
		imageRight = ZombieSleigher.bazookaRightImage;
		imageLeft = ZombieSleigher.bazookaLeftImage;
		imageFire = ZombieSleigher.muzzleSmokeImages[0];
		
		//temp
		frameSpeed = 2;
		firing = new BufferedImage[2];
		firing[0] = ZombieSleigher.muzzleSmokeImages[0];
		firing[1] = ZombieSleigher.muzzleSmokeImages[1];
		
		for (int i = 0; i < eframes; i++) {
			explosions[i] = ZombieSleigher.explosionImages[i];
		}
		
		frames = firing.length;
		
		fireSound = ZombieSleigher.blastSound;
		
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
	
	public void fired() {
		countdown = eframes * eframeSpeed - 1;
	}
	
	public void update() {
		super.update();
		countdown--;
	}
	
	public void render(Graphics2D g, float delta) {
		super.render(g, delta);
		//the higher those two magic numbers, the slower. the second one should be 1/4 of the first b/c there are 4 frames
		if (countdown > -1) g.drawImage(explosions[countdown / eframes], ex, ey, 90, 90, null);
	}

}
