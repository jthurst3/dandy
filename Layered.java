import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.*;

import java.awt.geom.Rectangle2D;
import java.awt.font.LineMetrics;

public class Layered extends JPanel{
	int curr_scroll = 0;
    
    private final int FRAMEPAUSE = 20;
    private final int NEWTOKENPAUSE = 1000;
    int gw, gh;
    int rw, rh;
		
    Point point1 = new Point(100, 500);
    private Timer timer,timer2;
    ArrayList<Token> rList = new ArrayList<Token>();
	ArrayList<Token> pList = new ArrayList<Token>();
    Polygon tail = new Polygon();
    Font font = new Font("TimesRoman", Font.PLAIN, 24);
	
	long lastpaint;
    double speed_per_ms = .25;
	double accel_per_ms = .001;
	double token_speed_per_ms = .1;
	double token_max_accel = .005;

	String programtext="";
    
    int dir[] = {0,0,0,0};
	double vel[] = {0.0, 0.0};

    Parser p;
    boolean gameOver = false;
  
    
    public Layered()
    {
        p = new Parser();
		initlevel(0);
		
		lastpaint = System.currentTimeMillis();
		
        gw = 50;
        gh = 30;
        rw = 50;
        rh = 20;
        Color myblue = new Color(80,103,175);
        setBackground(myblue);
        setVisible(true);
        timer = new Timer(FRAMEPAUSE, new PaintListener());
        timer2 = new Timer(NEWTOKENPAUSE, new FListener());
        
        addKeyListener(new GuppyMover());
        setFocusable(true);
        
        timer.start();
        timer2.start();
    }
	
