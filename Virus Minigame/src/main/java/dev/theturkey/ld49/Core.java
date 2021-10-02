package dev.theturkey.ld49;

import dev.theturkey.ld49.mainui.MainUI;
import dev.theturkey.ld49.virusmg.VirusMinigame;

public class Core
{
	public static String BASE_FOLDER = "";

	private static MainUI mainUI;
	private static FileManagement fileManagement;
	private static VirusMinigame miniGame = null;

	private static boolean virusMiniGameBeat = false;
	private static boolean faveAnimal = false;

	public static void startVirusMiniGame(String password)
	{
		if(miniGame == null)
			miniGame = new VirusMinigame(password);
	}

	public static void endMiniGame()
	{
		miniGame = null;
		virusMiniGameBeat = true;
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
