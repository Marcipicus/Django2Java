package chord.relations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;

/**
 * Main data model for rating the consonance of chord signatures to
 * single notes. The relationship between the notes and chords is defined
 * by the interval between the root note of the chord and the note that is
 * being rated.
 */
public class NoteConsonanceModel implements RatingModel<NoteConsonanceRecord,NoteConsonanceRecordRequest>{

	/**
	 * String used to indicate that a file represents a NoteConsonanceModel.
	 * This will be the first line in the file and the program will not attempt
	 * to read the file unless the string exists and is in the first line.
	 */
	private static final String noteConsonanceModelFileFormatIndicator = "NOTE_CONSONANCE_MODEL_FILE";
	/**
	 * String used to signify that a chord signature is on the line and
	 * the program should start to add note consonance ratings for that
	 * chord signature.
	 */
	private static final String chordSignatureSignal = "ChordSignature";
	/**
	 * String used to signify that an interval rating is on the line.
	 */
	private static final String intervalRatingSignal = "IntervalRating";
	/**
	 * String used to separate the ChordSignature and interval rating
	 * Signal from the data on the line.
	 */
	private static final String signalDataSeparator = ":";
	/**
	 * String used to separate the Interval data from the rating.
	 */
	private static final String intervalRatingSeparator = ",";

	/**
	 * Create a string used to signify that a chord is being declared.
	 * @param chordSig chord signature to be written to file.
	 * @return formatted string containing the chord signature preceded by a signalString
	 * to notify readers that the signature is on that line.
	 */
	static String createChordSignatureString(ChordSignature chordSig) {
		return chordSignatureSignal+
				signalDataSeparator+
				chordSig;
	}

	/**
	 * Parse a line of text containing the chordSignatureSignal and return a
	 * ChordSignature object following the signature.
	 * @param line line of text containing a chord signature
	 * @return chord signature contained in the line.
	 */
	static ChordSignature readChordSignatureFromLine(String line) {
		final int indexOfSignalDataSeparator = 
				line.indexOf(signalDataSeparator);
		final int indexOfFirstCharacterOfChordSignature = 
				indexOfSignalDataSeparator + 1;
		final String stringContainingChordSignature = 
				line.substring(indexOfFirstCharacterOfChordSignature);

		return ChordSignature.valueOf(
				stringContainingChordSignature);
	}

	/**
	 * Create a string that stores the rating for the note specified by the interval
	 * from the chords root.
	 * @param interval interval between chord's root note and the note being rated.
	 * @param rating rating assigned to the note
	 * @return formatted string containing the rating of the note/chord consonance
	 */
	static String createIntervalRatingString(Interval interval, ConsonanceRating rating) {
		return intervalRatingSignal + 
				signalDataSeparator + 
				interval + 
				intervalRatingSeparator + 
				rating;
	}

	/**
	 * Take a line of text containing an interval rating and return the interval.
	 * @param line line of text containing an interval rating
	 * @return the interval for the rating
	 */
	static Interval parseIntervalFromIntervalRatingLine(String line) {
		final int indexOfSignalDataSeparator = 
				line.indexOf(signalDataSeparator);
		final int indexOfFirstCharacterOfInterval =
				indexOfSignalDataSeparator + 1;
		final int indexOfIntervalRatingSeparator =
				line.indexOf(intervalRatingSeparator);


		//Get the substring between the signalData separator and the 
		//interval rating separator
		return Interval.valueOf(
				line.substring(
						indexOfFirstCharacterOfInterval ,
						indexOfIntervalRatingSeparator));
	}

	/**
	 * Take a line of text containing an interval rating and return
	 * the rating for the interval.
	 * @param line line of text containing an interval rating
	 * @return rating for the interval
	 */
	static  ConsonanceRating parseRatingFromIntervalRatingLine(String line) {
		final int indexOfIntervalRatingSeparator = 
				line.indexOf(intervalRatingSeparator);
		final int indexOfFirstCharacterOfRating =
				indexOfIntervalRatingSeparator + 1;

		return ConsonanceRating.valueOf(
				line.substring(indexOfFirstCharacterOfRating));
	}

