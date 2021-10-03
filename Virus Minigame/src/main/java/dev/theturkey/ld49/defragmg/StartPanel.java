package dev.theturkey.ld49.defragmg;

import dev.theturkey.ld49.mainui.MainUI;

import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel
{
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private DefragMiniGame game;

	public StartPanel(DefragMiniGame game)
	{
		this.game = game;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int left = (screenSize.width / 2) - (WIDTH / 2);
		int top = (screenSize.height / 2) - (HEIGHT / 2);
		this.setBounds(left, top, WIDTH, HEIGHT);
		this.setLayout(null);
		this.setBackground(MainUI.BG_COLOR);

		JLabel title = new JLabel("Hard Drive Defragger", SwingConstants.CENTER);
		title.setBounds(10, 10, WIDTH - 20, 60);
		Font f = new Font(title.getFont().getName(), Font.BOLD, 48);
		title.setFont(f);
		title.setForeground(MainUI.FG_COLOR);
		this.add(title);
		title.setVisible(true);

		JTextArea info = new JTextArea("Welcome to the Hard Drive Defragger program! The goal of the game is to " +
				"click, drag, and drop the various colored squares to their matching color word. Beware though! This " +
				"computer is so unstable that the words dont match their respective colors and will often change locations. " +
				"Also there seems to be a goose on the loose? No idea what that's about, but he really seems to " +
				"want your cursor...\n\nYou have 3 minutes to correctly match 100 Rectangles with their " +
				"respective color word. The progress bar and timer counter are both located at the top of this window." +
				"\n\nClick Start to begin...");
		info.setBounds(10, 75, WIDTH - 20, HEIGHT - 200);
		f = new Font(info.getFont().getName(), Font.PLAIN, 18);
		info.setFont(f);
		info.setForeground(MainUI.FG_COLOR);
		info.setBackground(MainUI.BG_COLOR);
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		info.setEditable(false);
		this.add(info);
		info.setVisible(true);

		JButton button = new JButton("Start");
		button.setBounds((WIDTH / 2) - 50, HEIGHT - 50, 100, 25);
		button.setVisible(true);
		button.setBackground(MainUI.BG_COLOR);
		button.setForeground(MainUI.FG_COLOR);
		this.add(button);
		button.addActionListener(actionEvent -> game.setGameState(DefragMiniGame.GameState.IN_GAME));
	}
}

