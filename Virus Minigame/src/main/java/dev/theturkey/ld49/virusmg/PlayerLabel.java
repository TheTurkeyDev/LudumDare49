package dev.theturkey.ld49.virusmg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class PlayerLabel extends JLabel
{
	private static final int MAX_SPEED = 10;
	private static final int UNINFECT_TIME = 4 * VirusMinigame.TPS;

	private final boolean[] keyInputs = new boolean[5];
	private int uninfectingDuration = 0;

	public PlayerLabel(BufferedImage image)
	{
		super(new ImageIcon(image));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = this.getPreferredSize();
		setBounds(screenSize.width / 2, screenSize.height / 2, size.width, size.height);
	}

	public void tick(VirusMinigame game)
	{
		if(keyInputs[4])
		{
			FolderLabel closest = game.getClosestFolderWithin(this.getX() - 16, this.getY() - 32, 32);
			if(closest != null && closest.isInfected())
			{
				uninfectingDuration++;
				if(uninfectingDuration >= UNINFECT_TIME)
				{
					closest.setInfected(false);
					uninfectingDuration = 0;
				}

				game.updateProgress(closest, (int) ((uninfectingDuration / (float) UNINFECT_TIME) * 100));
			}
			else
			{
				uninfectingDuration = 0;
			}
		}
		else
		{
			uninfectingDuration = 0;
		}

		int xChange = (keyInputs[3] ? 1 : 0) - (keyInputs[1] ? 1 : 0);
		int yChange = (keyInputs[2] ? 1 : 0) - (keyInputs[0] ? 1 : 0);
		if(xChange == 0 && yChange == 0)
			return;

		double angle = Math.atan2(yChange, xChange);
		double changeX = Math.cos(angle) * MAX_SPEED;
		double changeY = Math.sin(angle) * MAX_SPEED;
		this.setLocation((int) (this.getX() + changeX), (int) (this.getY() + changeY));

		PowerUpLabel closestPowerUp = game.getClosestPowerUpWithin(this.getX() + 8, this.getY() - 32, 50);
		if(closestPowerUp != null)
			closestPowerUp.onPickup(game);
	}

	public void onKeyStateChange(int key, boolean pressed)
	{
		switch(key)
		{
			case KeyEvent.VK_W:
				this.keyInputs[0] = pressed;
				break;
			case KeyEvent.VK_A:
				this.keyInputs[1] = pressed;
				break;
			case KeyEvent.VK_S:
				this.keyInputs[2] = pressed;
				break;
			case KeyEvent.VK_D:
				this.keyInputs[3] = pressed;
				break;
			case KeyEvent.VK_SPACE:
				this.keyInputs[4] = pressed;
				break;
		}
	}
}