	public void initlevel(int level){
		pList = new ArrayList<Token>();
		pList.add(new Token("answer", TokenType.VARIABLE));
		pList.add(new Token("=", TokenType.EQ));
		pList.add(new Token("1", TokenType.INTEGER));
		pList.add(new Token("+", TokenType.PLUS));
		pList.add(new Token(";", TokenType.SEMICOLON));


		rList = new ArrayList<Token>();
		rList.add(new Token("function", TokenType.FUNCTIONSTART));
		rList.add(new Token("level" + level, TokenType.FUNCTION));
		rList.add(new Token("(", TokenType.LPAREN));
		rList.add(new Token(")", TokenType.RPAREN));
		rList.add(new Token("{", TokenType.LBRACE));
		StringBuilder sb = new StringBuilder();
		for(Token t: rList){
			sb = sb.append(t.content).append(" ");
			p.addAndEvaluate(t);
		}
		programtext = sb.append("\n").toString();
	}
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
		g.setFont(font);
		
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);

		long time = System.currentTimeMillis();
		long diff = time-lastpaint;
		lastpaint = time;
		
		vel[0] += (accel_per_ms*diff*(dir[1]-dir[0]));
		vel[1] += (accel_per_ms*diff*(dir[3]-dir[2]));
		
		if(vel[0] < -speed_per_ms) vel[0] =-speed_per_ms;
		if(vel[0] >  speed_per_ms) vel[0] = speed_per_ms;
		if(vel[1] < -speed_per_ms) vel[1] =-speed_per_ms;
		if(vel[1] >  speed_per_ms) vel[1] = speed_per_ms;
		
		int newx = point1.x + (int)(vel[0]*diff);
		int newy = point1.y + (int)(vel[1]*diff);
		if(newx < 0){
			newx = 0;
			vel[0] = 0;
		} else if(newx > getWidth()-gw){
			newx = getWidth()-gw;
			vel[0] = 0;
		}
		if(newy < 0){
			newy = 0;
			vel[1] = 0;
		} else if(newy > getHeight()-gh){
			newy = getHeight()-gh;
			vel[1] = 0;
		}
		point1.x = newx;
		point1.y = newy;

        g2.fillOval(point1.x, point1.y, gw, gh);
        
        int xPoly[] = {point1.x - 30, point1.x - 30, point1.x + 3};
        int yPoly[] = {point1.y - 5, point1.y + 30, point1.y + gh/2};

        tail = new Polygon(xPoly, yPoly, xPoly.length);
        
        g2.fill(tail);
        
        for (Iterator<Token> i = rList.iterator(); i.hasNext();){
			Token token = i.next();
            // set a rectangle red or green depending on if it was hit

			token.vel += ((Math.random()-.5)*2)*token_max_accel;
			
			token.y += token.vel*diff;

			Rectangle2D tokenbounds = g.getFontMetrics().getStringBounds(token.content, g);
			LineMetrics tokenmetrics = g.getFontMetrics().getLineMetrics(token.content, g);

			if(token.y < tokenmetrics.getAscent()  || token.y > getHeight()){
				i.remove();
				continue;
			}
						
            token.x -= (int)(token_speed_per_ms*diff);
			if(token.x < 0){
				i.remove();
				continue;
			}
			
            if (tokenbounds.intersects(point1.x-token.x, point1.y-token.y, gw, gh)) {
                // if it was hit, see if the new token would produce a syntax error
				if(token.type == TokenType.RBRACE) programtext += "\n";
				programtext += token.content;
				if(token.type == TokenType.RBRACE || token.type == TokenType.LBRACE || token.type == TokenType.SEMICOLON) programtext += "\n";

                boolean valid = p.addAndEvaluate(token);
                if (!valid) {
					timer.stop();
					timer2.stop();
					SwingUtilities.getWindowAncestor(this).setVisible(false);
					SwingUtilities.getWindowAncestor(this).dispose();
					JOptionPane.showMessageDialog(null, "Syntax error, game over!"); 
				}
                // remove the rectangle from the list
                i.remove();
				continue;
            }				
			// trick from http://stackoverflow.com/questions/7679459/thick-border-of-drawn-string
			//g2.setColor(Color.red);
			//g2.fill(token.rect);
			g2.setColor(Color.black);
			g2.drawString(token.content, (int)(token.x-1), (int)(token.y-1));
			g2.drawString(token.content, (int)(token.x-1), (int)(token.y+1));
			g2.drawString(token.content, (int)(token.x+1), (int)(token.y-1));
			g2.drawString(token.content, (int)(token.x+1), (int)(token.y+1));
			g2.setColor(Color.white);
			g2.drawString(token.content, (int)token.x, (int)(token.y));
        }
		
		Scanner s = new Scanner(programtext);
		g2.setColor(Color.black);
		for(int line=0; s.hasNextLine(); line++){
			g.drawString(s.nextLine(), 100, 100+20*line);
		}

        if (gameOver) {
            g2.drawString("GAME OVER", 500, 500);
        }
    }

    // // draws the text of the user's current program
	// public void drawCurrentProgram(Graphics2D g2) {
		
		// g2.setColor(Color.black);
		// // http://stackoverflow.com/questions/18249592/how-to-change-font-size-in-drawstring-java
		// g2.setFont(font);
		// g2.drawString(p.currentProgramString, 100, 100);
	// }
    
    
    class GuppyMover implements KeyListener{
        
        public void keyTyped(KeyEvent event){}
        
        public void keyReleased(KeyEvent event){
			switch(event.getKeyCode()){
			case KeyEvent.VK_LEFT:
				dir[0] = 0;
				break;
			case KeyEvent.VK_RIGHT:
				dir[1] = 0;
				break;
			case KeyEvent.VK_UP:
				dir[2] = 0;
				break;
			case KeyEvent.VK_DOWN:
				dir[3] = 0;
				break;
			}
        }
        
        public void keyPressed(KeyEvent event)
        {
            switch(event.getKeyCode()){
			case KeyEvent.VK_LEFT:
				dir[0] = 1;
				break;
			case KeyEvent.VK_RIGHT:
				dir[1] = 1;
				break;
			case KeyEvent.VK_UP:
				dir[2] = 1;
				break;
			case KeyEvent.VK_DOWN:
				dir[3] = 1;
				break;
			}
            repaint();
        }
    
    }
        
    private class PaintListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e){
			repaint();
        }
    }

    private class FListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e){
			Token t = pList.get((int)(pList.size()*Math.random()));
			Token t2 = new Token(t.content, t.type);
			t2.x = getWidth();
			t2.y = Math.random()*(getHeight()-rh);
            rList.add(t2);
        }
    }
}
