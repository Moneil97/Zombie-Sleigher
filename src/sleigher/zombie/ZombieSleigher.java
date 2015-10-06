package sleigher.zombie;

import javax.swing.JFrame;

import com.jackdahms.Controllable;
import com.jackdahms.ControllableThread;

public class ZombieSleigher implements Controllable{ //we want to make this a canvas and use bufferstrategy, not jpanel

    public static int WIDTH = 600;
    public static int HEIGHT = 600;
    
    private ControllableThread ct;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Santa Sleigher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        
        //add ZombieSleigher to jframe
        
        frame.setVisible(true);
    }
    
    public ZombieSleigher() {
    	
    	//Update will be called 60 fps, render will be called default 60 fps
    	ct = new ControllableThread(this);
    	ct.setTargetUps(60);
    	ct.start();
    }
    
    public void update() {
    	
    }
    
    public void render(float delta) {
    	
    }

}
