package sleigher.zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Zombie {

	float x, y;
	float ys;
	int width, height;
	BufferedImage image = ZombieSleigher.zombieImage;
	
	public Zombie(int x, int y, int ys) {
		this.x = x;
		this.y = y;
		this.ys = ys;
		
		width = 30;
		height = 60;
	}
	
	public void update(){
		y -= ys;
	}
	
	public void render(Graphics2D g, float delta) {
		g.drawImage(image, (int) x, (int) y, width, height, null);
	}

}
