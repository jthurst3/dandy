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
	// boolean hit = false;
	int gw, gh;
	int rw, rh;
	boolean move = false;
	Point point1 = new Point(100, 500);
	private Timer timer,timer2;
	ArrayList<Token> rList = new ArrayList<Token>();
	Polygon tail = new Polygon();
	
	private int[] dir = {0,0};
	
	
	public Layered()
	{
		gw = 50;
		gh = 30;
		rw = 50;
		rh = 10;
		setLayout(null);
		setBounds(0, 0, 1350, 670);
		Color myblue = new Color(80,103,175);
		setBackground(myblue);
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
		
		int xPoly[] = {point1.x - 30, point1.x - 30, point1.x + 3};
        int yPoly[] = {point1.y - 5, point1.y + 30, point1.y + gh/2};

        tail = new Polygon(xPoly, yPoly, xPoly.length);
        
		g2.fill(tail);
		
		// g2.setColor(Color.red);
		
		// if(hit)
		// {
		// 	g2.setColor(Color.green);
		// 	hit = false;
		// }
		
		
		for (Token token : rList){
			// set a rectangle red or green depending on if it was hit
			Color color = (token.hit ? Color.green : Color.red);
			g2.setColor(color);
			token.hit = false;
			g2.fill(token.rect);
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
			
			
	
			
			
			repaint();
		}
	
	}
	
	private class ProjectileListener implements ActionListener
	{
		
		
		public void actionPerformed (ActionEvent e)
		{
//			rList.add(new Rectangle(WIDTH,(int)(Math.random()*HEIGHT),10,20));
			for (Token token : rList){
				Rectangle rect = token.rect;
				rect.x -= SPEED;
				if(((rect.x >= point1.x) && (rect.x <= (point1.x + gw))) && ((rect.y >= point1.y) && (rect.y <= (point1.y + gh))))
					token.hit = true;
					
			}
			repaint();
			
		}
	}
	
	private class FListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e){
			rList.add(new Token("asdf", TokenType.UNDEFINED, new Rectangle(WIDTH,(int)(Math.random()*(HEIGHT-rh)),rw,rh)));
		}
	}

}