	/**
	 * Print all values of the NoteConsonanceModel to the outputStream.
	 * This method assumes that all ratings have been added in the order
	 * of their declaration in the ChordSignature and Interval enums.
	 * 
	 * This method will also close the stream once data has been written.
	 * @param model the model to save to the stream
	 * @param outputStream destination of the model's data
	 */
	static void saveToStream(NoteConsonanceModel model, OutputStream outputStream) {
		if(model == null) {
			throw new NullPointerException("model may not be null");
		}
		if(outputStream == null) {
			throw new NullPointerException("outputStream may not be null.");
		}

		model.purgeMapsWithoutRatings();

		//create a writer that autoflushes lines.
		//TODO:may have to specify a charset as well.
		try(PrintWriter modelWriter = new PrintWriter(outputStream,true)){
			//let them know what type of file it is.
			modelWriter.println(noteConsonanceModelFileFormatIndicator);
			
			//write the data
			for(ChordSignature chordSig:model.chordToIntervalRatingMap.keySet()) {
				modelWriter.println(createChordSignatureString(chordSig));

				IntervalRatingMap ratingMap = 
						model.chordToIntervalRatingMap.get(chordSig);
				for(Interval interval:ratingMap.keySet()) {
					ConsonanceRating rating = ratingMap.get(interval);

					modelWriter.println(createIntervalRatingString(interval, rating));
				}
			}
		}
	}

	/**
	 * Take the given input stream representing a NoteConsonanceModel
	 * and return a decoded NoteConsonanceModel.
	 * @param inputStream stream of resource that was encoded using
	 * saveToStream
	 * @return a NoteConsonanceModel equivalent to the one encoded
	 * by saveToStream
	 */
	static NoteConsonanceModel loadFromStream(InputStream inputStream) {
		if(inputStream == null) {
			throw new NullPointerException("inputStream may not be null");
		}

		NoteConsonanceModel model = new NoteConsonanceModel();

		try (Scanner inputScanner = new Scanner(inputStream)) {
			String firstLineOfFile = inputScanner.nextLine();
			if( !firstLineOfFile.equals(noteConsonanceModelFileFormatIndicator)) {
				throw new FileFormatException();
			}
			//I have to assign a value to avoid compiler errors
			ChordSignature currentChordSignature = null;

			while(inputScanner.hasNextLine()) {

				String line = inputScanner.nextLine();
				if(line.contains(chordSignatureSignal)) {
					currentChordSignature = readChordSignatureFromLine(line);
				}else if( line.contains(intervalRatingSignal) ){
					Interval interval = parseIntervalFromIntervalRatingLine(line);
					ConsonanceRating rating = parseRatingFromIntervalRatingLine(line);
					model.addRating(currentChordSignature, interval, rating);
				}
			}
		}

		return model;
	}

	/**
	 * Save the given NoteConsonanceModel to the destination file.
	 * @param model model to be saved
	 * @param destinationFileName name of destination file
	 * @throws FileNotFoundException if the file is a directory or cannot
	 * be written to for some other reason
	 */
	public static void saveToFile(NoteConsonanceModel model, String destinationFileName) 
			throws FileNotFoundException {
		if(model == null) {
			throw new NullPointerException("model may not be null");
		}
		if(destinationFileName == null) {
			throw new NullPointerException("destination file name may not be null");
		}
		File destinationFile = new File(destinationFileName);

		saveToFile(model,destinationFile);
	}

	/**
	 * Save the given model to the destination file.
	 * @param model model to save
	 * @param destinationFile file to which we will save
	 * @throws FileNotFoundException if the file is a directory or cannot
	 * be written to for some other reason
	 */
	public static void saveToFile(NoteConsonanceModel model, File destinationFile) 
			throws FileNotFoundException {
		FileOutputStream destinationFileOutputStream = 
				new FileOutputStream(destinationFile);

		NoteConsonanceModel.saveToStream(model, destinationFileOutputStream);
	}

