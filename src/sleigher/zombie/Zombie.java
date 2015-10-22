package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Zombie {

	float x, y;
	float hillSpeed;
	float speed;
	int width, height;
	BufferedImage image = ZombieSleigher.zombieImage;
	
	public Zombie(int x, int y, int ys) {
		this.x = x;
		this.y = y;
		this.hillSpeed = ys;
		
		width = 30;
		height = 60;
	}
	
	public void update(float santax, float santay, int santawidth, int santaheight){
		y -= hillSpeed;
		
		if (y > santay && y + height < santay + santaheight) {
			speed = hillSpeed;
		} else if (y < santay) {
			speed = hillSpeed + 2;
		} else {
			speed = hillSpeed - 2;
		}
		
		y += speed;
	}
	
	/**
	 * TODO 
	 * incorporate delta
	 */
	public void render(Graphics2D g, float delta) {
		g.drawImage(image, (int) x, (int) y, width, height, null);
	}

}
