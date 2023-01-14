package chord.maps;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import chord.Chord;
import chord.Interval;
import chord.NoteName;
import chord.Scale;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;
import chord.relations.ChordChangeConsonanceModel;
import chord.relations.NoteConsonanceModel;
import chord.relations.ScaleConsonanceModel;
import chord.relations.record.ChordChangeConsonanceRecord;
import chord.relations.record.NoteConsonanceRecord;
import chord.relations.record.ScaleConsonanceRecord;
import chord.relations.request.ChordChangeConsonanceRecordRequest;
import chord.relations.request.NoteConsonanceRecordRequest;
import chord.relations.request.ScaleConsonanceRecordRequest;

/**
 * Chord library to be used to look up chords by type and root. 
 * Also exists to prevent the creation of identical chord objects and
 * waste memory.
 * @author DAD
 *
 */
public class ChordLibrary {

	public static final String ROOT_NOTE_CANNOT_BE_NULL_MESSAGE = "Note value cannot be null.";
	public static final String CHORD_TYPE_CANNOT_BE_NULL_MESSAGE= "Chord type variable may not be null.";
	public static final String CHORD_TYPE_INVALID = "The given class is not supported.";
	private static final String FILE_DOES_NOT_EXIST_MESSAGE = "The given rating file does not exist.";
	private static final String USER_DOES_NOT_HAVE_READ_PERMISSIONS_FOR_FILE = "The user does not have read permissions for the given file.";

	private static final Logger logger = LogManager.getLogger();

	private static ChordLibrary mainLibrary;

	private static boolean mainLibraryHasBeenInitialized = false;

	/**
	 * Initialize the ChordLibrary instance with the given models.
	 * @param cccModel non-null and full
	 * @param scModel non-null and full
	 * @param ncModel non-null and full
	 */
	public static void initializeChordLibraryInstance(
			ChordChangeConsonanceModel cccModel, 
			ScaleConsonanceModel scModel, 
			NoteConsonanceModel ncModel) {
		logger.info("Attempting to create ChordLibrary instance.");

		if( mainLibraryHasBeenInitialized ) {
			throw new IllegalStateException("ChordLibrary can only be initialized once.");
		}

		logger.info("Creating chord library.");
		mainLibrary = new ChordLibrary(cccModel, scModel, ncModel);
		logger.info("Chord library created successfully.");

		mainLibraryHasBeenInitialized = true;
	}

	/**
	 * Get the chord library instance.
	 * 
	 * MUST BE INITIALIZED FIRST BY CALLING "initializeChordLibraryInstance"
	 * @return the main instance of the ChordLibrary
	 */
	public static ChordLibrary getInstance() {
		if(!mainLibraryHasBeenInitialized) {
			throw new IllegalStateException("Library must be initialized before being used...call ChordLibrary.initializeChordLibraryInstance");
		}

		return mainLibrary;
	}

	private final Map<ChordSignature,Map<NoteName,Chord>> chordMap;

	//models to be used to find related musical entities
	private final ChordChangeConsonanceModel cccModel;
	private final ScaleConsonanceModel scModel;
	private final NoteConsonanceModel ncModel;



