package chord.relations.persist.file;

import chord.relations.ChordChangeConsonanceModel;
import chord.relations.record.ChordChangeConsonanceRecord;
import chord.relations.request.ChordChangeConsonanceRecordRequest;

public class ChordChangeConsonanceFilePersister extends
		AbstractFilePersister<ChordChangeConsonanceRecord, ChordChangeConsonanceRecordRequest, ChordChangeConsonanceModel> {

	private static final String CHORD_CHANGE_CONSONANCE_FILE_TYPE_SIGNIFIER = "ChordChangeConsonanceModelFile";
	
	
	public ChordChangeConsonanceFilePersister(FileStrategyConfig config,
			ChordChangeConsonanceRecordRequest request) {
		super(config, request);
	}

	@Override
	protected String getFileTypeSignifier() {
		return CHORD_CHANGE_CONSONANCE_FILE_TYPE_SIGNIFIER;
	}

	@Override
	protected ChordChangeConsonanceModel createEmptyModel() {
		return new ChordChangeConsonanceModel();
	}

	@Override
	protected ChordChangeConsonanceRecord createRecordFromString(String persistedString) {
		return ChordChangeConsonanceRecord.createRecordFromString(persistedString);
	}

}
