package dev.theturkey.ld49.defragmg;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Goose extends JPanel
{
	private static final int MAX_SPEED = 3;
	private static final int GRAB_RANGE = 48;

	private Robot robot;
	private final BufferedImage image;

	private boolean grabbed = false;
	private int grabTime = 0;
	private final double[] moveVel = new double[2];
	private double angle;

	public Goose(BufferedImage image)
	{
		this.image = image;
		setBackground(new Color(0, 255, 0, 0));
		Dimension size = this.getPreferredSize();
		setBounds(0, 0, size.width, size.height);
		try
		{
			robot = new Robot();
		} catch(AWTException e)
		{
			e.printStackTrace();
		}
	}

	public void tick(DefragMiniGame game)
	{
		Point p = MouseInfo.getPointerInfo().getLocation();
		double yDiff = p.getY() - (getY() + 32);
		double xDiff = p.getX() - (getX() + 32);
		double dist = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

		if(!grabbed)
		{
			if(dist < GRAB_RANGE && grabTime <= 0)
			{
				grabMouse();
				return;
			}
			angle = Math.atan2(yDiff, xDiff);
			double changeX = Math.cos(angle) * MAX_SPEED;
			double changeY = Math.sin(angle) * MAX_SPEED;
			this.setLocation((int) (this.getX() + changeX), (int) (this.getY() + changeY));
		}
		else
		{
			this.setLocation((int) (this.getX() + moveVel[0]), (int) (this.getY() + moveVel[1]));
			int xAdj = (int) ((Math.cos(angle) * 32) + 32);
			int yAdj = (int) ((Math.sin(angle) * 32) + 32);
			robot.mouseMove(this.getX() + xAdj, this.getY() + yAdj);

			if(grabTime < 300)
				grabbed = false;
		}

		if(grabTime > 0)
			grabTime--;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(image.getWidth(), image.getHeight());
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(angle + (Math.PI / 2), image.getWidth() / 2d, image.getHeight() / 2d);
		g2.drawImage(image, 0, 0, null);
	}

	public void grabMouse()
	{
		grabbed = true;
		grabTime = 500;

		angle = (Math.random() * (Math.PI * 4)) - Math.PI * 2;
		moveVel[0] = Math.cos(angle) * (MAX_SPEED * 1.5);
		moveVel[1] = Math.sin(angle) * (MAX_SPEED  * 1.5);
	}
}
