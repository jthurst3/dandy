import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.*;

import java.math.BigInteger;

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

    private final int DEFAULT_NUM_TEST_CASES = 100;
    Random rand = new Random();

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
		initlevel(1);
		
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
		things.add(new Thing(new Rectangle(0,0,400,590), Thing.ThingType.OBSTACLE, testpic));
		things.add(new Thing(new Rectangle(80, 620, 1, 1), Thing.ThingType.FIXEDTOKEN, new Token("function ", TokenType.FUNCTIONSTART)));
		things.add(new Thing(new Rectangle(170, 620, 1, 1), Thing.ThingType.FIXEDTOKEN, new Token("level"+level, TokenType.FUNCTION)));
		things.add(new Thing(new Rectangle(240, 620, 1, 1), Thing.ThingType.FIXEDTOKEN, new Token("(", TokenType.LPAREN)));
		things.add(new Thing(new Rectangle(380, 620, 1, 1), Thing.ThingType.FIXEDTOKEN, new Token(")", TokenType.RPAREN)));
		things.add(new Thing(new Rectangle(400, 620, 1, 1), Thing.ThingType.FIXEDTOKEN, new Token("{", TokenType.LBRACE)));
		
		programtext = "";
				
        p = new Parser();
        pList = new ArrayList<Token>();
        rList = new ArrayList<Token>();
		
		pList.add(new Token("answer", TokenType.VARIABLE));
		pList.add(new Token("=", TokenType.EQ));
		pList.add(new Token(";", TokenType.SEMICOLON));
        
        switch(level)
        {
        case 1:
			things.add(new Thing(new Rectangle(600,200,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(600,400,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(1600,0,200,200), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(2000, 300, 200, 200), Thing.ThingType.END, testpic));

            pList.add(new Token("1", TokenType.INTEGER));
            break;
        
        case 2:
			things.add(new Thing(new Rectangle(600,200,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(600,400,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(1600,0,200,200), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(2000, 300, 200, 200), Thing.ThingType.END, testpic));

            pList.add(new Token("1", TokenType.INTEGER));
            pList.add(new Token("+", TokenType.PLUS));
            break;
            
        case 3:
			things.add(new Thing(new Rectangle(600,200,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(600,400,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(1600,0,200,200), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(2000, 300, 200, 200), Thing.ThingType.END, testpic));

            pList.add(new Token("1", TokenType.INTEGER));
            pList.add(new Token("+", TokenType.PLUS));
            pList.add(new Token("*", TokenType.TIMES));
            pList.add(new Token("(", TokenType.LPAREN));
            pList.add(new Token(")", TokenType.RPAREN));
            break;
            
        case 4:
			things.add(new Thing(new Rectangle(600,200,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(600,400,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(1600,0,200,200), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(2000, 300, 200, 200), Thing.ThingType.END, testpic));

            pList.add(new Token("x", TokenType.VARIABLE));
            break;
            
        case 5:
 			things.add(new Thing(new Rectangle(600,200,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(600,400,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(1600,0,200,200), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(2000, 300, 200, 200), Thing.ThingType.END, testpic));

            pList.add(new Token("x", TokenType.VARIABLE));
            pList.add(new Token("+", TokenType.PLUS));
            break;
        
        case 6:
 			things.add(new Thing(new Rectangle(600,200,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(600,400,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(1600,0,200,200), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(2000, 300, 200, 200), Thing.ThingType.END, testpic));

            pList.add(new Token("x", TokenType.VARIABLE));
            pList.add(new Token("y", TokenType.VARIABLE));
            pList.add(new Token("+", TokenType.PLUS));
            pList.add(new Token("*", TokenType.TIMES));
            break;
            
        case 7:
			things.add(new Thing(new Rectangle(600,200,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(600,400,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(1600,0,200,200), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(2000, 300, 200, 200), Thing.ThingType.END, testpic));

            pList.add(new Token("x", TokenType.VARIABLE));
            pList.add(new Token("y", TokenType.VARIABLE));
            pList.add(new Token("level6", TokenType.FUNCTION));
            pList.add(new Token("(", TokenType.LPAREN));
            pList.add(new Token(")", TokenType.RPAREN));
            break;
            
            
        case 8:
			things.add(new Thing(new Rectangle(600,200,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(600,400,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(1600,0,200,200), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(2000, 300, 200, 200), Thing.ThingType.END, testpic));

            pList.add(new Token("x", TokenType.VARIABLE));
            pList.add(new Token("y", TokenType.VARIABLE));
            pList.add(new Token("(", TokenType.LPAREN));
            pList.add(new Token(")", TokenType.RPAREN));
            pList.add(new Token("{", TokenType.LBRACE));
            pList.add(new Token("}", TokenType.RBRACE));
            pList.add(new Token(">", TokenType.GREATERTHAN));
            pList.add(new Token("if", TokenType.IF));
            pList.add(new Token("else", TokenType.ELSE));
            break;
            
        case 9:
			things.add(new Thing(new Rectangle(600,200,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(600,400,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(1600,0,200,200), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(2000, 300, 200, 200), Thing.ThingType.END, testpic));

            pList.add(new Token("x", TokenType.VARIABLE));
            pList.add(new Token("y", TokenType.VARIABLE));
            pList.add(new Token("z", TokenType.VARIABLE));
            pList.add(new Token("(", TokenType.LPAREN));
            pList.add(new Token(")", TokenType.RPAREN));
            pList.add(new Token("level8", TokenType.FUNCTION));
            pList.add(new Token(",", TokenType.COMMA));
            break;
            
        case 10:
			things.add(new Thing(new Rectangle(600,200,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(600,400,50,50), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(1600,0,200,200), Thing.ThingType.OBSTACLE, testpic));
			things.add(new Thing(new Rectangle(2000, 300, 200, 200), Thing.ThingType.END, testpic));

            pList.add(new Token("x", TokenType.VARIABLE));
            pList.add(new Token("+", TokenType.PLUS));
            pList.add(new Token("(", TokenType.LPAREN));
            pList.add(new Token(")", TokenType.RPAREN));
            pList.add(new Token("{", TokenType.LBRACE));
            pList.add(new Token("}", TokenType.RBRACE));
            pList.add(new Token(">", TokenType.GREATERTHAN));
            pList.add(new Token("if", TokenType.IF));
            pList.add(new Token("else", TokenType.ELSE));
            pList.add(new Token("0", TokenType.INTEGER));
            pList.add(new Token("1", TokenType.INTEGER));
            pList.add(new Token("2", TokenType.INTEGER));
            pList.add(new Token("level10", TokenType.FUNCTION));
            pList.add(new Token("-", TokenType.MINUS));

            break;  
        default: System.out.println("should not get here!!!");
        break;
        }

		myx = 10.0;
		myy = 610.0;
		
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

		Thing t = conflicts(g,new Rectangle((int)newx,(int)newy,gw,gh));
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
			if(!grabToken((Token)t.data)){
				initlevel(currlevel);
				return;
			} else {
				things.remove(t);
			}
			break;
		case END:
			if(!grabToken(new Token("}", TokenType.RBRACE)) || !testProgram()){
				initlevel(currlevel);
			} else {
				initlevel(currlevel+1);
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
	
	Thing conflicts(Graphics g, Rectangle test){
		for(Thing t: things){
			boolean x;
			if(t.type == Thing.ThingType.FIXEDTOKEN){
				Rectangle2D tokenbounds = g.getFontMetrics().getStringBounds(((Token)t.data).content, g);
				Rectangle temp = new Rectangle(test);
				temp.translate(-(int)t.bounds.getX(), -(int)t.bounds.getY());
				x = tokenbounds.intersects(temp);
			} else{
				x = test.intersects(t.bounds);
			}
			if(x) return t;
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
		return test(currlevel);
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


    // stub, note that Doug is gonna write this function out in another file
    BigInteger evaluate(int level, ArrayList<BigInteger> inputs) {
        return BigInteger.ONE; // stub
    }



    boolean test(int level) {
        switch (level) {
            case 1:
            return testLevel1();
            case 2:
            return testLevel2();
            case 3:
            return testLevel3();
            case 4:
            return testLevel4();
            case 5:
            return testLevel5();
            case 6:
            return testLevel6();
            case 7:
            return testLevel7();
            case 8:
            return testLevel8();
            case 9:
            return testLevel9();
            case 10:
            return testLevel10();
            default:
            return false;
        }
    }
    boolean testLevel1() {
        ArrayList<BigInteger> inputs = new ArrayList<BigInteger>();
        return evaluate(1, inputs).equals(BigInteger.ONE);
    }
    boolean testLevel2() {
        ArrayList<BigInteger> inputs = new ArrayList<BigInteger>();
        return evaluate(2, inputs).equals(new BigInteger("2"));
    }
    boolean testLevel3() {
        ArrayList<BigInteger> inputs = new ArrayList<BigInteger>();
        return evaluate(3, inputs).equals(new BigInteger("4"));
    }
    boolean testLevel4() {
        ArrayList<BigInteger> inputs = new ArrayList<BigInteger>();
        for (int i = 0; i < DEFAULT_NUM_TEST_CASES; i++) {
            BigInteger bi = new BigInteger(32, rand);
            bi = bi.subtract(new BigInteger("2147483648")); // 2^31
            inputs.add(bi);
            boolean passed = (evaluate(4, inputs).equals(computeAnswerLevel4(inputs)));
            if (!passed) {
                return false;
            }
        }
        return true;
    }
    boolean testLevel5() {
        ArrayList<BigInteger> inputs = new ArrayList<BigInteger>();
        for (int i = 0; i < DEFAULT_NUM_TEST_CASES; i++) {
            BigInteger bi = new BigInteger(32, rand);
            bi = bi.subtract(new BigInteger("2147483648")); // 2^31
            inputs.add(bi);
            boolean passed = (evaluate(5, inputs).equals(computeAnswerLevel5(inputs)));
            if (!passed) {
                return false;
            }
        }
        return true;
    }
    boolean testLevel6() {
        ArrayList<BigInteger> inputs = new ArrayList<BigInteger>();
        for (int i = 0; i < DEFAULT_NUM_TEST_CASES; i++) {
            BigInteger bi = new BigInteger(32, rand);
            bi = bi.subtract(new BigInteger("2147483648")); // 2^31
            inputs.add(bi);
            boolean passed = (evaluate(6, inputs).equals(computeAnswerLevel6(inputs)));
            if (!passed) {
                return false;
            }
        }
        return true;
    }
    boolean testLevel7() {
        ArrayList<BigInteger> inputs = new ArrayList<BigInteger>();
        for (int i = 0; i < DEFAULT_NUM_TEST_CASES; i++) {
            BigInteger bi = new BigInteger(32, rand);
            bi = bi.subtract(new BigInteger("2147483648")); // 2^31
            inputs.add(bi);
            boolean passed = (evaluate(7, inputs).equals(computeAnswerLevel7(inputs)));
            if (!passed) {
                return false;
            }
        }
        return true;
    }
    boolean testLevel8() {
        ArrayList<BigInteger> inputs = new ArrayList<BigInteger>();
        for (int i = 0; i < DEFAULT_NUM_TEST_CASES; i++) {
            BigInteger bi = new BigInteger(32, rand);
            bi = bi.subtract(new BigInteger("2147483648")); // 2^31
            inputs.add(bi);
            BigInteger bi2 = new BigInteger(32, rand);
            bi2 = bi2.subtract(new BigInteger("2147483648")); // 2^31
            inputs.add(bi2);
            boolean passed = (evaluate(8, inputs).equals(computeAnswerLevel8(inputs)));
            if (!passed) {
                return false;
            }
        }
        return true;
    }
    boolean testLevel9() {
        ArrayList<BigInteger> inputs = new ArrayList<BigInteger>();
        for (int i = 0; i < DEFAULT_NUM_TEST_CASES; i++) {
            BigInteger bi = new BigInteger(32, rand);
            bi = bi.subtract(new BigInteger("2147483648")); // 2^31
            inputs.add(bi);
            BigInteger bi2 = new BigInteger(32, rand);
            bi2 = bi2.subtract(new BigInteger("2147483648")); // 2^31
            inputs.add(bi2);
            boolean passed = (evaluate(9, inputs).equals(computeAnswerLevel9(inputs)));
            if (!passed) {
                return false;
            }
        }
        return true;
    }
    boolean testLevel10() {
        ArrayList<BigInteger> inputs = new ArrayList<BigInteger>();
        for (int i = 0; i < 10; i++) {
            BigInteger bi = new BigInteger("" + i);
            inputs.add(bi);
            boolean passed = (evaluate(10, inputs).equals(computeAnswerLevel10(inputs)));
            if (!passed) {
                return false;
            }
        }
        return true;
    }



    BigInteger computeAnswerLevel1(ArrayList<BigInteger> inputs) {
        return BigInteger.ONE;
    }
    BigInteger computeAnswerLevel2(ArrayList<BigInteger> inputs) {
        return new BigInteger("2");
    }
    BigInteger computeAnswerLevel3(ArrayList<BigInteger> inputs) {
        return new BigInteger("4");
    }
    BigInteger computeAnswerLevel4(ArrayList<BigInteger> inputs) {
        return inputs.get(0);
    }
    BigInteger computeAnswerLevel5(ArrayList<BigInteger> inputs) {
        return inputs.get(0).multiply(new BigInteger("2"));
    }
    BigInteger computeAnswerLevel6(ArrayList<BigInteger> inputs) {
        BigInteger y = inputs.get(0).multiply(inputs.get(0));
        return y.multiply(y).add(y);
    }
    BigInteger computeAnswerLevel7(ArrayList<BigInteger> inputs) {
        BigInteger y = computeAnswerLevel6(inputs);
        ArrayList<BigInteger> i2 = new ArrayList<BigInteger>();
        i2.add(y);
        return computeAnswerLevel6(i2);
    }
    BigInteger computeAnswerLevel8(ArrayList<BigInteger> inputs) {
        BigInteger x = inputs.get(0);
        BigInteger y = inputs.get(1);
        int cto = x.compareTo(y);
        if (cto > 0) {
            return x;
        } else {
            return y;
        }
    }
    BigInteger computeAnswerLevel9(ArrayList<BigInteger> inputs) {
        ArrayList<BigInteger> i2 = new ArrayList<BigInteger>();


        ArrayList<BigInteger> im1 = new ArrayList<BigInteger>();
        im1.add(inputs.get(0));
        im1.add(inputs.get(1));


        i2.add(computeAnswerLevel8(im1));
        i2.add(inputs.get(2));
        return computeAnswerLevel8(i2);
    }
    BigInteger computeAnswerLevel10(ArrayList<BigInteger> inputs) {
        ArrayList<BigInteger> im1 = new ArrayList<BigInteger>();
        im1.add(inputs.get(0).subtract(BigInteger.ONE));
        ArrayList<BigInteger> im2 = new ArrayList<BigInteger>();
        im2.add(inputs.get(0).subtract(new BigInteger("2")));
        BigInteger x = inputs.get(0);
        int cto = x.compareTo(BigInteger.ZERO);
        if (cto > 0) {
            BigInteger r1 = computeAnswerLevel10(im1);
            BigInteger r2 = computeAnswerLevel10(im2);
            return r1.add(r2);
        } else {
            return BigInteger.ONE;
        }
    }





}
