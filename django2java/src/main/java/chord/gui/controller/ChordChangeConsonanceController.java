package chord.gui.controller;

import java.io.File;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import chord.ConsonanceRating;
import chord.MIDIPlayer;
import chord.exceptions.ChordToneBuildingException;
import chord.exceptions.GenericMIDIException;
import chord.exceptions.InvalidMIDIValueException;
import chord.exceptions.InvalidNoteRegisterException;
import chord.relations.ChordChangeConsonanceModel;
import chord.relations.persist.PersistenceException;
import chord.relations.persist.file.ChordChangeConsonanceFilePersister;
import chord.relations.persist.file.FileStrategyConfig;
import chord.relations.record.ChordChangeConsonanceRecord;
import chord.relations.request.ChordChangeConsonanceRecordRequest;

public class ChordChangeConsonanceController extends 
RatingModelController<
ChordChangeConsonanceRecord, 
ChordChangeConsonanceRecordRequest , 
ChordChangeConsonanceModel>{

	public ChordChangeConsonanceController(ChordChangeConsonanceModel model,
			StateChangeListener<ChordChangeConsonanceRecord>... listeners) {
		super(model, listeners);
	}

	@Override
	public void play() throws GenericMIDIException{
		MIDIPlayer midiPlayer = MIDIPlayer.getInstance();
		
		midiPlayer.playChordChangeConsonanceRecord(currentRecord);
	}

	@Override
	public void saveFile(File destinationFile) throws PersistenceException {
		ChordChangeConsonanceRecordRequest request = 
				ChordChangeConsonanceRecordRequest.allPossibleRecords();
		FileStrategyConfig fileConfig = new FileStrategyConfig(destinationFile);
		
		ChordChangeConsonanceFilePersister chordChangeConsonancePersister = 
				new ChordChangeConsonanceFilePersister(
						fileConfig, 
						request);

		chordChangeConsonancePersister.save(model);
	}

	@Override
	protected ChordChangeConsonanceRecord createRecordToSave(ConsonanceRating rating) {
		if(currentRecord == null) {
			throw new IllegalStateException(
					"This should not happen. currentRecord is checked by RatingModelController");
		}
		ChordChangeConsonanceRecord recordToSave = 
				new ChordChangeConsonanceRecord(
						currentRecord.startChordSignature(),
						currentRecord.endChordSignature(),
						currentRecord.intervalBetweenRoots(),
						rating);
		
		return recordToSave;
	}

}