	/**
	 * Convert a file that has been used to store the data from a NoteConsonanceModel
	 * into a NoteConsonanceModel
	 * 
	 * @param sourceFileName name of file containing the data
	 * @return NoteConsonanceModel containing the stored data
	 * @throws FileNotFoundException if the file does not exist or 
	 * cannot be opened for some other reason.
	 */
	public static NoteConsonanceModel loadFromFile(String sourceFileName) 
			throws FileNotFoundException {
		if(sourceFileName == null) {
			throw new NullPointerException("sourceFileName may not be null.");
		}

		File sourceFile = new File(sourceFileName);
		return loadFromFile(sourceFile);
	}

	/**
	 * Create a NoteConsonanceModel from the source file.
	 * 
	 * @param sourceFile file containing NoteConsonance data
	 * @return initialized NoteConsonanceModel
	 * @throws FileNotFoundException if the file does not exist or 
	 * cannot be opened for some other reason.
	 */
	public static NoteConsonanceModel loadFromFile(File sourceFile) throws FileNotFoundException {
		FileInputStream sourceFileInputStream = 
				new FileInputStream(sourceFile);

		return NoteConsonanceModel.loadFromStream(sourceFileInputStream);
	}

	/**
	 * Main data structure containing the consonance rating between
	 * chords and notes represented by intervals.
	 */
	private Map<ChordSignature, IntervalRatingMap> chordToIntervalRatingMap;

	/**
	 * Create an empty NoteConsonanceModel.
	 */
	public NoteConsonanceModel() {
		this.chordToIntervalRatingMap = new HashMap<>();
	}

	/**
	 * Add the rating for the given chord signature and interval.
	 * @param chordSig the chord that the interval is being compared to.
	 * @param interval the interval between the root note of the chord to note whose
	 * consonance is being rated
	 * @param rating the rating of how consonant the chord is.
	 * @return the previous rating that existed for the chord signature and interval,
	 * null if no previous rating exists.
	 */
	ConsonanceRating addRating(
			ChordSignature chordSig, 
			Interval interval, 
			ConsonanceRating rating) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(interval == null) {
			throw new NullPointerException("Interval may not be null.");
		}
		if(!interval.inFirstOctave()) {
			throw new IllegalArgumentException("Interval must be between UNISON and MAJOR7 inclusive.");
		}
		if(rating == null) {
			throw new NullPointerException("Rating may not be null.");
		}

		IntervalRatingMap ratingMapForChordSignature = 
				chordToIntervalRatingMap.get(chordSig);

		if(ratingMapForChordSignature == null) {
			ratingMapForChordSignature = new IntervalRatingMap();
			chordToIntervalRatingMap.put(chordSig, ratingMapForChordSignature);
		}

