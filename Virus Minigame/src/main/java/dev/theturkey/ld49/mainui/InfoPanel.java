package dev.theturkey.ld49.mainui;

import dev.theturkey.ld49.Core;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel
{
	private final JLabel virusRemove;
	private final JLabel faveAnimal;
	private final JLabel osFilesSorted;
	private final JLabel defragedHDD;
	private final JButton launchDefrag;

	private final JLabel winnerLabel;
	private final JButton exit;

	public InfoPanel(MainUI frame)
	{
		this.setBounds(0, 0, MainUI.WIDTH, MainUI.HEIGHT);

		this.setBackground(MainUI.BG_COLOR);
		this.setLayout(null);

		JButton start = new JButton("Refresh");
		this.initTextComponent(start, 100, 25, 50, 50);
		start.addActionListener(actionEvent -> refresh());

		virusRemove = new JLabel("\u2139 Virus Removed  " + (Core.hasBeatVirusMiniGame() ? "\u2714" : "\u274C"));
		virusRemove.setToolTipText("Find and remove the virus on the computer!");
		this.initTextComponent(virusRemove, 400, 50, 50, 100);
		virusRemove.setForeground(Core.hasBeatVirusMiniGame() ? Color.GREEN : Color.RED);
		Font f = new Font(virusRemove.getFont().getName(), Font.BOLD, 22);
		virusRemove.setFont(f);

		faveAnimal = new JLabel("\u2139 Favorite Animal Restored  " + (Core.isFaveAnimalCorrect() ? "\u2714" : "\u274C"));
		faveAnimal.setToolTipText("Replace the corrupted contents of the favorite animal file with the correct animal name!");
		this.initTextComponent(faveAnimal, 400, 50, 50, 150);
		faveAnimal.setForeground(Core.isFaveAnimalCorrect() ? Color.GREEN : Color.RED);
		faveAnimal.setFont(f);

		osFilesSorted = new JLabel("\u2139 OS Files Sorted  " + (Core.isOSFilesSorted() ? "\u2714" : "\u274C"));
		osFilesSorted.setToolTipText("Sort the files in the operating system to their correct Folder!");
		this.initTextComponent(osFilesSorted, 400, 50, 50, 200);
		osFilesSorted.setForeground(Core.isOSFilesSorted() ? Color.GREEN : Color.RED);
		osFilesSorted.setFont(f);

		defragedHDD = new JLabel("\u2139 Hard Drive Defragged  " + (Core.isHddDefragged() ? "\u2714" : "\u274C"));
		defragedHDD.setToolTipText("Click the button to defrag the hard drive!");
		this.initTextComponent(defragedHDD, 400, 50, 50, 250);
		defragedHDD.setForeground(Core.isHddDefragged() ? Color.GREEN : Color.RED);
		defragedHDD.setFont(f);

		launchDefrag = new JButton("Start Defrag");
		this.initTextComponent(launchDefrag, 150, 25, 600, 265);
		launchDefrag.setVisible(!Core.isHddDefragged());
		launchDefrag.addActionListener(actionEvent -> Core.startDefragMiniGame());

		winnerLabel = new JLabel("Computer Fixed! You Win!");
		this.initTextComponent(winnerLabel, 400, 50, 50, MainUI.HEIGHT - 100);
		winnerLabel.setForeground(Color.GREEN);
		winnerLabel.setVisible(false);
		winnerLabel.setFont(f);

		exit = new JButton("Exit");
		this.initTextComponent(exit, 100, 25, 650, MainUI.HEIGHT - 85);
		exit.setVisible(false);
		exit.addActionListener(actionEvent -> System.exit(0));
	}

	private void initTextComponent(JComponent component, int width, int height, int x, int y)
	{
		component.setVisible(true);
		component.setBackground(MainUI.BG_COLOR);
		component.setForeground(MainUI.FG_COLOR);
		component.setBounds(x, y, width, height);
		this.add(component);
	}

	public void refresh()
	{
		boolean virusMG = Core.hasBeatVirusMiniGame();
		boolean fave = Core.isFaveAnimalCorrect();
		boolean osSorted = Core.isOSFilesSorted();
		boolean hddDefragged = Core.isHddDefragged();

		virusRemove.setForeground(virusMG ? Color.GREEN : Color.RED);
		virusRemove.setText("\u2139 Virus Removed  " + (virusMG ? "\u2714" : "\u274C"));
		faveAnimal.setForeground(fave ? Color.GREEN : Color.RED);
		faveAnimal.setText("\u2139 Favorite Animal Restored  " + (fave ? "\u2714" : "\u274C"));
		osFilesSorted.setForeground(osSorted ? Color.GREEN : Color.RED);
		osFilesSorted.setText("\u2139 OS Files Sorted  " + (osSorted ? "\u2714" : "\u274C"));
		defragedHDD.setForeground(hddDefragged ? Color.GREEN : Color.RED);
		defragedHDD.setText("\u2139 Hard Drive Defragged  " + (hddDefragged ? "\u2714" : "\u274C"));
		launchDefrag.setVisible(!Core.isHddDefragged());

		boolean gameOver = fave && virusMG && osSorted && hddDefragged;
		winnerLabel.setVisible(gameOver);
		exit.setVisible(gameOver);
	}
}
