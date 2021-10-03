package dev.theturkey.ld49.mainui;

import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame
{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	public static final Color BG_COLOR = Color.decode("#212529");
	public static final Color FG_COLOR = Color.decode("#d1d1d1");

	private Panels currentPanel = Panels.START;

	private StartPanel startPanel;
	private InfoPanel infoPanel;

	public MainUI()
	{
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);

		this.getContentPane().setBackground(BG_COLOR);
		this.setLayout(null);

		startPanel = new StartPanel(this);
		this.add(startPanel);

		infoPanel = new InfoPanel(this);
		infoPanel.setVisible(false);
		this.add(infoPanel);

		startPanel.setVisible(true);
		this.setVisible(true);
	}

	public void setCurrentPanel(Panels panel)
	{
		switch(currentPanel)
		{
			case START:
				startPanel.setVisible(false);
				break;
			case INFO:
				infoPanel.setVisible(false);
				break;
		}

		this.currentPanel = panel;

		switch(currentPanel)
		{
			case START:
				startPanel.setVisible(true);
				break;
			case INFO:
				infoPanel.setVisible(true);
				break;
		}
	}

	public void refresh()
	{
		if(currentPanel == Panels.INFO)
			infoPanel.refresh();
	}

	public enum Panels
	{
		START,
		INFO
	}
}
