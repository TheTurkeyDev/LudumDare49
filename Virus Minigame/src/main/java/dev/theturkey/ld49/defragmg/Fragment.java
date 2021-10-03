package dev.theturkey.ld49.defragmg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Fragment extends JLabel
{
	public Fragment(DefragMainPanel panel, Color color)
	{
		int x = (int) (Math.random() * (panel.getWidth() - 200)) + 75;
		int y = (int) (Math.random() * (panel.getHeight() - 200)) + 75;
		int width = (int) (Math.random() * 150) + 50;
		int height = (int) (Math.random() * 150) + 50;
		this.setBounds(x, y, width, height);
		this.setBackground(color);
		this.setOpaque(true);
		this.setVisible(true);
		this.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				panel.pressed(Fragment.this);
			}

			public void mouseReleased(MouseEvent e)
			{
				panel.released();
			}
		});
	}

	public void reset(DefragMainPanel panel)
	{
		int x = (int) (Math.random() * panel.getWidth());
		int y = (int) (Math.random() * (panel.getHeight() - 200)) + 75;
		this.setLocation(x, y);
	}
}
