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

	String programtext;
	int tabs=0;
    
    int dir[] = {0,0,0,0};
	double vel[] = {0.0, 0.0};

	Image testpic;
    Parser p;
	
	ArrayList<Thing> things;
    
    public Layered()
    {
		
		try{
			testpic = ImageIO.read(new BufferedInputStream(new FileInputStream("testpic.png")));
		} catch(Exception e){
			System.exit(0);
		}
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

		things = new ArrayList<Thing>(10);
		things.add(new Thing(new Rectangle(0,0,400,550), Thing.ThingType.OBSTACLE, testpic));
		things.add(new Thing(new Rectangle(600,200,50,50), Thing.ThingType.OBSTACLE, testpic));
		things.add(new Thing(new Rectangle(600,400,50,50), Thing.ThingType.OBSTACLE, testpic));
		things.add(new Thing(new Rectangle(1600,0,200,200), Thing.ThingType.OBSTACLE, testpic));
		things.add(new Thing(new Rectangle(80, 600, 1, 1), Thing.ThingType.FIXEDTOKEN, new Token("function ", TokenType.FUNCTIONSTART)));
		things.add(new Thing(new Rectangle(200, 600, 1, 1), Thing.ThingType.FIXEDTOKEN, new Token("level"+level, TokenType.FUNCTION)));
		things.add(new Thing(new Rectangle(300, 600, 1, 1), Thing.ThingType.FIXEDTOKEN, new Token("(", TokenType.LPAREN)));
		things.add(new Thing(new Rectangle(350, 600, 1, 1), Thing.ThingType.FIXEDTOKEN, new Token(")", TokenType.RPAREN)));
		things.add(new Thing(new Rectangle(400, 600, 1, 1), Thing.ThingType.FIXEDTOKEN, new Token("{", TokenType.LBRACE)));
		things.add(new Thing(new Rectangle(2000, 300, 200, 200), Thing.ThingType.END, testpic));
		
		rList = new ArrayList<Token>();
		programtext = "";
				
        p = new Parser();

		pList = new ArrayList<Token>();
		pList.add(new Token("answer", TokenType.VARIABLE));
		pList.add(new Token("=", TokenType.EQ));
		pList.add(new Token("1", TokenType.INTEGER));
		pList.add(new Token("+", TokenType.PLUS));
		pList.add(new Token(";", TokenType.SEMICOLON));
		
		myx = 10.0;
		myy = 580.0;
		
		mapx = 0.0;
		mapy = 0.0;
	}
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
		g.setFont(font);
		
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

		Thing t = conflicts(new Rectangle((int)newx,(int)newy,gw,gh));
		if(t == null || t.type == Thing.ThingType.FIXEDTOKEN){
			myx = newx;
			myy = newy;
		}
		if(t != null) switch(t.type){
		case OBSTACLE:
			vel[0] = 0;
			vel[1] = 0;
			break;
		case FIXEDTOKEN:
			System.out.println("DEBUG!");
			if(!grabToken((Token)t.data)){
				initlevel(currlevel);
				return;
			} else {
				things.remove(t);
			}
			break;
		case END:
			if(testProgram()){
				initlevel(currlevel+1);
			} else {
				initlevel(currlevel);
			}
			return;
		}
		
		for(Iterator<Thing> i = things.iterator(); i.hasNext();){
			Thing th = i.next();
			Rectangle2D r = th.bounds;
			if(r.getX() + r.getWidth() <= mapx || r.getX() >= mapx+getWidth()) continue;
			
			switch(th.type){
			case OBSTACLE:
			case END:
				g.drawImage((Image)th.data, (int)(r.getX()-mapx), (int)(r.getY()-mapy), (int)r.getWidth(), (int)r.getHeight(), Color.black, this);
				break;
			case FIXEDTOKEN:
				Token to = (Token)th.data;
				paintTokenString(g, to.content, (int)(r.getX()-mapx), (int)(r.getY()-mapy));
				break;
			}
		}

		double map_velocity = (newx-(mapx+2.0*getWidth()/3))/1000.0;
		if(map_velocity < 0) map_velocity = 0;
		if(vel[0] >= 0 && map_velocity > vel[0]) map_velocity = vel[0];
		mapx += map_velocity*diff;

		g.setColor(Color.black);

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
                if (!grabToken(token)) {
					initlevel(currlevel);
					return;
				}
                // remove the rectangle from the list
                i.remove();
				continue;
            }
			// trick from http://stackoverflow.com/questions/7679459/thick-border-of-drawn-string
			paintTokenString(g, token.content, (int)(token.x-mapx), (int)(token.y-mapy));
        }
		
		Scanner s = new Scanner(programtext);
		g2.setColor(Color.black);
		for(int line=0; s.hasNextLine(); line++){
			String l = s.nextLine();
			int tb=0;
			for(char c : l.toCharArray()){
				if(c == '\t') tb++;
				else break;
			}
			g.drawString(l, 100+30*tb, 100+20*line);
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
	
	Thing conflicts(Rectangle test){
		for(Thing t: things){
			if(test.intersects(t.bounds)) return t;
		}
		return null;
	}
	
	boolean grabToken(Token token){
		// if it was hit, see if the new token would produce a syntax error
		if(token.type == TokenType.RBRACE){
			tabs--;
			programtext += "\n";
			for(int i = 0; i < tabs; i++) programtext += "\t";
		}
		programtext += token.content;
		if(token.type == TokenType.LBRACE) tabs++;
		if(token.type == TokenType.RBRACE || token.type == TokenType.LBRACE || token.type == TokenType.SEMICOLON){
			programtext += "\n";
			for(int i = 0; i < tabs; i++) programtext += "\t";
		}

		return p.addAndEvaluate(token);
	}
	
	boolean testProgram(){
		return true;
	}
	
	void paintTokenString(Graphics g, String s, int x, int y){
		g.setColor(Color.black);
		g.drawString(s, x-1, y-1);
		g.drawString(s, x-1, y+1);
		g.drawString(s, x+1, y-1);
		g.drawString(s, x+1, y+1);
		g.setColor(Color.white);
		g.drawString(s, x, y);
	}
}
