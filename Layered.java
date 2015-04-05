import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.*;

import java.awt.geom.Rectangle2D;
import java.awt.font.LineMetrics;

import javax.imageio.ImageIO;
import java.io.*;

public class Layered extends JPanel{
	int currlevel;
    private final int FRAMEPAUSE = 20;
    private final int NEWTOKENPAUSE = 1000;
    int gw=50, gh=30;
    int rw=50, rh=20;

	double myx, myy;
	double mapx, mapy;

	private Timer timer,timer2;
    ArrayList<Token> rList = new ArrayList<Token>();
	ArrayList<Token> pList = new ArrayList<Token>();
    Font font = new Font("TimesRoman", Font.PLAIN, 24);
	
	long lastpaint;
    double speed_per_ms = .25;
	double accel_per_ms = .001;
	double token_speed_per_ms = .05;
	double token_max_accel = .005;

	String programtext="";
    
    int dir[] = {0,0,0,0};
	double vel[] = {0.0, 0.0};

	Image testpic;
    Parser p;
	
	ArrayList<Rectangle> obstacles;
    
    public Layered()
    {
		
		try{
			testpic = ImageIO.read(new BufferedInputStream(new FileInputStream("testpic.png")));
		} catch(Exception e){}
		initlevel(0);
		
		lastpaint = System.currentTimeMillis();
		
        setBackground(new Color(80, 103, 175));
        setVisible(true);
        timer = new Timer(FRAMEPAUSE, new PaintListener());
        timer2 = new Timer(NEWTOKENPAUSE, new FListener());
        
        addKeyListener(new GuppyMover());
        setFocusable(true);
        
        timer.start();
        timer2.start();
    }

