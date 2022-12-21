package chord.relations.persist;

import java.io.File;

/**
 * Configuration class for PersistModelStrategy to
 * save to file.
 * 
 * This class exists to conform to the 
 * Command design pattern of the PersistModelStrategy interface.
 * @author DAD
 *
 */
public class FileStrategyConfig {

	/**
	 * source or destination file for persist strategy.
	 */
	private File sourceDestFile;
	
	/**
	 * Create the configuration with the specified
	 * source/destination file.
	 * @param sourceDestFile source/destination file,
	 * may not be null or a directory.
	 */
	public FileStrategyConfig(File sourceDestFile) {
		if(sourceDestFile == null) {
			throw new NullPointerException("sourceDest file may not be null");
		}
		if(sourceDestFile.isDirectory()) {
			throw new IllegalArgumentException("sourceDestFile may not be a directory");
		}
		this.sourceDestFile = sourceDestFile;
	}
	
	/**
	 * Get the source/destination file.
	 * @return the source to save to or destination
	 * to load from.
	 */
	public File getSourceDestFile() {
		return sourceDestFile;
	}
}
