package dev.theturkey.ld49.virusmg;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VirusLabel extends JLabel
{
	private static final int MAX_SPEED = 8;
	private static final int INFECT_TIME = 5 * VirusMinigame.TPS;
	private static final int FROZEN_TIME = 10 * VirusMinigame.TPS;

	private VirusState state = VirusState.IDLE;
	private FolderLabel folderTarget;
	private int infectingDuration = 0;
	private int frozenDuration = 0;

	public VirusLabel(BufferedImage image)
	{
		super(new ImageIcon(image));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = this.getPreferredSize();
		setBounds(screenSize.width / 2, screenSize.height / 2, size.width, size.height);
	}

	public void tick(VirusMinigame game)
	{
		switch(state)
		{
			case IDLE:
				folderTarget = game.getRandomNonInfectedFolder();

				if(folderTarget != null)
					state = VirusState.MOVING;
				break;
			case MOVING:
				if(folderTarget == null)
				{
					state = VirusState.IDLE;
					return;
				}

				double distance = Math.sqrt(Math.pow(folderTarget.getX() - this.getX(), 2) + Math.pow(folderTarget.getY() - this.getY(), 2));
				if(distance < MAX_SPEED)
				{
					this.setLocation(folderTarget.getX(), folderTarget.getY());
					this.state = VirusState.INFECTING;
				}
				else
				{
					double angle = Math.atan2(folderTarget.getY() - this.getY(), folderTarget.getX() - this.getX());
					double changeX = Math.cos(angle) * MAX_SPEED;
					double changeY = Math.sin(angle) * MAX_SPEED;
					this.setLocation((int) (this.getX() + changeX), (int) (this.getY() + changeY));
				}
				break;
			case INFECTING:
				infectingDuration++;
				if(infectingDuration >= INFECT_TIME)
				{
					infectingDuration = 0;
					state = VirusState.IDLE;
					folderTarget.setInfected(true);
				}
				break;
			case FROZEN:
				frozenDuration++;
				if(frozenDuration >= FROZEN_TIME)
				{
					this.state = infectingDuration > 0 ? VirusState.INFECTING : VirusState.MOVING;
					this.frozenDuration = 0;
				}
				break;
		}
	}

	public void cancelInfection()
	{
		infectingDuration = 0;
		this.folderTarget = null;
		if(this.state == VirusState.INFECTING)
			state = VirusState.IDLE;
	}

	public void freeze()
	{
		state = VirusState.FROZEN;
	}

	private enum VirusState
	{
		IDLE,
		MOVING,
		FROZEN,
		INFECTING
	}
}