		return ratingMapForChordSignature.put(interval,rating);
	}

	/**
	 * Remove the ConsonanceRating mapped to the given chordSignature and interval.
	 * @param chordSig ChordSignature from which the rating is being removed.
	 * @param interval the interval from which the rating is being removed.
	 * @return the rating that was removed if successful,
	 * null if the rating does not exist.
	 */
	ConsonanceRating removeRating(ChordSignature chordSig, Interval interval) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(interval == null) {
			throw new NullPointerException("Interval may not be null.");
		}
		if(!interval.inFirstOctave()) {
			throw new IllegalArgumentException("Interval must be between UNISON and MAJOR7 inclusive.");
		}

		IntervalRatingMap ratingMapForChordSignature = 
				chordToIntervalRatingMap.get(chordSig);

		return ratingMapForChordSignature == null? null : ratingMapForChordSignature.remove(interval);
	}

	/**
	 * Retrieve the rating for the given chord signature and interval.
	 * @param chordSig Chord signature for which the interval is rated
	 * @param interval interval between the root note of the chord and the note whose
	 * rating we are retrieving. 
	 * @return the rating for the parameters if it exists, 
	 * null otherwise
	 */
	ConsonanceRating getRating(ChordSignature chordSig, Interval interval) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(interval == null) {
			throw new NullPointerException("Interval may not be null.");
		}
		if(!interval.inFirstOctave()) {
			throw new IllegalArgumentException("Interval must be between UNISON and MAJOR7 inclusive.");
		}

		IntervalRatingMap ratingMapForChordSignature = 
				chordToIntervalRatingMap.get(chordSig);

		return ratingMapForChordSignature == null? null : ratingMapForChordSignature.get(interval);
	}

	/**
	 * Get a set of all chord signatures that have ratings. 
	 * @return set of all chord signatures with ratings.
	 */
	private Set<ChordSignature> getChordSignatureSet(){
		return this.chordToIntervalRatingMap.keySet();
	}

	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof NoteConsonanceModel))
			return false;

		NoteConsonanceModel other = (NoteConsonanceModel)o;

		//Because of removal of ratings there may be unfilled
		//rating maps in the data structure
		this.purgeMapsWithoutRatings();
		other.purgeMapsWithoutRatings();

		return this.chordToIntervalRatingMap.equals(other.chordToIntervalRatingMap);
	}

	/**
	 * Remove any intervalMaps for chord signatures with no ratings.
	 * 
	 * Thisis used to simplify the equals method and save/load methods.
	 */
	private void purgeMapsWithoutRatings() {
		List<ChordSignature> chordSignaturesWithoutRatings = new LinkedList<>();

		for(ChordSignature chordSig : chordToIntervalRatingMap.keySet()) {
			IntervalRatingMap ratingMapForChordSig = 
					chordToIntervalRatingMap.get(chordSig);
			if(ratingMapForChordSig.isEmpty()) {
				chordSignaturesWithoutRatings.add(chordSig);
			}
		}

		for(ChordSignature chordSig : chordSignaturesWithoutRatings) {
			chordToIntervalRatingMap.remove(chordSig);
		}

	}

	@Override
	public NoteConsonanceRecord addRating(NoteConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}if(!record.isRated()) {
			throw new IllegalArgumentException("Record must have a rating for the addRating method.");
		}

		ConsonanceRating rating = 
				addRating(
						record.chordSignature(),
						record.interval(),
						record.rating());
		if(rating == null) {
			return null;
		}else {
			return new NoteConsonanceRecord(
					record.chordSignature(),
					record.interval(),
					rating);
		}
	}

	@Override
	public NoteConsonanceRecord removeRating(NoteConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}
		ConsonanceRating rating = 
				removeRating(
						record.chordSignature(),
						record.interval());
		if(rating==null) {
			return null;
		}else {
			return new NoteConsonanceRecord(record.chordSignature(),record.interval(),rating);
		}
	}

	@Override
	public NoteConsonanceRecord getRating(NoteConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}

		ConsonanceRating rating = 
				getRating(
						record.chordSignature(),
						record.interval());
		if(rating==null) {
			return null;
		}else {
			return new NoteConsonanceRecord(record.chordSignature(),record.interval(),rating);
		}	
	}

	@Override
	public NoteConsonanceRecord getNextRecordToBeRated() {
		//we will iterate through all chord signatures until we find one
		//that does not have a rating...we cannot use the keyset since
		//the ordering of the chord signatures is not guaranteed

		for(ChordSignature chordSig : ChordSignature.values()) {
			IntervalRatingMap intervalMapForChordSig = 
					this.chordToIntervalRatingMap.get(chordSig);

			if(intervalMapForChordSig == null) {
				return new NoteConsonanceRecord(
						chordSig, 
						Interval.UNISON, 
						null);
			}
			for(Interval interval : Interval.values()) {
				//make sure that we aren't duplicating
				//interval chord relations by going to
				//the second octave
				if( !interval.inFirstOctave() ) {
					break;
				}

				if(getRating(chordSig, interval) == null) {
					//We are looking for the next chord/interval pair to be
					//rated so the ConsonanceRating is only added to
					//avoid a NullPointerException
					//may be a design flaw
					return new NoteConsonanceRecord(chordSig, interval, ConsonanceRating.BAD);
				}
			}
		}
		//if we reach here then every rating has been filled
		return null;
	}

	@Override
	public NoteConsonanceRecord getLastRecordRated() {
		ChordSignature chordSignatureOfLastRating,chordSignatureOfCurrentRating;
		Interval intervalOfLastRating,intervalOfCurrentRating;
		ConsonanceRating ratingOfLastRating,ratingOfCurrentRating;

		NoteConsonanceRecord currentNoteConsonanceRecordBeingRated = 
				getNextRecordToBeRated();

		//if all records have been filled return a record
		//with the last chord signature and last interval
		//possible
		if(currentNoteConsonanceRecordBeingRated == null)  {
			chordSignatureOfLastRating = 
					ChordSignature.lastSignature();
			intervalOfLastRating = 
					Interval.MAJOR7;
			ratingOfLastRating = getRating(chordSignatureOfLastRating, intervalOfLastRating);
			return new NoteConsonanceRecord(
					chordSignatureOfLastRating, 
					intervalOfLastRating,
					ratingOfLastRating);
		}

		chordSignatureOfCurrentRating = 
				currentNoteConsonanceRecordBeingRated.chordSignature();
		intervalOfCurrentRating = 
				currentNoteConsonanceRecordBeingRated.interval();
		ratingOfCurrentRating = 
				currentNoteConsonanceRecordBeingRated.rating();

		//if there have not been any ratings added yet then return a null value
		if( chordSignatureOfCurrentRating.isFirstSignature() &&
				intervalOfCurrentRating.equals(Interval.UNISON)) {
			return null;
		}

		//if we reach this point then the last rating is somewhere in the middle of 
		//the total list of chord signatures and intervals
		//
		//if the interval we are currenlty working on is UNISON then
		//the previous rating was on the previous ChordSignature.
		if(intervalOfCurrentRating.equals(Interval.UNISON)) {
			chordSignatureOfLastRating = 
					chordSignatureOfCurrentRating.getPreviousChordSignature();
			intervalOfLastRating = 
					Interval.MAJOR7;
			ratingOfLastRating = 
					getRating(chordSignatureOfLastRating, intervalOfLastRating);


			return new NoteConsonanceRecord(
					chordSignatureOfLastRating,
					intervalOfLastRating,
					ratingOfLastRating);
		}

		chordSignatureOfLastRating =
				chordSignatureOfCurrentRating;
		intervalOfLastRating = 
				intervalOfCurrentRating.getPreviousInterval();
		ratingOfLastRating = 
				getRating(chordSignatureOfLastRating, intervalOfLastRating);

		return new NoteConsonanceRecord(
				chordSignatureOfLastRating,
				intervalOfLastRating,
				ratingOfLastRating);
	}

	@Override
	public boolean isFull() {
		return getNextRecordToBeRated() == null;
	}

	@Override
	public boolean isEmpty() {
		purgeMapsWithoutRatings();
		return this.chordToIntervalRatingMap.size() == 0;
	}

	@Override
	public Set<NoteConsonanceRecord> getRecords(NoteConsonanceRecordRequest request) {
		if(request == null) {
			throw new NullPointerException("request may not be null");
		}
		//TODO: we might have to change this to a checked exception
		if( !request.isInitialized()) {
			throw new IllegalArgumentException("Request has not been properly initialized");
		}
		
		Set<NoteConsonanceRecord> matchingRecords = new HashSet<>();
		
		for(ChordSignature chordSig : this.chordToIntervalRatingMap.keySet()) {
			//skip any chords that we are not looking for.
			if( !request.contains(chordSig)) {
				continue;
			}
			
			IntervalRatingMap ratingMap = this.chordToIntervalRatingMap.get(chordSig);
			for(Interval interval : ratingMap.keySet()) {
				//skip intervals that we are not looking for
				if( !request.contains(interval)) {
					continue;
				}
				ConsonanceRating rating = ratingMap.get(interval);
				
				//skip ratings we are not looking for
				if( !request.contains(rating)) {
					continue;
				}
				
				NoteConsonanceRecord recordToAdd = 
						new NoteConsonanceRecord(chordSig, interval, rating);
				matchingRecords.add(recordToAdd);
			}
		}
		
		return matchingRecords;
	}
}