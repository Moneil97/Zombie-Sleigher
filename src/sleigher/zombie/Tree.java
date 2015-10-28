package sleigher.zombie;

import java.awt.image.BufferedImage;

public class Tree {
	
	float x, y;
	int width, height;
	float hillSpeed;
	
	BufferedImage image = ZombieSleigher.treeImage;
	
	public Tree() {
		
	}
	
	public void update(float hillSpeed) {
		this.hillSpeed = hillSpeed;
		
		
	}

}
