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
	
	long lastpaint = System.currentTimeMillis();
    double speed_per_ms = .25;
	double token_speed_per_ms = .1;

	String programtext="";
    
    int dir[] = {0,0,0,0};

    Parser p = new Parser();
    boolean gameOver = false;
    
    
    public Layered()
    {
		pList.add(new Token("answer", TokenType.VARIABLE, null));
		pList.add(new Token("=", TokenType.EQ, null));
		pList.add(new Token("1", TokenType.INTEGER, null));
		pList.add(new Token("+", TokenType.PLUS, null));
		pList.add(new Token(";", TokenType.SEMICOLON, null));
		
        setLayout(new BorderLayout());
        gw = 50;
        gh = 30;
        rw = 50;
        rh = 20;
        setBounds(0, 0, WIDTH, HEIGHT);
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
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
		
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);

		long time = System.currentTimeMillis();
		long diff = time-lastpaint;
		lastpaint = time;
		
		
		int newx = point1.x + (int)(speed_per_ms*diff*(dir[1]-dir[0]));
		int newy = point1.y + (int)(speed_per_ms*diff*(dir[3]-dir[2]));
		if (0 <= newx && newx <= WIDTH-70){
			point1.x = newx;
		}
		if (0 <= newy && newy <= HEIGHT-90){
			point1.y = newy;
		}

        g2.fillOval(point1.x, point1.y, gw, gh);
        
        int xPoly[] = {point1.x - 30, point1.x - 30, point1.x + 3};
        int yPoly[] = {point1.y - 5, point1.y + 30, point1.y + gh/2};

        tail = new Polygon(xPoly, yPoly, xPoly.length);
        
        g2.fill(tail);
        
		
        for (Iterator<Token> i = rList.iterator(); i.hasNext();){
			Token token = i.next();
            // set a rectangle red or green depending on if it was hit

            Rectangle rect = token.rect;
            rect.x -= (int)(token_speed_per_ms*diff);
			if(rect.x < 0){
				i.remove();
				continue;
			}
			if(((rect.x >= point1.x) && (rect.x <= (point1.x + gw))) && ((rect.y >= point1.y) && (rect.y <= (point1.y + gh)))){
				token.hit = true;
			}

            if (token.hit) {
                // if it was hit, see if the new token would produce a syntax error
				if(token.type == TokenType.RBRACE) programtext += "\n";
				programtext += token.content;
				if(token.type == TokenType.RBRACE || token.type == TokenType.LBRACE || token.type == TokenType.SEMICOLON) programtext += "\n";

                boolean valid = p.addAndEvaluate(token);
                if (!valid) {
                    gameOver = true;
                }
                // remove the rectangle from the list
                i.remove();
            } else {
				// trick from http://stackoverflow.com/questions/7679459/thick-border-of-drawn-string
                //g2.setColor(Color.red);
                //g2.fill(token.rect);
                g2.setColor(Color.black);
                g2.setFont(font);
                g2.drawString(token.content, token.rect.x-1, token.rect.y + rh-1);
                g2.drawString(token.content, token.rect.x-1, token.rect.y + rh+1);
                g2.drawString(token.content, token.rect.x+1, token.rect.y + rh-1);
                g2.drawString(token.content, token.rect.x+1, token.rect.y + rh+1);
                g2.setColor(Color.white);
                g2.drawString(token.content, token.rect.x, token.rect.y + rh);
            }
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
            rList.add(new Token(t.content, t.type, new Rectangle(WIDTH,(int)(Math.random()*(HEIGHT-rh)), rw, rh)));
        }
    }
}
