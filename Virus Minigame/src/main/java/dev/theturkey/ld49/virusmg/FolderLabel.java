package dev.theturkey.ld49.virusmg;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FolderLabel extends JLabel
{
	private ImageIcon normalImage, infectedImage;
	private boolean infected = false;

	public FolderLabel(BufferedImage normalImage, BufferedImage infectedImage, int x, int y)
	{
		super(new ImageIcon(normalImage));
		this.normalImage = new ImageIcon(normalImage);
		this.infectedImage = new ImageIcon(infectedImage);
		Dimension size = this.getPreferredSize();
		setBounds(x, y, size.width, size.height);
	}

	public boolean isInfected()
	{
		return this.infected;
	}

	public void setInfected(boolean infected)
	{
		this.infected = infected;
		this.setIcon(this.infected ? infectedImage : normalImage);
	}
}
