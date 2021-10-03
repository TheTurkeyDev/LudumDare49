package dev.theturkey.ld49;

import dev.theturkey.ld49.defragmg.DefragMiniGame;
import dev.theturkey.ld49.mainui.MainUI;
import dev.theturkey.ld49.virusmg.VirusMinigame;

import java.awt.*;

public class Core
{
	public static String BASE_FOLDER = "";

	private static MainUI mainUI;
	private static FileManagement fileManagement;
	private static VirusMinigame miniGame = null;
	private static DefragMiniGame defragMiniGame = null;

	private static boolean virusMiniGameBeat = false;
	private static boolean faveAnimal = false;
	private static boolean hddDefragged = false;

	public static void startVirusMiniGame(String password)
	{
		if(miniGame == null || !miniGame.isShowing())
		{
			mainUI.setState(Frame.ICONIFIED);
			miniGame = new VirusMinigame(password);
		}
	}

	public static void startDefragMiniGame()
	{
		if(defragMiniGame == null || !defragMiniGame.isShowing())
		{
			mainUI.setState(Frame.ICONIFIED);
			defragMiniGame = new DefragMiniGame();
		}
	}

	public static void endMiniGame()
	{
		miniGame = null;
		virusMiniGameBeat = true;
		mainUI.setState(Frame.NORMAL);
		mainUI.refresh();
	}

	public static void endDefragMiniGame()
	{
		defragMiniGame = null;
		mainUI.setState(Frame.NORMAL);
		mainUI.refresh();
	}

	public static boolean hasBeatVirusMiniGame()
	{
		return virusMiniGameBeat;
	}

	public static void setFaveAnimalCorrect(boolean correct)
	{
		faveAnimal = correct;
		mainUI.refresh();
	}

	public static void refreshUI()
	{
		mainUI.refresh();
	}

	public static boolean isFaveAnimalCorrect()
	{
		return faveAnimal;
	}

	public static void setHddDefragged(boolean defragged)
	{
		hddDefragged = defragged;
		mainUI.refresh();
	}

	public static boolean isHddDefragged()
	{
		return hddDefragged;
	}

	public static boolean isOSFilesSorted()
	{
		return fileManagement != null && fileManagement.isOSFilesSorted();
	}

	public static void main(String[] args)
	{
		mainUI = new MainUI();
	}

	public static void initFileSystem()
	{
		fileManagement = new FileManagement();
		fileManagement.init();
	}
}
