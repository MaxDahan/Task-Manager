import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class HorScrollBarUI extends BasicScrollBarUI {
		//part behind scroll
	    protected void paintTrack(Graphics g, JComponent c, Rectangle tb) {
	    	g.setColor(new Color(226, 226, 226));
	    	g.fillRect((int)tb.getX(), (int)tb.getY(), (int)tb.getWidth(), (int)tb.getHeight());
	    	
	    }
	    //actual bar
	    protected void paintThumb(Graphics g, JComponent c, Rectangle tb) {
	    	g.setColor(new Color(70, 70, 70));
	    	g.fillRect((int)tb.getX() + 5, (int)tb.getY() + 2, (int)tb.getWidth() - 10, (int)tb.getHeight() - 4);
	    	
	    	//top rounded edge
	    	g.fillOval((int)tb.getX(), (int)tb.getY() + 2, 12, 12);
	    	
	    	//bottom rounded edge
	    	g.fillOval((int)tb.getX() + (int)tb.getWidth() - 13, (int)tb.getY() + 2, 12, 12);
	    }
	    
	    protected JButton createDecreaseButton(int orientation) {
	    	JButton button = new JButton();
	        BufferedImage img = null;
	        button.setBorder(null);
	        button.setFocusable(false);
	        try {img = ImageIO.read(new File("data\\left.png"));} catch (IOException e1) {e1.printStackTrace();}
	        button.setIcon(new ImageIcon(img));
	        button.setPreferredSize(new Dimension(14, 14));
		    button.setForeground(Color.BLACK);
	        button.setBackground(new Color(226, 226, 226));
	        return button;
	    }
	 
	    protected JButton createIncreaseButton(int orientation) {
	    	JButton button = new JButton();
	        BufferedImage img = null;
	        button.setBorder(null);
	        button.setFocusable(false);
	        try {img = ImageIO.read(new File("data\\right.png"));} catch (IOException e1) {e1.printStackTrace();}
	        button.setIcon(new ImageIcon(img));
	        button.setPreferredSize(new Dimension(14, 14));
		    button.setForeground(Color.BLACK);
	        button.setBackground(new Color(226, 226, 226));
	        return button;
	    }
	}