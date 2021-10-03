package dev.theturkey.ld49.defragmg;

import dev.theturkey.ld49.mainui.MainUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DefragMainPanel extends JPanel
{
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private static final String[] colorWords = {"Red", "Blue", "Black", "Yellow", "Magenta", "Cyan"};
	private static final Color[] colors = {Color.RED, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.BLACK};

	private final List<CollectionArea> collectionAreas = new ArrayList<>();

	private DefragMiniGame game;
	private Fragment clicked = null;
	private int top;
	private int left;

	public DefragMainPanel(DefragMiniGame game)
	{
		this.game = game;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		left = (screenSize.width / 2) - (WIDTH / 2);
		top = (screenSize.height / 2) - (HEIGHT / 2);
		this.setBounds(left, top, WIDTH, HEIGHT);
		this.setLayout(null);
		this.setBackground(MainUI.BG_COLOR);

		CollectionArea area1 = new CollectionArea();
		area1.setBounds(10, 10, 100, 50);
		this.add(area1);
		collectionAreas.add(area1);

		CollectionArea area2 = new CollectionArea();
		area2.setBounds(350, 10, 100, 50);
		this.add(area2);
		collectionAreas.add(area2);

		CollectionArea area3 = new CollectionArea();
		area3.setBounds(690, 10, 100, 50);
		this.add(area3);
		collectionAreas.add(area3);

		CollectionArea area4 = new CollectionArea();
		area4.setBounds(10, 540, 100, 50);
		this.add(area4);
		collectionAreas.add(area4);

		CollectionArea area5 = new CollectionArea();
		area5.setBounds(350, 540, 100, 50);
		this.add(area5);
		collectionAreas.add(area5);

		CollectionArea area6 = new CollectionArea();
		area6.setBounds(690, 540, 100, 50);
		this.add(area6);
		collectionAreas.add(area6);

		for(int i = 0; i < 5; i++)
			spawnFragment();

		randomize();
	}

	private void spawnFragment()
	{
		Fragment fragment = new Fragment(this, colors[(int) (Math.random() * colors.length)]);
		this.add(fragment);
		this.repaint();
	}

	public void tick()
	{
		Point p = MouseInfo.getPointerInfo().getLocation();
		if(clicked != null)
		{
			Point copy = new Point(p);
			copy.translate(-left - clicked.getWidth() / 2, -top - clicked.getHeight() / 2);
			clicked.setLocation(copy);
		}
	}

	private void randomize()
	{
		List<String> colorsToUse = new ArrayList<>(Arrays.asList(colorWords));
		for(CollectionArea area : collectionAreas)
		{
			String color = colorsToUse.remove((int) (Math.random() * colorsToUse.size()));
			area.setText(color);
			area.setForeground(colors[(int) (Math.random() * colors.length)]);
		}
	}

	public void pressed(Fragment fragment)
	{
		clicked = fragment;
	}

	public void released()
	{
		Point p = MouseInfo.getPointerInfo().getLocation();
		Point copy = new Point(p);
		copy.translate(-left, -top);
		for(CollectionArea c : collectionAreas)
		{
			if(c.getBounds().contains(copy))
			{
				if(c.acceptsColor(clicked.getBackground()))
				{
					clicked.setVisible(false);
					this.remove(clicked);
					this.game.incProgress();
					this.spawnFragment();
					if(Math.random() < 0.25)
						this.randomize();
				}
				else
				{
					clicked.reset(this);
				}
			}
		}
		clicked = null;
	}
}
