package chord.maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import chord.Chord;
import chord.NoteName;
import chord.ident.ChordSignature;

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

	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

	private static final String ratingFileSchemaPath = "C:\\Users\\DAD\\Documents\\Java Projects\\django2java\\src\\main\\resources\\ChordChangeRatingSchema.xsd";
	private static final String ratingFileXMLFormat = "C:\\Users\\DAD\\Documents\\Java Projects\\django2java\\src\\main\\resources\\ChordChangeFiles\\MainChordChanges.xml";
	private static final String ratingFileTextFormat = "C:\\Users\\DAD\\Documents\\Java Projects\\django2java\\src\\main\\resources\\ChordChangeFiles\\MainChordChanges.txt";

	private static final Logger logger = LogManager.getLogger();

	private static ChordLibrary mainLibrary;

	public static ChordLibrary getInstance() {
		if(mainLibrary == null) {
			logger.info("Creating chord library.");
			mainLibrary = new ChordLibrary();
			logger.info("Chord Library created successfully");
		}
		return mainLibrary;
	}

	private Map<ChordSignature,Map<NoteName,Chord>> chordMap;

	private ChordLibrary() {
		chordMap = new HashMap<ChordSignature,Map<NoteName,Chord>>();

		List<ChordSignature> allChordSignatures = ChordSignature.getAllChordSignatures();

		for(ChordSignature sig : allChordSignatures) {
			Map<NoteName,Chord> noteToChordMap = new HashMap<NoteName,Chord>();
			
			for(NoteName note : NoteName.values()) {
				Chord nChord = new Chord(note, sig);

				noteToChordMap.put(note, nChord);
			}

			//make sure clients cannot change anything added to the library
			chordMap.put(sig, Collections.unmodifiableMap(noteToChordMap));
		}
		
		//make sure clients cannot change anything added to the library
		chordMap = Collections.unmodifiableMap(chordMap);
	}
	
	/**
	 * Retrieve a chord from the library with the given rootNote and chord signature.
	 * @param root root note of the chord
	 * @param chordSig signature of the chord(can be retrieved from Signature.StandardChordSignatures
	 * @return chord with the given root note and of the given type, null if the chord does not exist
	 */
	public Chord get(NoteName root, ChordSignature chordSig) {
		return chordMap.get(chordSig).get(root);
	}
}
