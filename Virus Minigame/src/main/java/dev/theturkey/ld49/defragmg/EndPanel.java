package dev.theturkey.ld49.defragmg;

import dev.theturkey.ld49.Core;
import dev.theturkey.ld49.mainui.MainUI;

import javax.swing.*;
import java.awt.*;

public class EndPanel extends JPanel
{
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private final JButton button;
	private final JTextArea info;

	private final String loseText = "You failed to defrag the hard drive! No worries though, you can always try again.\n\nClick Restart to restart the program.";

	public EndPanel(DefragMiniGame game)
	{
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


		info = new JTextArea(loseText);
		info.setBounds(10, 75, WIDTH - 20, HEIGHT - 150);
		f = new Font(info.getFont().getName(), Font.PLAIN, 18);
		info.setFont(f);
		info.setForeground(MainUI.FG_COLOR);
		info.setBackground(MainUI.BG_COLOR);
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		info.setEditable(false);
		this.add(info);
		info.setVisible(true);


		button = new JButton("Restart");
		button.setBounds((WIDTH / 2) - 50, HEIGHT - 50, 100, 25);
		button.setVisible(true);
		button.setBackground(MainUI.BG_COLOR);
		button.setForeground(MainUI.FG_COLOR);
		this.add(button);
		button.addActionListener(actionEvent ->
		{
			if(Core.isHddDefragged())
			{
				game.dispose();
				Core.endDefragMiniGame();
			}
			else
			{
				game.setGameState(DefragMiniGame.GameState.IN_GAME);
			}
		});
	}

	public void refresh()
	{
		String winText = "Congrats you have successfully defragged the hard drive!\n\nClick Close to close the program.";
		info.setText(Core.isHddDefragged() ? winText : loseText);
		button.setText(Core.isHddDefragged() ? "Close" : "Restart");
	}
}

