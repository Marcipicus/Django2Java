package chord.relations.persist;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Set;

import chord.relations.NoteConsonanceModel;
import chord.relations.record.NoteConsonanceRecord;
import chord.relations.request.NoteConsonanceRecordRequest;

public class NoteConsonanceFilePersistStrategy 
	implements PersistModelStrategy<
		NoteConsonanceRecord, 
		NoteConsonanceRecordRequest, 
		FileStrategyConfig, 
		NoteConsonanceModel> {
	
	private static final String FILE_TYPE_SIGNIFIER = "NoteConsonanceModelFile";

	private FileStrategyConfig config;
	private NoteConsonanceRecordRequest request;
	
	public NoteConsonanceFilePersistStrategy(
			FileStrategyConfig config, 
			NoteConsonanceRecordRequest request) {
		
		setRequest(request);
		configure(config);
	}

	@Override
	public void setRequest(NoteConsonanceRecordRequest request) {
		if(request == null) {
			throw new NullPointerException("request may not be null");
		}
		if(!request.isInitialized()) {
			throw new IllegalArgumentException("request must be initialized.");
		}
		this.request = request;
	}

	@Override
	public void configure(FileStrategyConfig parameters) {
		if(parameters == null) {
			throw new NullPointerException("parameters may not be null");
		}

		this.config = parameters;
	}

	@Override
	public void save(NoteConsonanceModel model) throws PersistenceException {
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
			
			modelWriter.println(FILE_TYPE_SIGNIFIER);

			Set<NoteConsonanceRecord> recordsToSave = model.getRecords(request);
			
			for(NoteConsonanceRecord record : recordsToSave) {
				modelWriter.println(record.createPersistenceString());
			}
			
		} catch (FileNotFoundException e) {
			throw new PersistenceException("error saving to file",e);
		}

	}

	@Override
	public NoteConsonanceModel load() throws PersistenceException {
		if(config == null) {
			throw new IllegalStateException("config has not been initialized");
		}
		
		NoteConsonanceModel model = new NoteConsonanceModel();
		
		try(Scanner fileScanner = new Scanner(config.getSourceDestFile())){
			String line = fileScanner.nextLine();
			if( !line.equals(NoteConsonanceFilePersistStrategy.FILE_TYPE_SIGNIFIER) ) {
				throw new PersistenceException("Incorrect file type, fileSignifier:"+line);
			}
			
			while(fileScanner.hasNextLine()) {
				line = fileScanner.nextLine();
				model.addRating(NoteConsonanceRecord.createRecordFromString(line));
			}
			
		} catch (FileNotFoundException e) {
			throw new PersistenceException("error loading from file",e);
		}

		return model;
	}

}
