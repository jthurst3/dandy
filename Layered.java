import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.*;



public class Layered extends JPanel{
	
	private final int WIDTH = 1350;
	private final int HEIGHT = 670;
	private final int SPEED = 10;
	private final int DELAY = 20;
	private final int DELAY2 = 1000;
	//Point point1;
	Point point2;
	int gw, gh;
	int rw, rh;
	boolean move = false;
	Point point1 = new Point(100, 500);
	private Timer timer,timer2;
	ArrayList<Rectangle> rList = new ArrayList<Rectangle>();
	
	private int[] dir = {0,0};
	
	
	public Layered()
	{
		gw = 50;
		gh = 50;
		rw = 50;
		rh = 10;
		setLayout(null);
		setBounds(0, 0, 1350, 670);
		setBackground(Color.blue);
		setVisible(true);
		timer  = new Timer(DELAY, new ProjectileListener());
		timer2 = new Timer(DELAY2, new FListener());
		
		addKeyListener(new GuppyMover());
		setFocusable(true);
		
		timer.start();
		timer2.start();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.black);
		if(move){
			int newx = point1.x + SPEED*dir[0];
			int newy = point1.y + SPEED*dir[1];
			if (0 <= newx && newx <= WIDTH-70){
				point1.x = newx;
			}
			if (0 <= newy && newy <= HEIGHT-90){
				point1.y = newy;
			}
		}
		g2.fillOval(point1.x, point1.y, gw, gh);
		
		g2.setColor(Color.pink);
		for (Rectangle rect : rList){
			g2.fill(rect);
		}
		
	}
	
	
	class GuppyMover implements KeyListener{
		
		public void keyTyped(KeyEvent Event){}
		
		public void keyReleased(KeyEvent Event){
			dir[0] = 0;
			dir[1] = 0;
			move = false;
		}
		
		public void keyPressed(KeyEvent event)
		{
			
			if (!move){
				if(event.getKeyCode() == KeyEvent.VK_RIGHT){
					dir[0] = 1;
					dir[1] = 0;
					move = true;
				}
				
				if(event.getKeyCode() == KeyEvent.VK_LEFT){
					dir[0] = -1;
					dir[1] = 0;
					move = true;
				}
				
				if(event.getKeyCode() == KeyEvent.VK_DOWN){
					dir[0] = 0;
					dir[1] = 1;
					move = true;
				}
				
				if(event.getKeyCode() == KeyEvent.VK_UP){
					dir[0] = 0;
					dir[1] = -1;
					move = true;
				}
			}
			
//			if(event.getKeyCode() == KeyEvent.VK_ENTER){
//				
//			}
			
			
	
			
			
			repaint();
		}
	
	}
	
	private class ProjectileListener implements ActionListener
	{
		
		
		public void actionPerformed (ActionEvent e)
		{
//			rList.add(new Rectangle(WIDTH,(int)(Math.random()*HEIGHT),10,20));
			for (Rectangle rect : rList){
				rect.x -= SPEED;
			}
			repaint();
			
//			//moves ball
//			
//			proj_x += movex;
//			proj_y += movey;
//			
//			
//			
//		
//			if (x <= 0 || x >= getWidth()-IMAGE_SIZE) //collision with walls
//				movex = movex * -1;
//			
//			if (y <= 0) //collisions with ceiling
//				movey = movey * -1;
//			
//			
//			if ((y+IMAGE_SIZE == HEIGHT -25) && (x >= point1.x) && (x <= (point1.x+pw)))
//				movey = movey* -1;
		}
	}
	
	private class FListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e){
			rList.add(new Rectangle(WIDTH,(int)(Math.random()*(HEIGHT-rh)),rw,rh));
		}
	}

}



