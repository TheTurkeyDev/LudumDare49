package dev.theturkey.ld49;

import dev.theturkey.ld49.virusmg.VirusMinigame;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

public class Core
{
	private static final String BASE_FOLDER = "";
	private static final Map<WatchKey, Path> KEY_MAP = new HashMap<>();

	private VirusMinigame miniGame = null;

	private WatchService watcher;
	private String picturesPassword;

	public Core()
	{
		try
		{
			watcher = FileSystems.getDefault().newWatchService();
			picturesPassword = "testing123"; //TODO: randomize
			initFilesAndFolders();
			initFileWatcher();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initFilesAndFolders() throws Exception
	{
		File baseFolder = new File(BASE_FOLDER);

		// OS FOLDER
		File osFolder = this.initFolder("Operating System", baseFolder);
		File clickMe = new File(osFolder, "CLICK ME!.txt");
		clickMe.createNewFile();


		// IMPORTANT DOCS FOLDER
		File importantDocsFolder = this.initFolder("Important Docs", baseFolder);


		// TBN
		File myPicsFolder = this.initFolder("My Pics", baseFolder);
		File fileToAdd = new File(myPicsFolder, "aFile.txt");
		fileToAdd.createNewFile();
		FileWriter writer = new FileWriter(fileToAdd);
		writer.append("This is a test!");
		writer.close();

		ZipParameters zipParameters = new ZipParameters();
		zipParameters.setEncryptFiles(true);
		zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
		zipParameters.setEncryptionMethod(EncryptionMethod.AES);

		ZipFile zipFile = new ZipFile(new File(baseFolder, "compressedPics.zip"), picturesPassword.toCharArray());
		zipFile.addFolder(myPicsFolder, zipParameters);
		zipFile.close();

		deleteFilesInDir(myPicsFolder);
		myPicsFolder.delete();

		registerFolderWatcher(osFolder.toPath());

		//new VirusMinigame(picturesPassword);
	}

	private File initFolder(String name, File parent)
	{
		File folder = new File(parent, name);

		if(folder.exists())
		{
			this.deleteFilesInDir(folder);
			if(!folder.delete())
				System.out.println("Failed to delete: " + folder.toURI());
		}

		folder.mkdir();
		return folder;
	}

	private void deleteFilesInDir(File folder)
	{
		File[] files = folder.listFiles();
		if(files == null)
			return;

		for(File f : files)
		{
			if(f.isFile())
				f.delete();
			else if(f.isDirectory())
				this.deleteFilesInDir(f);
		}
	}


	private void registerFolderWatcher(Path folder) throws IOException
	{
		KEY_MAP.put(folder.register(watcher, ENTRY_DELETE, ENTRY_MODIFY), folder);
	}

	private void initFileWatcher()
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
				Path p = new File(BASE_FOLDER).toPath().relativize(fullPath);

				// Email the file to the
				//  specified email alias.
				System.out.format("File edited: %s%n", p);
				onFileChange(p.toString());
				//Details left to reader....
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
	}

	private void onFileChange(String file)
	{
		switch(file)
		{
			case "Operating System\\CLICK ME!.txt":
				if(miniGame == null)
					miniGame = new VirusMinigame(this, picturesPassword);
				break;
		}
	}

	public void endMiniGame()
	{
		this.miniGame = null;
	}

	public static void main(String[] args)
	{
		new Core();
	}
}
