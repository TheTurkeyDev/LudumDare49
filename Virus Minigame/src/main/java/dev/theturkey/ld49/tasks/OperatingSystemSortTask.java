package dev.theturkey.ld49.tasks;

import dev.theturkey.ld49.Core;
import dev.theturkey.ld49.FileManagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperatingSystemSortTask
{
	private final String[] programFiles = {"Chrome", "Adobe", "Spotify", "Discord"};
	private final String[] userFiles = {"Timmy", "Admin"};
	private final String[] gameFiles = {"League Of Legands", "Minecraft", "Rocket League", "Steam"};

	private boolean programFilesCorrect = false;
	private boolean userFilesCorrect = false;
	private boolean gameFilesCorrect = false;

	public OperatingSystemSortTask()
	{

	}

	public void initFilesAndFolders(FileManagement fileManagement, File osFolder) throws IOException
	{
		File programsFolder = fileManagement.initFolder("Programs", osFolder);
		File userFolder = fileManagement.initFolder("Users", osFolder);
		File gamesFolder = fileManagement.initFolder("Games", osFolder);

		List<String> files = new ArrayList<>(Arrays.asList(programFiles));
		files.addAll(Arrays.asList(userFiles));
		files.addAll(Arrays.asList(gameFiles));

		for(String osFile : files)
		{
			File f = new File(osFolder, osFile);
			f.createNewFile();
		}

		fileManagement.registerFolderWatcher(gamesFolder.toPath());
		fileManagement.registerFolderWatcher(userFolder.toPath());
		fileManagement.registerFolderWatcher(programsFolder.toPath());
	}

	public void checkFolder(File folder)
	{
		switch(folder.getName())
		{
			case "Programs":
				programFilesCorrect = containsAllFiles(programFiles, folder.list());
				break;
			case "Users":
				userFilesCorrect = containsAllFiles(userFiles, folder.list());
				break;
			case "Games":
				gameFilesCorrect = containsAllFiles(gameFiles, folder.list());
				break;
		}
		Core.refreshUI();
	}

	private boolean containsAllFiles(String[] shouldHave, String[] contained)
	{
		if(contained != null)
		{
			List<String> files = new ArrayList<>(Arrays.asList(contained));
			for(String file : shouldHave)
				if(!files.contains(file))
					return false;
		}
		else
		{
			return false;
		}

		return true;
	}

	public boolean isComplete()
	{
		return this.gameFilesCorrect && this.userFilesCorrect && this.programFilesCorrect;
	}
}
