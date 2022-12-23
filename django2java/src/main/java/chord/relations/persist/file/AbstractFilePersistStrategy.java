package chord.relations.persist.file;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Set;

import chord.relations.RatingModel;
import chord.relations.persist.PersistModelStrategy;
import chord.relations.persist.PersistenceException;
import chord.relations.record.StringPersistable;
import chord.relations.request.AbstractRecordRequest;

/**
 * Abstract class used for file persistence of models.
 * 
 * To use, overwrite getFileTypeSignifier(), createEmptyModel(),
 * and createRecordFromString()....follow the comments and you should
 * be good.
 * 
 * All three of the generics should go together...
 * i.e. NoteConsonanceRecord,NoteConsonanceRecordRequest, NoteConsonanceModel
 * @author DAD
 *
 * @param <RECORD> type of record to use
 * @param <REQUEST> type of request to use
 * @param <MODEL> type of model to use
 */
public abstract class AbstractFilePersistStrategy<
	RECORD extends StringPersistable,
	REQUEST extends AbstractRecordRequest,
	MODEL extends RatingModel<RECORD,REQUEST>>
	implements PersistModelStrategy<
	RECORD, 
	REQUEST, 
	FileStrategyConfig, 
	MODEL>{
	
	private FileStrategyConfig config;
	
	private REQUEST request;
	
	public AbstractFilePersistStrategy(
			FileStrategyConfig config, 
			REQUEST request) {
		
		setRequest(request);
		configure(config);
	}

	@Override
	public final void setRequest(REQUEST request) {
		if(request == null) {
			throw new NullPointerException("request may not be null");
		}
		if(!request.isInitialized()) {
			throw new IllegalArgumentException("request must be initialized.");
		}
		this.request = request;
	}

	@Override
	public final void configure(FileStrategyConfig configParam) {
		if(configParam == null) {
			throw new NullPointerException("parameters may not be null");
		}

		this.config = configParam;
	}

	@Override
	public final void save(MODEL model) throws PersistenceException {
		if(model == null) {
			throw new NullPointerException("model may not be null");
		}
		if(request == null) {
			throw new IllegalStateException("request has not been initialized");
		}
		if(config == null) {
			throw new IllegalStateException("config has not been initialized");
		}

		try(PrintWriter modelWriter = new PrintWriter(config.getSourceDestFile())){
			
			modelWriter.println(getFileTypeSignifier());

			Set<RECORD> recordsToSave = model.getRecords(request);
			
			for(RECORD record : recordsToSave) {
				modelWriter.println(record.createPersistenceString());
			}
			
		} catch (FileNotFoundException e) {
			throw new PersistenceException("error saving to file",e);
		}

	}

	@Override
	public final MODEL load() throws PersistenceException {
		if(config == null) {
			throw new IllegalStateException("config has not been initialized");
		}
		
		MODEL model = createEmptyModel();
		
		try(Scanner fileScanner = new Scanner(config.getSourceDestFile())){
			String line = fileScanner.nextLine();
			if( !line.equals(getFileTypeSignifier()) ) {
				throw new PersistenceException("Incorrect file type, fileSignifier:"+line);
			}
			
			while(fileScanner.hasNextLine()) {
				line = fileScanner.nextLine();
				model.addRating(createRecordFromString(line));
			}
			
		} catch (FileNotFoundException e) {
			throw new PersistenceException("error loading from file",e);
		}

		return model;
	}
	
	/**
	 * Get the string used to declare the type of
	 * file that is being read/written.
	 * 
	 * This string is the first line of the file
	 * to which the model is being saved.
	 * 
	 * 
	 * @return String declaring the type of file
	 */
	protected abstract String getFileTypeSignifier();
	
	/**
	 * We need to create the model externally since we cannot 
	 * call the constructor of the model directly through generics.
	 * 
	 * @return empty model of the required type.
	 */
	protected abstract MODEL createEmptyModel();
	
	/**
	 * Create the record from the persisted string.
	 * 
	 * we need this method since we cannot call the
	 * static methods directly using generics.
	 * 
	 * @param persistedString string containing the
	 * data required to create the record.
	 * @return record representing the data in the persistedString
	 */
	protected abstract RECORD createRecordFromString(String persistedString);

}
