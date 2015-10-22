package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Zombie {

	float x, y;
	float hillSpeed;
	float xs, ys;
	int width, height;
	BufferedImage image = ZombieSleigher.zombieImage;
	
	public Zombie(int x, int y, int ys) {
		this.x = x;
		this.y = y;
		this.hillSpeed = ys;
		
		width = 40;
		height = 60;
	}
	
	public void update(float santax, float santay, int santawidth, int santaheight){
		y -= hillSpeed;
		
		if (y > santay && y + height < santay + santaheight) {
			ys = hillSpeed;
		} else if (y < santay) {
			ys = hillSpeed + 2;
		} else {
			ys = hillSpeed - 2;
		}
		
		int gap = 30;
		if (x + width > santax - gap && x < santax + santawidth + gap) {
			xs = 0;
		} else if (x + width < santax - gap) {
			xs = 2;
		} else {
			xs = -2;
		}
		
		x += xs;
		y += ys;
	}
	
	/**
	 * TODO 
	 * incorporate delta
	 */
	public void render(Graphics2D g, float delta) {
		g.drawImage(image, (int) x, (int) y, width, height, null);
	}

}
