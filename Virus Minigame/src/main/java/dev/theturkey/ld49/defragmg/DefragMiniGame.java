package dev.theturkey.ld49.defragmg;

import dev.theturkey.ld49.Core;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class DefragMiniGame extends JFrame
{
	public static final int TPS = 30;
	private static final int GAME_DURATION = 180 * TPS;
	private static final int NUM_CORRECT_NEEDED = 100;

	private GameState state = GameState.START;
	private int timeLeft = GAME_DURATION;

	private final StartPanel startPanel;
	private final DefragMainPanel gamePanel;
	private final EndPanel endPanel;

	private final Goose goose;
	private final JProgressBar progressBar;
	private final JLabel timerLabel;

	public DefragMiniGame()
	{
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

		goose = new Goose(getImage("/goose.png"));
		goose.setVisible(false);
		add(goose);

		startPanel = new StartPanel(this);
		this.add(startPanel);
		startPanel.setVisible(true);

		endPanel = new EndPanel(this);
		this.add(endPanel);
		endPanel.setVisible(false);

		gamePanel = new DefragMainPanel(this);
		this.add(gamePanel);
		gamePanel.setVisible(false);

		progressBar = new JProgressBar(0, NUM_CORRECT_NEEDED);
		progressBar.setBackground(Color.DARK_GRAY);
		progressBar.setForeground(Color.GREEN);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setString("Progress");
		progressBar.setVisible(true);
		int innerWidth = 800;
		int innerHeight = 600;
		int left = (screenSize.width / 2) - (innerWidth / 2);
		int top = (screenSize.height / 2) - (innerHeight / 2);
		progressBar.setBounds(left, top - 50, innerWidth, 50);
		add(progressBar);

		timerLabel = new JLabel("Time Left: " + (timeLeft / TPS));
		timerLabel.setBounds(left + innerWidth - 200, top - 100, 200, 50);
		Font f = new Font(timerLabel.getFont().getName(), Font.BOLD, 28);
		timerLabel.setFont(f);
		timerLabel.setForeground(Color.GREEN);
		timerLabel.setVisible(false);
		add(timerLabel);

		this.setVisible(true);

		Timer timer = new Timer(1000 / TPS, e ->
		{
			if(state == GameState.IN_GAME)
			{
				if(this.progressBar.getValue() >= this.progressBar.getMaximum())
				{
					Core.setHddDefragged(true);
					this.setGameState(GameState.END);
				}
				else if(timeLeft <= 0)
				{
					this.setGameState(GameState.END);
				}
				timeLeft--;
				gamePanel.tick();
				goose.tick(this);
				timerLabel.setText("Time Left: " + timeLeft / TPS);
			}
		});
		timer.setInitialDelay(0);
		timer.start();

		setGameState(GameState.START);
	}

	public void incProgress()
	{
		this.progressBar.setValue(this.progressBar.getValue() + 1);
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

	public void setGameState(GameState newState)
	{
		switch(state)
		{
			case START:
				startPanel.setVisible(false);
				break;
			case IN_GAME:
				gamePanel.setVisible(false);
				goose.setVisible(false);
				timerLabel.setVisible(false);
				break;
			case END:
				endPanel.setVisible(false);
				break;
		}

		this.state = newState;

		switch(state)
		{
			case START:
				startPanel.setVisible(true);
				break;
			case IN_GAME:
				timeLeft = GAME_DURATION;
				this.progressBar.setValue(0);
				gamePanel.setVisible(true);
				goose.setVisible(true);
				timerLabel.setVisible(true);
				break;
			case END:
				endPanel.refresh();
				endPanel.setVisible(true);
				break;
		}
	}

	public enum GameState
	{
		START,
		IN_GAME,
		END
	}
}
