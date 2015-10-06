package sleigher.zombie;

import javax.swing.JFrame;

public class ZombieSleigher { //we want to make this a canvas and use bufferstrategy, not jpanel

    public static int WIDTH = 600;
    public static int HEIGHT = 600;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Santa Sleigher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        
        //add ZombieSleigher to jframe
        
        frame.setVisible(true);
    }
    
    public ZombieSleigher() {
        
    }

}
