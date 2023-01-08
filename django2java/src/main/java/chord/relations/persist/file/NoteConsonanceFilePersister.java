package chord.relations.persist.file;

import chord.relations.NoteConsonanceModel;
import chord.relations.record.NoteConsonanceRecord;
import chord.relations.request.NoteConsonanceRecordRequest;

public class NoteConsonanceFilePersister extends 
	AbstractFilePersister<
		NoteConsonanceRecord, 
		NoteConsonanceRecordRequest, 
		NoteConsonanceModel> {
	
	private static final String NOTE_CONSONANCE_FILE_TYPE_SIGNIFIER = "NoteConsonanceModelFile";
	
	public NoteConsonanceFilePersister(FileStrategyConfig config, NoteConsonanceRecordRequest request) {
		super(config, request);
	}

	@Override
	protected String getFileTypeSignifier() {
		return NOTE_CONSONANCE_FILE_TYPE_SIGNIFIER;
	}

	@Override
	protected NoteConsonanceModel createEmptyModel() {
		return new NoteConsonanceModel();
	}

	@Override
	protected NoteConsonanceRecord createRecordFromString(String persistedString) {
		return NoteConsonanceRecord.createRecordFromString(persistedString);
	}
}
