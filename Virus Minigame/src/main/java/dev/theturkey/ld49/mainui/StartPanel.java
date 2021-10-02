package dev.theturkey.ld49.mainui;

import dev.theturkey.ld49.Core;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class StartPanel extends JPanel
{
	public StartPanel(MainUI frame)
	{
		this.setBounds(0, 0, MainUI.WIDTH, MainUI.HEIGHT);

		this.setBackground(MainUI.BG_COLOR);
		this.setLayout(null);

		JLabel title = new JLabel("WELCOME TO TBN");
		this.initTextComponent(title, 500, 50, (MainUI.WIDTH / 2), 50);
		Font f = new Font(title.getFont().getName(), Font.BOLD, 50);
		title.setFont(f);

		JTextArea textArea = new JTextArea("The goal of this game is to fix your friends unstable computer (Represented by the " +
				"folders and files that get created where you specify below)! The next screen will show you all the " +
				"tasks you have to complete and have completed. Hovering over the text will give you helpful information " +
				"about what the task refers to. This is meant to be more of a puzzle sort of game, so the hints aren't " +
				"very specific.\n\n If you get an error pop up, just try and close and restart the game. Closing this " +
				"window will close and end the game, but, closing any of the other generated windows will not. Some of" +
				"the popup windows have no background to them and are therefore click-throughable, so make sure to " +
				"click on any of the text or images to regain focus if your inputs aren't registering or are focused " +
				"on another background window.");
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setWrapStyleWord(true);
		this.initTextComponent(textArea, MainUI.WIDTH - 50, 350, (MainUI.WIDTH / 2), (MainUI.HEIGHT / 2));
		f = new Font(title.getFont().getName(), Font.PLAIN, 18);
		textArea.setFont(f);

		JLabel folderLabel = new JLabel("Game Folder: " + Core.BASE_FOLDER);
		this.initTextComponent(folderLabel, MainUI.WIDTH - 200, 100, (MainUI.WIDTH / 2), MainUI.HEIGHT - 100);
		f = new Font(folderLabel.getFont().getName(), Font.BOLD, 22);
		folderLabel.setFont(f);

		JButton button = new JButton("Select");
		this.initTextComponent(button, 75, 30, 55, MainUI.HEIGHT - 100);
		button.addActionListener(actionEvent ->
		{
			JFileChooser fileChooser = new JFileChooser(Core.BASE_FOLDER);
			fileChooser.setBackground(MainUI.BG_COLOR);
			fileChooser.setForeground(MainUI.FG_COLOR);
			fileChooser.setDialogTitle("Select the folder where the game files will be created");
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			StartPanel.this.initTextComponent(fileChooser, 200, 50, (MainUI.WIDTH / 2), (MainUI.HEIGHT / 2));
			int returnValue = fileChooser.showOpenDialog(null);
			if(returnValue == JFileChooser.APPROVE_OPTION)
			{
				File selectedFile = fileChooser.getSelectedFile();
				Core.BASE_FOLDER = selectedFile.getAbsolutePath();
				folderLabel.setText("Game Folder: " + Core.BASE_FOLDER);
			}
		});

		JButton start = new JButton("Start");
		this.initTextComponent(start, 75, 25, MainUI.WIDTH / 2, MainUI.HEIGHT - 60);
		start.addActionListener(actionEvent ->
		{
			Core.initFileSystem();
			frame.setCurrentPanel(MainUI.Panels.INFO);
		});
	}

	private void initTextComponent(JComponent component, int width, int height, int x, int y)
	{
		component.setVisible(true);
		component.setBackground(MainUI.BG_COLOR);
		component.setForeground(MainUI.FG_COLOR);
		component.setBounds(x - (width / 2), y - (height / 2), width, height);
		this.add(component);
	}
}
