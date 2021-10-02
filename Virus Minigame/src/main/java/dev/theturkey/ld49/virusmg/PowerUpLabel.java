package dev.theturkey.ld49.virusmg;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUpLabel extends JLabel
{
	private PowerUpType type;

	public PowerUpLabel(PowerUpType type, BufferedImage normalImage, int x, int y)
	{
		super(new ImageIcon(normalImage));
		this.type = type;
		Dimension size = this.getPreferredSize();
		setBounds(x, y, size.width, size.height);
	}

	public void onPickup(VirusMinigame game)
	{
		if(this.type == PowerUpType.CANCEL)
		{
			game.cancelInfections();
		}
		else if(this.type == PowerUpType.FROZEN)
		{
			game.freezeVirus();
		}
		this.setVisible(false);
	}

	public enum PowerUpType
	{
		CANCEL,
		FROZEN
	}
}
