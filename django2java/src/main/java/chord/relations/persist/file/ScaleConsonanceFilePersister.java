package chord.relations.persist.file;

import chord.relations.ScaleConsonanceModel;
import chord.relations.record.ScaleConsonanceRecord;
import chord.relations.request.ScaleConsonanceRecordRequest;

public class ScaleConsonanceFilePersister
		extends AbstractFilePersister<ScaleConsonanceRecord, ScaleConsonanceRecordRequest, ScaleConsonanceModel> {

	private static final String SCALE_CONSONANCE_FILE_TYPE_SIGNIFIER = "ScaleConsonanceModelFile";
	
	public ScaleConsonanceFilePersister(FileStrategyConfig config, ScaleConsonanceRecordRequest request) {
		super(config, request);
	}

	@Override
	protected String getFileTypeSignifier() {
		return SCALE_CONSONANCE_FILE_TYPE_SIGNIFIER;
	}

	@Override
	protected ScaleConsonanceModel createEmptyModel() {
		return new ScaleConsonanceModel();
	}

	@Override
	protected ScaleConsonanceRecord createRecordFromString(String persistedString) {
		return ScaleConsonanceRecord.createRecordFromString(persistedString);
	}

}