	/**
	 * Create an chord instance for every chord signature and rootNote
	 * combination.
	 * 
	 * All model parameters must be initialized when added.
	 * 
	 * 
	 * @param cccModel ChordChangeConsonanceModel containing relations between chords.
	 * @param scModel ScaleConsonanceModel containing relations between chords and scales.
	 * @param ncModel NoteConsonanceModel containing relations between chords and intervals.
	 */
	private ChordLibrary(
			ChordChangeConsonanceModel cccModel, 
			ScaleConsonanceModel scModel, 
			NoteConsonanceModel ncModel) {
		if(cccModel == null || scModel == null || ncModel == null) {
			throw new NullPointerException("models may not be null");
		}
		if( !cccModel.isFull() || !scModel.isFull() || !ncModel.isFull()) {
			logger.warn("models added to ChordLibrary instance are not complete.");
		}
		this.cccModel = cccModel;
		this.scModel = scModel;
		this.ncModel = ncModel;

		Map<ChordSignature,Map<NoteName,Chord>> tempChordMap = 
				new EnumMap<>(ChordSignature.class);

		for(ChordSignature sig : ChordSignature.values()) {
			Map<NoteName,Chord> noteToChordMap = new EnumMap<>(NoteName.class);

			for(NoteName note : NoteName.values()) {
				Chord nChord = new Chord(note, sig);

				noteToChordMap.put(note, nChord);
			}

			//make sure clients cannot change anything added to the library
			tempChordMap.put(sig, Collections.unmodifiableMap(noteToChordMap));
		}

		//make sure clients cannot change anything added to the library
		chordMap = Collections.unmodifiableMap(tempChordMap);
	}

	/**
	 * Retrieve a chord from the library with the given rootNote and chord signature.
	 * @param root root note of the chord
	 * @param chordSig signature of the chord(can be retrieved from Signature.StandardChordSignatures
	 * @return chord with the given root note and of the given type, null if the chord does not exist
	 */
	public Chord getChord(NoteName root, ChordSignature chordSig) {
		return chordMap.get(chordSig).get(root);
	}

	/**
	 * Get all chords related to the single reference chord in the chordChangeRequest using the given root note.
	 * @param rootNote rootNote of reference chord
	 * @param chordChangeRequest request containing a single referenceChordSignature, and the target chords defined
	 * by the other parameters of the request
	 * @return set of related chords defined by the chordChangeRequest
	 */
	public Set<Chord> getRelatedChords(NoteName rootNote,ChordChangeConsonanceRecordRequest chordChangeRequest){
		if(rootNote == null) {
			throw new NullPointerException("rootNote may not be null");
		}
		if(chordChangeRequest == null) {
			throw new NullPointerException("chordChangeRequest may not be null.");
		}
		//Make sure that there is only one reference chord we are looking for in the request.
		if(chordChangeRequest.numReferenceChordsRequested() != 1) {
			throw new IllegalArgumentException("There must only be one reference chord within the request.");
		}

		Set<ChordChangeConsonanceRecord> matchingChordRelations = cccModel.getRecords(chordChangeRequest);

		Set<Chord> relatedChords = new HashSet<>();
		for(ChordChangeConsonanceRecord chordRelationRecord : matchingChordRelations) {
			NoteName rootNoteOfRelatedChord = 
					rootNote.getNoteByInterval(chordRelationRecord.intervalBetweenRoots());
			ChordSignature chordSignatureOfRelatedChord = chordRelationRecord.endChordSignature();

			relatedChords.add(this.getChord(rootNoteOfRelatedChord, chordSignatureOfRelatedChord));
		}

		return relatedChords;
	}

	/**
	 * Get all of the scales defined by the ScaleConsonanceRecordRequest.
	 * The request must have a SINGLE REFERENCE CHORD, and the scales/ratings
	 * desired by the user.
	 * @param rootNote rootNote of the reference chord and the scales returned.
	 * @param scaleConsonanceRequest request containing a single reference chord signature,
	 * and desired ratings/scaleSignatures
	 * @return set of all concrete scales that match the request using the root note given
	 */
	public Set<Scale> getRelatedScales(NoteName rootNote, ScaleConsonanceRecordRequest scaleConsonanceRequest){
		if(rootNote == null) {
			throw new NullPointerException("rootNote may not be null.");
		}

		Set<ScaleSignature> relatedScaleSignatures = 
				getRelatedScaleSignatures(scaleConsonanceRequest);

		Set<Scale> relatedScales = new HashSet<>();

		ScaleLibrary scaleLibrary = ScaleLibrary.getInstance();
		for(ScaleSignature scaleSignature : relatedScaleSignatures) {
			Scale concreteScale = scaleLibrary.getScale(rootNote, scaleSignature);

			relatedScales.add(concreteScale);
		}

		return relatedScales;
	}

