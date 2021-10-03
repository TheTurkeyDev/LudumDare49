package dev.theturkey.ld49.defragmg;

import javax.swing.*;
import java.awt.*;

public class CollectionArea extends JLabel
{

	public CollectionArea()
	{
		super("", SwingConstants.CENTER);
		this.setBackground(Color.GRAY);
		this.setVisible(true);
		this.setOpaque(true);

	}

	public boolean acceptsColor(Color color)
	{
		switch(this.getText().toLowerCase())
		{
			case "red":
				return color.equals(Color.RED);
			case "blue":
				return color.equals(Color.BLUE);
			case "cyan":
				return color.equals(Color.CYAN);
			case "black":
				return color.equals(Color.BLACK);
			case "yellow":
				return color.equals(Color.YELLOW);
			case "magenta":
				return color.equals(Color.MAGENTA);
		}
		return false;
	}
}
