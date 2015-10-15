package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Zombie {

	int x,y;
	int width, height;
	BufferedImage image = ZombieSleigher.zombieImage;
	
	public Zombie(int x, int y) {
		this.x = x;
		this.y = y;
		width = 30;
		height = 60;
	}
	
	public void update(){
		y--;
	}
	
	public void render(Graphics2D g, float delta) {
		g.drawImage(image, (int) x, (int) y, width, height, null);
	}

}
