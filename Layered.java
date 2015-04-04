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
    private final int SPEED = 5;
    private final int DELAY = 20;
    private final int DELAY2 = 1000;
    int gw, gh;
    int rw, rh;
    boolean move = false;
    Point point1 = new Point(100, 500);
    private Timer timer,timer2;
    ArrayList<Token> rList = new ArrayList<Token>();
    Polygon tail = new Polygon();
    Font font = new Font("TimesRoman", Font.PLAIN, 24);

    
    private int[] dir = {0,0};


    Parser p = new Parser();
    boolean gameOver = false;
    
    
    public Layered()
    {
        gw = 50;
        gh = 30;
        rw = 50;
        rh = 20;
        setLayout(null);
        setBounds(0, 0, WIDTH, HEIGHT);
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
        
        
        for (Token token : rList){
            if (token.removed) continue;
            // set a rectangle red or green depending on if it was hit
            if (token.hit) {
                // if it was hit, see if the new token would produce a syntax error
                boolean valid = p.addAndEvaluate(token);
                if (!valid) {
                    gameOver = true;
                }
                token.removed = true;
                // remove the rectangle from the list
                // rList.remove(token);
            } else {
                g2.setColor(Color.red);
                token.hit = false;
                g2.fill(token.rect);
                g2.setColor(Color.black);
                g2.setFont(font);
                g2.drawString(token.content, token.rect.x, token.rect.y + rh);
            }

        }

        drawCurrentProgram(g2);


        if (gameOver) {
            g2.drawString("GAME OVER", 500, 500);
        }
    }

    // draws the text of the user's current program
    public void drawCurrentProgram(Graphics2D g2) {
        g2.setColor(Color.black);
        // http://stackoverflow.com/questions/18249592/how-to-change-font-size-in-drawstring-java
        g2.setFont(font);
        g2.drawString(p.currentProgramString, 100, 100);
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
//          rList.add(new Rectangle(WIDTH,(int)(Math.random()*HEIGHT),10,20));
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
            rList.add(p.getNewRandomToken(WIDTH, HEIGHT, rw, rh));
        }
    }

}



