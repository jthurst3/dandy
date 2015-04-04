import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


public class driver {
	public static void main (String [] args)
	{
		JFrame theframe = new JFrame();
		theframe.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		theframe.getContentPane().add(new Layered());
		theframe.setSize(1350, 670);
		theframe.setVisible(true);
	}

}
