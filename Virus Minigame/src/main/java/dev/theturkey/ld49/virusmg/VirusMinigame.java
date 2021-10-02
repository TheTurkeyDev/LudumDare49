package dev.theturkey.ld49.virusmg;

import dev.theturkey.ld49.Core;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VirusMinigame extends JFrame
{
	public static final int TPS = 10;
	private static final int NUM_FOLDERS = 32;
	private static final int GAME_DURATION = 180 * TPS;

	private GameState state = GameState.STARTING;

	private final JLabel initialSplash;

	private final PlayerLabel player;
	private final List<FolderLabel> folderLabels = new ArrayList<>();
	private final List<VirusLabel> virusLabels = new ArrayList<>();
	private final List<PowerUpLabel> powerUpLabels = new ArrayList<>();
	private final JProgressBar progressBar;
	private final JLabel timerLabel;
	private final JLabel infectedCountLabel;
	private final JLabel nonInfectedCountLabel;


	private final List<JLabel> gameOverLabels = new ArrayList<>();

	private final String password;
	private int timeLeft = 0;

	private boolean won = false;

	public VirusMinigame(String password)
	{
		this.password = password;
		this.setAlwaysOnTop(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLocationRelativeTo(null);

		// Apply a transparent color to the background
		// This is REALLY important, without this, it won't work!
		setBackground(new Color(0, 255, 0, 0));
		getContentPane().setBackground(Color.BLACK);
		this.setLayout(null);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.setSize(Math.min(screenSize.width, 1920), Math.min(screenSize.height, 1080));

		this.initialSplash = new JLabel(new ImageIcon(getImage("/virus_splash.png")));
		this.initialSplash.setBounds(screenSize.width / 2 - 300, screenSize.height / 2 - 300, 600, 600);
		initialSplash.setVisible(false);
		add(this.initialSplash);

		this.player = new PlayerLabel(getImage("/player_cursor.png"));
		this.player.setVisible(false);
		add(player);

		for(int i = 0; i < 4; i++)
		{
			VirusLabel virusLabel = new VirusLabel(getImage("/virus_" + (i + 1) + ".png"));
			add(virusLabel);
			virusLabel.setVisible(false);
			virusLabels.add(virusLabel);
		}

		for(int i = 0; i < NUM_FOLDERS; i++)
		{
			int x = ((i % 8) + 1) * (screenSize.width / 10);
			int y = ((i / 8) + 1) * (screenSize.height / 6);
			FolderLabel folderLabel = new FolderLabel(getImage("/folder.png"), getImage("/folder_infected.png"), x, y);
			add(folderLabel);
			folderLabel.setVisible(false);
			folderLabels.add(folderLabel);
		}

		PowerUpLabel cancelPowerUpLabel = new PowerUpLabel(PowerUpLabel.PowerUpType.CANCEL, getImage("/cancel.png"), 0, 0);
		add(cancelPowerUpLabel);
		cancelPowerUpLabel.setVisible(false);
		powerUpLabels.add(cancelPowerUpLabel);

		PowerUpLabel frozenPowerUpLabel = new PowerUpLabel(PowerUpLabel.PowerUpType.FROZEN, getImage("/frozen.png"), 0, 0);
		add(frozenPowerUpLabel);
		frozenPowerUpLabel.setVisible(false);
		powerUpLabels.add(frozenPowerUpLabel);

		setVisible(true);

		timerLabel = new JLabel("Time Left: 180");
		timerLabel.setBounds(screenSize.width - 250, 0, 300, 50);
		Font f = new Font(timerLabel.getFont().getName(), Font.BOLD, 28);
		timerLabel.setFont(f);
		timerLabel.setForeground(Color.GREEN);
		timerLabel.setBackground(Color.DARK_GRAY);
		timerLabel.setVisible(false);
		add(timerLabel);

		infectedCountLabel = new JLabel("Infected Folders: 0");
		infectedCountLabel.setBounds(screenSize.width / 2 - 115, 10, 300, 50);
		infectedCountLabel.setFont(f);
		infectedCountLabel.setForeground(Color.RED);
		infectedCountLabel.setVisible(false);
		add(infectedCountLabel);

		nonInfectedCountLabel = new JLabel("Non-Infected Folders: 0");
		nonInfectedCountLabel.setBounds(screenSize.width / 2 - 150, 60, 500, 50);
		nonInfectedCountLabel.setFont(f);
		nonInfectedCountLabel.setForeground(Color.GREEN);
		nonInfectedCountLabel.setVisible(false);
		add(nonInfectedCountLabel);

		for(int i = 0; i < 4; i++)
		{
			JLabel gameOverLabel = new JLabel("GAMEOVER!");
			gameOverLabel.setBounds((screenSize.width / 2) - 200, ((screenSize.height / 2) - 200) + (i * 50), 400, 200);
			gameOverLabel.setFont(f);
			gameOverLabel.setForeground(Color.GREEN);
			gameOverLabel.setVisible(false);
			add(gameOverLabel);
			gameOverLabels.add(gameOverLabel);
		}

		progressBar = new JProgressBar(0, 100);
		progressBar.setBackground(Color.DARK_GRAY);
		progressBar.setForeground(Color.GREEN);
		progressBar.setValue(0);
		progressBar.setStringPainted(false);
		progressBar.setVisible(false);
		add(progressBar);

		Timer timer = new Timer(1000 / TPS, e ->
		{
			timeLeft -= 1;

			if(state == GameState.IN_GAME)
			{
				for(VirusLabel virusLabel : virusLabels)
					virusLabel.tick(this);

				this.player.tick(this);

				double chance = Math.random() * 1000;

				if(chance < 25)
				{
					List<PowerUpLabel> toChoose = powerUpLabels.stream().filter(p -> !p.isVisible()).collect(Collectors.toList());

					if(toChoose.size() != 0)
					{
						PowerUpLabel toDrop = toChoose.get((int) (Math.random() * toChoose.size()));
						int xLoc = (int) ((Math.random() * (screenSize.width - 200)) + 100);
						int yLoc = (int) ((Math.random() * (screenSize.height - 100)) + 50);
						toDrop.setLocation(xLoc, yLoc);
						toDrop.setVisible(true);
					}
				}

				int infected = (int) folderLabels.stream().filter(FolderLabel::isInfected).count();
				infectedCountLabel.setText("Infected Folders: " + infected);
				nonInfectedCountLabel.setText("Non-Infected Folders: " + (NUM_FOLDERS - infected));
				timerLabel.setText("Time Left: " + timeLeft / TPS);

				if(timeLeft <= 0)
				{
					changeGameState(GameState.GAME_OVER);
				}
			}
		});
		timer.setInitialDelay(0);
		timer.start();

		this.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent keyEvent)
			{

			}

			@Override
			public void keyPressed(KeyEvent keyEvent)
			{
				if(VirusMinigame.this.state == GameState.STARTING && keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
					VirusMinigame.this.changeGameState(GameState.IN_GAME);
				if(VirusMinigame.this.state == GameState.IN_GAME)
					VirusMinigame.this.player.onKeyStateChange(keyEvent.getKeyCode(), true);
				if(VirusMinigame.this.state == GameState.GAME_OVER && keyEvent.getKeyCode() == KeyEvent.VK_ENTER)
				{
					if(won)
					{
						Core.endMiniGame();
						VirusMinigame.this.dispatchEvent(new WindowEvent(VirusMinigame.this, WindowEvent.WINDOW_CLOSING));
					}
					else
					{
						VirusMinigame.this.changeGameState(GameState.STARTING);
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent keyEvent)
			{
				if(VirusMinigame.this.state == GameState.IN_GAME)
					VirusMinigame.this.player.onKeyStateChange(keyEvent.getKeyCode(), false);
			}
		});

		changeGameState(GameState.STARTING);
	}

	private BufferedImage getImage(String path)
	{
		try
		{
			URL url = getClass().getResource(path);
			if(url == null)
				return null;

			return ImageIO.read(url);
		} catch(Exception e)
		{
			return null;
		}
	}

	private void changeGameState(GameState newState)
	{
		switch(this.state)
		{
			case STARTING:
				this.initialSplash.setVisible(false);
				break;
			case IN_GAME:
				for(FolderLabel folderLabel : folderLabels)
					folderLabel.setVisible(false);
				for(VirusLabel virusLabel : virusLabels)
					virusLabel.setVisible(false);
				for(PowerUpLabel powerUpLabel : powerUpLabels)
					powerUpLabel.setVisible(false);
				this.player.setVisible(false);
				this.progressBar.setVisible(false);
				this.timerLabel.setVisible(false);
				this.infectedCountLabel.setVisible(false);
				this.nonInfectedCountLabel.setVisible(false);
				this.setAutoRequestFocus(true);
				break;
			case GAME_OVER:
				for(JLabel label : gameOverLabels)
					label.setVisible(false);
				break;
		}

		this.state = newState;

		switch(this.state)
		{
			case STARTING:
				this.initialSplash.setVisible(true);
				break;
			case IN_GAME:
				for(FolderLabel folderLabel : folderLabels)
					folderLabel.setVisible(true);
				for(VirusLabel virusLabel : virusLabels)
					virusLabel.setVisible(true);
				for(PowerUpLabel powerUpLabel : powerUpLabels)
					powerUpLabel.setVisible(false);
				this.player.setVisible(true);
				this.progressBar.setVisible(true);
				this.timerLabel.setVisible(true);
				this.infectedCountLabel.setVisible(true);
				this.nonInfectedCountLabel.setVisible(true);
				this.timeLeft = GAME_DURATION;
				break;
			case GAME_OVER:
				int infected = (int) folderLabels.stream().filter(FolderLabel::isInfected).count();

				if(infected >= 16)
				{
					won = false;
					this.gameOverLabels.get(0).setText("GAME OVER!");
					this.gameOverLabels.get(1).setText("PRESS ENTER TO RETRY");
					this.gameOverLabels.get(2).setText("");
					this.gameOverLabels.get(3).setText("");
				}
				else
				{
					won = true;
					this.gameOverLabels.get(0).setText("GAME OVER!");
					this.gameOverLabels.get(1).setText("THE PASSWORD IS:");
					this.gameOverLabels.get(2).setText(password);
					this.gameOverLabels.get(3).setText("PRESS ENTER TO CLOSE");
				}

				for(JLabel label : gameOverLabels)
					label.setVisible(true);
				break;
		}
	}

	public FolderLabel getRandomNonInfectedFolder()
	{
		List<FolderLabel> folders = folderLabels.stream().filter(f -> !f.isInfected()).collect(Collectors.toList());
		if(folders.size() == 0)
			return null;

		return folders.get((int) (Math.random() * folders.size()));
	}

	public FolderLabel getClosestFolderWithin(int x, int y, int range)
	{
		double lowDist = Integer.MAX_VALUE;
		FolderLabel closest = null;

		for(FolderLabel folderLabel : folderLabels)
		{
			double distance = Math.sqrt(Math.pow(folderLabel.getX() - x, 2) + Math.pow(folderLabel.getY() - y, 2));
			if(distance < lowDist)
			{
				closest = folderLabel;
				lowDist = distance;
			}
		}

		if(lowDist <= range)
			return closest;
		return null;
	}

	public PowerUpLabel getClosestPowerUpWithin(int x, int y, int range)
	{
		double lowDist = Integer.MAX_VALUE;
		PowerUpLabel closest = null;

		for(PowerUpLabel powerUpLabel : powerUpLabels)
		{
			double distance = Math.sqrt(Math.pow(powerUpLabel.getX() - x, 2) + Math.pow(powerUpLabel.getY() - y, 2));
			if(distance < lowDist)
			{
				closest = powerUpLabel;
				lowDist = distance;
			}
		}

		if(lowDist <= range)
			return closest;
		return null;
	}

	public void updateProgress(FolderLabel targetFolder, int progress)
	{
		Dimension size = progressBar.getPreferredSize();
		progressBar.setBounds(targetFolder.getX() - 50, targetFolder.getY() - 25, size.width, size.height);
		progressBar.setValue(progress);
		progressBar.setVisible(progress != 0);
	}

	public void cancelInfections()
	{
		for(VirusLabel virusLabel : virusLabels)
			virusLabel.cancelInfection();
	}

	public void freezeVirus()
	{
		for(VirusLabel virusLabel : virusLabels)
			virusLabel.freeze();
	}

	private enum GameState
	{
		STARTING,
		IN_GAME,
		GAME_OVER
	}
}
