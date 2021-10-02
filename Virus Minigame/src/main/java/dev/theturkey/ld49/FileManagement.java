package dev.theturkey.ld49;

import dev.theturkey.ld49.mainui.ErrorPopUp;
import dev.theturkey.ld49.tasks.OperatingSystemSortTask;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

public class FileManagement
{
	private static final Map<WatchKey, Path> KEY_MAP = new HashMap<>();

	private static final String[] PASSWORD_OPTIONS = {
			"Pizza", "Hat", "Paper", "Egg", "Table", "Balloon", "Radio", "Watch", "Lamp", "Card", "Sponge",
			"Tree", "Carrot", "Umbrella", "Door", "Marble", "Belt", "Pillow", "Remote", "Shovel", "Pen"
	};
	private static final String[] ANIMAL_OPTIONS = {"penguin", "turkey"};

	private WatchService watcher;
	private String picturesPassword;
	private String faveAnimal;

	private OperatingSystemSortTask osSortTask;

	public void init()
	{
		try
		{
			osSortTask = new OperatingSystemSortTask();
			watcher = FileSystems.getDefault().newWatchService();
			picturesPassword = getRandomPasswordWord() + getRandomPasswordWord() + getRandomPasswordWord(); //TODO: randomize
			initFilesAndFolders();
			initFileWatcher();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private String getRandomPasswordWord()
	{
		return PASSWORD_OPTIONS[(int) (Math.random() * PASSWORD_OPTIONS.length)];
	}

	private void initFilesAndFolders() throws Exception
	{
		File baseFolder = new File(Core.BASE_FOLDER);

		// OS FOLDER
		File osFolder = this.initFolder("Operating System", baseFolder);
		File helpFile = new File(osFolder, "help.txt");
		if(!helpFile.createNewFile())
			ErrorPopUp.addError("Failed to create: " + helpFile.toURI());
		FileWriter writer = new FileWriter(helpFile);
		writer.append("Debug=false");
		writer.close();

		osSortTask.initFilesAndFolders(this, osFolder);


		// IMPORTANT DOCS FOLDER
		File importantDocsFolder = this.initFolder("Important Docs", baseFolder);
		File importantFile = new File(importantDocsFolder, "MyFavoriteAnimal.txt");
		if(!importantFile.createNewFile())
			ErrorPopUp.addError("Failed to create: " + importantFile.toURI());
		writer = new FileWriter(importantFile);
		writer.append("ʀ���$��(�UD-Ib��%]�luq�sL�/���o");
		writer.close();

		// TBN
		File myPicsFolder = this.initFolder("My Pics", baseFolder);
		File fileToAdd = new File(myPicsFolder, "@s#%a!r~%w.png");
		if(!fileToAdd.createNewFile())
			ErrorPopUp.addError("Failed to create: " + fileToAdd.toURI());
		faveAnimal = ANIMAL_OPTIONS[(int) (Math.random() * ANIMAL_OPTIONS.length)];
		ImageIO.write(getImage("/" + faveAnimal + ".png"), "png", fileToAdd);

		ZipParameters zipParameters = new ZipParameters();
		zipParameters.setEncryptFiles(true);
		zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
		zipParameters.setEncryptionMethod(EncryptionMethod.AES);

		ZipFile zipFile = new ZipFile(new File(baseFolder, "compressedPics.zip"), picturesPassword.toCharArray());
		try
		{
			zipFile.addFolder(myPicsFolder, zipParameters);
		} catch(Exception e)
		{
			ErrorPopUp.addError(e.getMessage());
		}
		zipFile.close();

		deleteFilesInDir(myPicsFolder);
		if(!myPicsFolder.delete())
			ErrorPopUp.addError("Failed to delete: " + myPicsFolder.toURI());


		registerFolderWatcher(osFolder.toPath());
		registerFolderWatcher(importantDocsFolder.toPath());
		//new VirusMinigame(picturesPassword);
	}

	public File initFolder(String name, File parent)
	{
		File folder = new File(parent, name);

		if(folder.exists())
		{
			this.deleteFilesInDir(folder);
			if(!folder.delete())
				ErrorPopUp.addError("Failed to delete: " + folder.toURI());
		}

		try
		{
			Thread.sleep(100);
		} catch(InterruptedException e)
		{
			e.printStackTrace();
		}

		if(!folder.mkdir())
			ErrorPopUp.addError("Failed to create folder: " + folder.toURI());

		return folder;
	}

	private void deleteFilesInDir(File folder)
	{
		File[] files = folder.listFiles();
		if(files == null)
			return;

		for(File f : files)
		{
			if(f.isDirectory())
				this.deleteFilesInDir(f);

			if(!f.delete())
				ErrorPopUp.addError("Failed to delete: " + f.toURI());
		}
	}


	public void registerFolderWatcher(Path folder) throws IOException
	{
		KEY_MAP.put(folder.register(watcher, ENTRY_DELETE, ENTRY_MODIFY), folder);
	}

	private void initFileWatcher()
	{
		Thread thread = new Thread(() ->
		{
			for(; ; )
			{
				// wait for key to be signaled
				WatchKey key;
				try
				{
					key = watcher.take();
				} catch(InterruptedException x)
				{
					return;
				}

				Path parentFolder = KEY_MAP.get(key);

				for(WatchEvent<?> event : key.pollEvents())
				{
					WatchEvent.Kind<?> kind = event.kind();

					// This key is registered only
					// for ENTRY_CREATE events,
					// but an OVERFLOW event can
					// occur regardless if events
					// are lost or discarded.
					if(kind == OVERFLOW)
					{
						continue;
					}

					// The filename is the
					// context of the event.
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path eventPath = ev.context();

					Path fullPath = parentFolder.resolve(eventPath);
					Path p = new File(Core.BASE_FOLDER).toPath().relativize(fullPath);

					onFileChange(p.toString());
				}

				// Reset the key -- this step is critical if you want to
				// receive further watch events.  If the key is no longer valid,
				// the directory is inaccessible so exit the loop.
				boolean valid = key.reset();
				if(!valid)
				{
					break;
				}
			}
		});
		thread.start();
	}

	private void onFileChange(String file)
	{
		switch(file)
		{
			case "Operating System\\help.txt":
				Core.startVirusMiniGame(picturesPassword);
				break;
			case "Operating System\\Users":
			case "Operating System\\Programs":
			case "Operating System\\Games":
				osSortTask.checkFolder(new File(Core.BASE_FOLDER + "\\" + file));
				break;
			case "Important Docs\\MyFavoriteAnimal.txt":
				String contents = getFileContents(new File(Core.BASE_FOLDER + "\\" + file));
				Core.setFaveAnimalCorrect(contents.trim().equalsIgnoreCase(faveAnimal));
		}
	}

	private String getFileContents(File f)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(f));
			StringBuilder contents = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null)
				contents.append(line).append("\n");

			return contents.toString();
		} catch(Exception e)
		{
			ErrorPopUp.addError(e.getMessage());
		}
		return "";
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

	public boolean isOSFilesSorted()
	{
		return osSortTask.isComplete();
	}
}
