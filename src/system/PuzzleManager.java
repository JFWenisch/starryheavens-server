package system;

import java.io.File;
import java.io.FilenameFilter;

public class PuzzleManager 
{
	static File puzzledir = new File("bin/puzzles");
	public static File[] getPuzzles()
	{
		
		File[] files = puzzledir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".puz");
		    }
		});
		return files;
	}
}