	/**
	 * Get scale signatures related to the reference chord and of the ratings
	 * specified.
	 * @param scaleConsonanceRequest request containing SINGLE REFERENCE CHORD,
	 * and the scales/ratings wanted
	 * @return set of scale signatures reqeusted.
	 */
	public Set<ScaleSignature> getRelatedScaleSignatures(ScaleConsonanceRecordRequest scaleConsonanceRequest){
		if(scaleConsonanceRequest == null) {
			throw new NullPointerException("scaleConsonanceRequest may not be null.");
		}
		if(scaleConsonanceRequest.numReferenceChordsRequested() != 1) {
			throw new IllegalArgumentException("There must only be one reference chord within the request.");
		}

		Set<ScaleConsonanceRecord> relatedScaleRecords = scModel.getRecords(scaleConsonanceRequest);

		Set<ScaleSignature> relatedScaleSignatures = new HashSet<>();

		for(ScaleConsonanceRecord relatedScaleRecord : relatedScaleRecords) {
			ScaleSignature relatedScaleSignature = relatedScaleRecord.scaleSignature();

			relatedScaleSignatures.add(relatedScaleSignature);
		}
		return relatedScaleSignatures;
	}

	/**
	 * Get a set of notes specified by the rootNote and NoteConsonanceRequest.
	 * @param rootNoteOfChord note that used in relation to the request.
	 * @param noteConsonanceRequest request representing the desired related notes.
	 * MUST CONTAIN A SINGLE REFERENCE CHORD, and at least on interval and one rating
	 * @return set of notes related to the rootNote specified by the request.
	 */
	public Set<NoteName> getRelatedNotes(NoteName rootNoteOfChord, NoteConsonanceRecordRequest noteConsonanceRequest){
		if(rootNoteOfChord == null) {
			throw new NullPointerException("root note may not be null");
		}

		Set<Interval> relatedIntervals = getRelatedIntervals(noteConsonanceRequest);

		Set<NoteName> relatedNotes = new HashSet<>();

		for(Interval relatedInterval : relatedIntervals) {
			NoteName relatedNote = rootNoteOfChord.getNoteByInterval(relatedInterval);
			relatedNotes.add(relatedNote);
		}

		return relatedNotes;
	}

	/**
	 * Get all intervals related to the reference chord in the noteConsonanceRequest.
	 * 
	 * 
	 * @param noteConsonanceRequest request containing a single reference chord and 
	 * intervals/ratings requested
	 * @return set of intervals related to the reference chord
	 */
	public Set<Interval> getRelatedIntervals(NoteConsonanceRecordRequest noteConsonanceRequest){
		validateNoteConsonanceRequest(noteConsonanceRequest);

		Set<NoteConsonanceRecord> relatedNoteConsonanceRecords =
				ncModel.getRecords(noteConsonanceRequest);

		Set<Interval> relatedIntervals = new HashSet<>();
		for(NoteConsonanceRecord relatedNoteConsonanceRecord : relatedNoteConsonanceRecords) {
			Interval relatedInterval = relatedNoteConsonanceRecord.interval();
			relatedIntervals.add(relatedInterval);
		}

		return relatedIntervals;
	}

	/**
	 * Check the request and make sure that it only has one referenceChord requested.
	 * @param noteConsonanceRequest request to validate
	 */
	private void validateNoteConsonanceRequest(NoteConsonanceRecordRequest noteConsonanceRequest) {
		if(noteConsonanceRequest == null) {
			throw new NullPointerException("noteConsonanceRequest may not be null");
		}
		if(noteConsonanceRequest.numReferenceChordsRequested() != 1) {
			throw new IllegalArgumentException("request must have only one reference chord.");
		}
	}
}