    public void initlevel(int level){
        currlevel = level;
        
        obstacles = new ArrayList<Rectangle>(10);
        
        obstacles.add(new Rectangle(0,0,400,400));
        obstacles.add(new Rectangle(600,200,50,50));
        obstacles.add(new Rectangle(600,400,50,50));
        obstacles.add(new Rectangle(1600,0,200,200));
        
        p = new Parser();
        pList = new ArrayList<Token>();
        rList = new ArrayList<Token>();
        rList.add(new Token("function", TokenType.FUNCTIONSTART));
        rList.add(new Token("level" + level, TokenType.FUNCTION));
        rList.add(new Token("(", TokenType.LPAREN));
        
        pList.add(new Token("answer", TokenType.VARIABLE));
        
        switch(level)
        {
        case 1:
            pList.add(new Token("=", TokenType.EQ));
            pList.add(new Token("1", TokenType.INTEGER));
            pList.add(new Token(";", TokenType.SEMICOLON));
            break;
        
        case 2:
            pList.add(new Token("=", TokenType.EQ));
            pList.add(new Token("1", TokenType.INTEGER));
            pList.add(new Token("+", TokenType.PLUS));
            pList.add(new Token(";", TokenType.SEMICOLON));
            break;
            
        case 3:
            pList.add(new Token("=", TokenType.EQ));
            pList.add(new Token("1", TokenType.INTEGER));
            pList.add(new Token("+", TokenType.PLUS));
            pList.add(new Token("*", TokenType.TIMES));
            pList.add(new Token("(", TokenType.LPAREN));
            pList.add(new Token(")", TokenType.RPAREN));
            pList.add(new Token(";", TokenType.SEMICOLON));
            break;
            
        case 4:
            pList.add(new Token("=", TokenType.EQ));
            pList.add(new Token("x", TokenType.VARIABLE));
            pList.add(new Token(";", TokenType.SEMICOLON));

            rList.add(new Token("x", TokenType.VARIABLE));
            break;
            
        case 5:
            pList.add(new Token("=", TokenType.EQ));
            pList.add(new Token("x", TokenType.VARIABLE));
            pList.add(new Token("+", TokenType.PLUS));
            pList.add(new Token(";", TokenType.SEMICOLON));

            rList.add(new Token("x", TokenType.VARIABLE));
            break;
        
        case 6:
            pList.add(new Token("=", TokenType.EQ));
            pList.add(new Token("x", TokenType.VARIABLE));
            pList.add(new Token("y", TokenType.VARIABLE));
            pList.add(new Token("+", TokenType.PLUS));
            pList.add(new Token("*", TokenType.TIMES));
            pList.add(new Token(";", TokenType.SEMICOLON));

            rList.add(new Token("x", TokenType.VARIABLE));
            break;
            
        case 7:
            pList.add(new Token("x", TokenType.VARIABLE));
            pList.add(new Token("y", TokenType.VARIABLE));
            pList.add(new Token("=", TokenType.EQ));
            pList.add(new Token("levelsix", TokenType.FUNCTION));
            pList.add(new Token("(", TokenType.LPAREN));
            pList.add(new Token(")", TokenType.RPAREN));
            pList.add(new Token(";", TokenType.SEMICOLON));

            rList.add(new Token("x", TokenType.VARIABLE));
            break;
            
            
        case 8:
            pList.add(new Token("x", TokenType.VARIABLE));
            pList.add(new Token("y", TokenType.VARIABLE));
            pList.add(new Token("=", TokenType.EQ));
            pList.add(new Token("(", TokenType.LPAREN));
            pList.add(new Token(")", TokenType.RPAREN));
            pList.add(new Token("{", TokenType.LBRACE));
            pList.add(new Token("}", TokenType.RBRACE));
            pList.add(new Token(">", TokenType.GREATERTHAN));
            pList.add(new Token("if", TokenType.IF));
            pList.add(new Token("else", TokenType.ELSE));
            pList.add(new Token(";", TokenType.SEMICOLON));

            rList.add(new Token("x", TokenType.VARIABLE));
            rList.add(new Token(",", TokenType.COMMA));
            rList.add(new Token("y", TokenType.VARIABLE));
            break;
            
        case 9:
            pList.add(new Token("x", TokenType.VARIABLE));
            pList.add(new Token("y", TokenType.VARIABLE));
            pList.add(new Token("z", TokenType.VARIABLE));
            pList.add(new Token("(", TokenType.LPAREN));
            pList.add(new Token(")", TokenType.RPAREN));
            pList.add(new Token("=", TokenType.EQ));
            pList.add(new Token("leveleight", TokenType.FUNCTION));
            pList.add(new Token(",", TokenType.COMMA));
            pList.add(new Token(";", TokenType.SEMICOLON));

            rList.add(new Token("x", TokenType.VARIABLE));
            rList.add(new Token(",", TokenType.COMMA));
            rList.add(new Token("y", TokenType.VARIABLE));
            rList.add(new Token(",", TokenType.COMMA));
            rList.add(new Token("z", TokenType.VARIABLE));
            break;
            
        case 10:
            pList.add(new Token("x", TokenType.VARIABLE));
            pList.add(new Token("=", TokenType.EQ));
            pList.add(new Token("+", TokenType.PLUS));
            pList.add(new Token("(", TokenType.LPAREN));
            pList.add(new Token(")", TokenType.RPAREN));
            pList.add(new Token("{", TokenType.LBRACE));
            pList.add(new Token("}", TokenType.RBRACE));
            pList.add(new Token(">", TokenType.GREATERTHAN));
            pList.add(new Token("if", TokenType.IF));
            pList.add(new Token("else", TokenType.ELSE));
            pList.add(new Token(";", TokenType.SEMICOLON));
            pList.add(new Token("0", TokenType.INTEGER));
            pList.add(new Token("1", TokenType.INTEGER));
            pList.add(new Token("2", TokenType.INTEGER));
            pList.add(new Token("levelten", TokenType.FUNCTION));
            pList.add(new Token("-", TokenType.MINUS));

            rList.add(new Token("x", TokenType.VARIABLE));
            break;  
        
        }

        
        


        rList.add(new Token(")", TokenType.RPAREN));
        rList.add(new Token("{", TokenType.LBRACE));
        StringBuilder sb = new StringBuilder();
        for(Token t: rList){
            sb = sb.append(t.content).append(" ");
            p.addAndEvaluate(t);
        }
        programtext = sb.append("\n").toString();
        
        myx = 100.0;
        myy = 500.0;
        
        mapx = 0.0;
        mapy = 0.0;
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
		g.setFont(font);
		g.setColor(Color.black);
		
		for(Rectangle r: obstacles){
			if(r.getX() + r.getWidth() > mapx && r.getX() < mapx+getWidth()){
				g.drawImage(testpic, (int)(r.getX()-mapx), (int)(r.getY()-mapy), (int)r.getWidth(), (int)r.getHeight(), Color.black, this);
			}
		}
		
        Graphics2D g2 = (Graphics2D) g;

		long time = System.currentTimeMillis();
		long diff = time-lastpaint;
		lastpaint = time;
		
		vel[0] += (accel_per_ms*diff*(dir[1]-dir[0]));
		vel[1] += (accel_per_ms*diff*(dir[3]-dir[2]));
		
		if(vel[0] < -speed_per_ms) vel[0] =-speed_per_ms;
		if(vel[0] >  speed_per_ms) vel[0] = speed_per_ms;
		if(vel[1] < -speed_per_ms) vel[1] =-speed_per_ms;
		if(vel[1] >  speed_per_ms) vel[1] = speed_per_ms;
		
		double newx = myx + vel[0]*diff;
		double newy = myy + vel[1]*diff;
		if(newx < mapx){
			newx = mapx;
			vel[0] = 0;
		} else if(newx > mapx+getWidth()-gw){
			newx = mapx+getWidth()-gw;
			vel[0] = 0;
		}
		if(newy < mapy){
			newy = mapy;
			vel[1] = 0;
		} else if(newy > mapy+getHeight()-gh){
			newy = mapy+getHeight()-gh;
			vel[1] = 0;
		}

		if(conflicts(new Rectangle((int)newx,(int)newy,gw,gh))){
			vel[0] = 0;
			vel[1] = 0;
		} else {
			myx = newx;
			myy = newy;
		}
		
		double map_velocity = (newx-(mapx+2.0*getWidth()/3))/1000.0;
		if(map_velocity < 0) map_velocity = 0;
		if(vel[0] >= 0 && map_velocity > vel[0]) map_velocity = vel[0];
		mapx += map_velocity*diff;

        g2.fillOval((int)(myx-mapx), (int)(myy-mapy), gw, gh);
        
        int xPoly[] = {(int)(myx - 30-mapx), (int)(myx - 30-mapx), (int)(myx + 3-mapx)};
        int yPoly[] = {(int)(myy - 5-mapy), (int)(myy + 30-mapy), (int)(myy + gh/2-mapy)};

        g2.fill(new Polygon(xPoly, yPoly, xPoly.length));
        
        for (Iterator<Token> i = rList.iterator(); i.hasNext();){
			Token token = i.next();

			token.vel += ((Math.random()-.5)*2)*token_max_accel;
			token.y += token.vel*diff;

			Rectangle2D tokenbounds = g.getFontMetrics().getStringBounds(token.content, g);
			LineMetrics tokenmetrics = g.getFontMetrics().getLineMetrics(token.content, g);

			if(token.y < mapy+tokenmetrics.getAscent() || token.y > mapy+getHeight()-tokenmetrics.getDescent()){
				i.remove();
				continue;
			}
						
            token.x -= token_speed_per_ms*diff;
			if(token.x < mapx){
				i.remove();
				continue;
			}
			
			if (tokenbounds.intersects((int)(myx-token.x), (int)(myy-token.y), gw, gh)) {
                // if it was hit, see if the new token would produce a syntax error
				if(token.type == TokenType.RBRACE) programtext += "\n";
				programtext += token.content;
				if(token.type == TokenType.RBRACE || token.type == TokenType.LBRACE || token.type == TokenType.SEMICOLON) programtext += "\n";

                boolean valid = p.addAndEvaluate(token);
                if (!valid) {
					initlevel(currlevel);
					return;
				}
                // remove the rectangle from the list
                i.remove();
				continue;
            }				
			// trick from http://stackoverflow.com/questions/7679459/thick-border-of-drawn-string
			//g2.setColor(Color.red);
			//g2.fill(token.rect);
			g2.setColor(Color.black);
			g2.drawString(token.content, (int)(token.x-1-mapx), (int)(token.y-1-mapy));
			g2.drawString(token.content, (int)(token.x-1-mapx), (int)(token.y+1-mapy));
			g2.drawString(token.content, (int)(token.x+1-mapx), (int)(token.y-1-mapy));
			g2.drawString(token.content, (int)(token.x+1-mapx), (int)(token.y+1-mapy));
			g2.setColor(Color.white);
			g2.drawString(token.content, (int)(token.x-mapx), (int)(token.y-mapy));
        }
		
		Scanner s = new Scanner(programtext);
		g2.setColor(Color.black);
		for(int line=0; s.hasNextLine(); line++){
			g.drawString(s.nextLine(), 100, 100+20*line);
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
			t2.x = mapx+getWidth();
			t2.y = mapy+Math.random()*(getHeight()-rh);
            rList.add(t2);
        }
    }
	
	boolean conflicts(Rectangle test){
		for(Rectangle r: obstacles){
			if(test.intersects(r)) return true;
		}
		return false;
	}
}
