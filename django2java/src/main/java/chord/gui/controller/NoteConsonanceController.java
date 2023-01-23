package chord.gui.controller;

import java.io.File;

import chord.ConsonanceRating;
import chord.MIDIPlayer;
import chord.exceptions.GenericMIDIException;
import chord.relations.NoteConsonanceModel;
import chord.relations.persist.PersistenceException;
import chord.relations.persist.file.FileStrategyConfig;
import chord.relations.persist.file.NoteConsonanceFilePersister;
import chord.relations.record.NoteConsonanceRecord;
import chord.relations.request.NoteConsonanceRecordRequest;

/**
 * Concrete implementation of the RatngModelController to
 * mediate between the NoteConsonanceModel data structure
 * and the gui.
 * 
 * The controller class was created so that the event model
 * of the gui could be tested in isolation and programatically.
 * @author DAD
 *
 */
public class NoteConsonanceController extends 
RatingModelController<
NoteConsonanceRecord,
NoteConsonanceRecordRequest, 
NoteConsonanceModel> {

	@SafeVarargs
	public NoteConsonanceController(NoteConsonanceModel model, StateChangeListener<NoteConsonanceRecord>... listeners) {
		super(model, listeners);
	}

	@Override
	protected NoteConsonanceRecord createRecordToSave(ConsonanceRating rating) {
		if(currentRecord == null) {
			throw new IllegalStateException(
					"This should not happen. currentRecord is checked by RatingModelController");
		}
		NoteConsonanceRecord recordToSave = 
				new NoteConsonanceRecord(
						currentRecord.chordSignature(),
						currentRecord.interval(),
						rating);
		
		return recordToSave;
	}

	@Override
	public void saveFile(File destinationFile) throws PersistenceException {
		NoteConsonanceRecordRequest request = 
				NoteConsonanceRecordRequest.allPossibleRecords();
		FileStrategyConfig fileConfig = new FileStrategyConfig(destinationFile);
		
		NoteConsonanceFilePersister noteConsonancePersister = 
				new NoteConsonanceFilePersister(
						fileConfig, 
						request);

		noteConsonancePersister.save(model);
	}

	@Override
	public void play() throws GenericMIDIException {
		MIDIPlayer midiPlayer = MIDIPlayer.getInstance();
		
		midiPlayer.playNoteConsonanceRecord(currentRecord);
	}
}
