package sleigher.zombie;

import java.awt.Color;
import java.awt.Graphics2D;

public class BoxButton {
	
	int x, y;
	int width, height;
	
	Color borderColor = Color.red;
	Color textColor = Color.red;
	Color hoverColor = new Color(0, 255, 0, 125);
	
	boolean hovering = false;
	
	String title;
	
	public BoxButton(String title, int x, int y, int width, int height) {
		this.title = title;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void render(Graphics2D g) {
		g.setColor(hoverColor);
		if (hovering)
			g.fillRect(x, y, width, height);
		
		g.setColor(borderColor);
		g.drawRect(x, y, width, height);
		
		int w = g.getFontMetrics().stringWidth(title);
		int h = g.getFontMetrics().getHeight();
		g.setColor(textColor);
		g.drawString(title, x + width / 2 - w / 2, y + height / 2 - h / 2);
	}

}
