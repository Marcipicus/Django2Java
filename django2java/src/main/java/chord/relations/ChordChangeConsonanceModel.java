package chord.relations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
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
import chord.relations.record.ChordChangeConsonanceRecord;
import chord.relations.request.ChordChangeConsonanceRecordRequest;

/**
 * Main data structure for rating chord changes.
 * @author DAD
 *
 */
public class ChordChangeConsonanceModel implements RatingModel<ChordChangeConsonanceRecord,ChordChangeConsonanceRecordRequest>{

	//TODO: Create methods to write and read to/from file for ChordChangeConsonanceModel
	//
	//also create method to find the last combination that was rated
	//or the next one that needs to be rated.

	/**
	 * String used to signify that the start chord signature is on the line and
	 * the program should start to add chord change consonance ratings for that
	 * chord signature.
	 */
	private static final String startChordSignatureSignal = "StartChordSignature";

	/**
	 * String used to signify that the end chord signature is on the line and
	 * the program should start to add chord change consonance ratings for that
	 * chord signature.
	 */
	private static final String endChordSignatureSignal = "EndChordSignature";

	/**
	 * String used to signal that there is a chord change rating for the
	 * given interval.
	 */
	private static final String intervalRatingSignal = "IntervalRating";

	/**
	 * String used to separate the ChordSignature and scale rating
	 * Signal from the data on the line.
	 */
	private static final String signalDataSeparator = ":";

	/**
	 * String used to separate the interval being rated and the 
	 * consonance rating.
	 */
	private static final String intervalRatingSeparator = ",";

	/**
	 * Create a string storing the given chord signature with the
	 * signal requested using the isStartSignature argument
	 * @param isStartSignature create a start signature string if true
	 * create an end chord signature string if false
	 * @param chordSig the chord Signature to encode.
	 * @return a string with the requested signal and the given chord signature
	 */
	static String createChordSignatureString(boolean isStartSignature, ChordSignature chordSig) {
		final String chordSignatureSignal = 
				isStartSignature?startChordSignatureSignal:endChordSignatureSignal;
		return chordSignatureSignal+
				signalDataSeparator+
				chordSig;
	}

	/**
	 * Read the chord signature from a line of text.
	 * 
	 * No need to figure out if it is a start or end chord
	 * since the test is done externally.
	 * @param line line of text to extract chord signature from
	 * @return chord signature on the line.
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
	 * Write a string of text containing the interval rating signal,
	 * an interval, and a rating.
	 * @param interval interval to be rated.
	 * @param rating rating for the chord change represented by the interval
	 * @return line of text containing the interval, rating, and a signal.
	 */
	static String createIntervalRatingString(Interval interval, ConsonanceRating rating) {
		return intervalRatingSignal + 
				signalDataSeparator + 
				interval + 
				intervalRatingSeparator + 
				rating;
	}

	/**
	 * Take a line of text encoded by createIntervalRatingString and 
	 * extract the interval from it.
	 * @param line line of text encoded by createIntervalRatingString
	 * @return interval that was encoded in the line
	 */
	static Interval parseIntervalFromIntervalRatingLine(String line) {
		final int indexOfSignalDataSeparator = 
				line.indexOf(signalDataSeparator);
		final int indexOfFirstCharacterOfScaleSignature =
				indexOfSignalDataSeparator + 1;
		final int indexOfIntervalRatingSeparator =
				line.indexOf(intervalRatingSeparator);


		//Get the substring between the signalData separator and the interval rating separator
		return Interval.valueOf(
				line.substring(
						indexOfFirstCharacterOfScaleSignature ,
						indexOfIntervalRatingSeparator));
	}

	/**
	 * Take a line of text encoded by createIntervalRatingString and 
	 * extract the rating from it.
	 * @param line line of text encoded by createIntervalRatingString
	 * @return Rating encoded in the string.
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
	 * Take the given model and write it to file.
	 * @param model model to save, cannot be null
	 * @param outputStream stream to save the data to.
	 */
	static void saveToStream(ChordChangeConsonanceModel model, OutputStream outputStream) {
		if(model == null) {
			throw new NullPointerException("model may not be null");
		}
		if(outputStream == null) {
			throw new NullPointerException("outputStream may not be null.");
		}

		//There may be a great number of values in the collection so we
		//are not going to autoflush
		//TODO:may have to specify a charset as well.
		try(PrintWriter modelWriter = new PrintWriter(outputStream,false)){
			for(ChordSignature startChordSig : model.getStartChordSignatureSet()) {
				modelWriter.println(createChordSignatureString(true,startChordSig));

				for(ChordSignature endChordSig : 
					model.getEndChordSignatureSetForStartChordSignature(startChordSig)) {
					modelWriter.println(createChordSignatureString(false, endChordSig));

					IntervalRatingMap intervalRatingMap = 
							model.chordChangeConsonanceMap.get(startChordSig).get(endChordSig);
					for(Interval interval : intervalRatingMap.keySet()) {
						modelWriter.println(
								createIntervalRatingString(
										interval, 
										intervalRatingMap.get(interval)));
					}
				}
			}
		}
	}

	/**
	 * Create a ChordChangeConsonanceModel using the data from the inputStream.
	 * @param inputStream stream to read data from.
	 * @return ChordChangeConsonanceModel containing all data saved to the resource
	 * with which the stream is associated.
	 */
	static ChordChangeConsonanceModel loadFromStream(InputStream inputStream) {
		if(inputStream == null) {
			throw new NullPointerException("inputStream may not be null");
		}

		ChordChangeConsonanceModel model = new ChordChangeConsonanceModel();

		try (Scanner inputScanner = new Scanner(inputStream)) {
			//I have to assign a value to avoid compiler errors
			ChordSignature 
			currentStartChordSignature = null,
			currentEndChordSignature = null;

			while(inputScanner.hasNextLine()) {

				String line = inputScanner.nextLine();
				if(line.contains(startChordSignatureSignal)) {
					currentStartChordSignature = readChordSignatureFromLine(line);
				}else if(line.contains(endChordSignatureSignal)){
					currentEndChordSignature = readChordSignatureFromLine(line);
				}else if( line.contains(intervalRatingSignal) ){
					Interval interval = parseIntervalFromIntervalRatingLine(line);
					ConsonanceRating rating = parseRatingFromIntervalRatingLine(line);
					model.addRating(currentStartChordSignature, currentEndChordSignature, interval, rating);
				}
			}
		}

		return model;
	}

	/**
	 * Take the given model and save it to file.
	 * 
	 * @param model model to save
	 * @param destinationFileName name of destination file
	 * @throws FileNotFoundException if there is an error saving due to
	 * specifying the name of a directory or there are insufficient permissions
	 * to create/write file
	 */
	public static void saveToFile(ChordChangeConsonanceModel model, String destinationFileName) throws FileNotFoundException {
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
	public static void saveToFile(ChordChangeConsonanceModel model, File destinationFile) 
			throws FileNotFoundException {
		FileOutputStream destinationFileOutputStream = 
				new FileOutputStream(destinationFile);

		ChordChangeConsonanceModel.saveToStream(model, destinationFileOutputStream);
	}

	/**
	 * Load the consonance model from the file.
	 * @param sourceFileName name of file from which we are to read
	 * @return model containing the same data that was saved from saveToFile
	 * @throws FileNotFoundException if there is an error saving due to
	 * specifying the name of a directory or there are insufficient permissions
	 * to create/write file
	 */
	public static ChordChangeConsonanceModel loadFromFile(String sourceFileName) throws FileNotFoundException {
		if(sourceFileName == null) {
			throw new NullPointerException("sourceFileName may not be null.");
		}
		
		File sourceFile = new File(sourceFileName);

		return ChordChangeConsonanceModel.loadFromFile(sourceFile);
	}
	
	/**
	 * Create a ChordChangeConsonanceModel from the source file.
	 * 
	 * @param sourceFile file containing ChordChangeConsonance data
	 * @return initialized ChordChangeConsonanceModel
	 * @throws FileNotFoundException if the file does not exist or 
	 * cannot be opened for some other reason.
	 */
	public static ChordChangeConsonanceModel loadFromFile(File sourceFile) throws FileNotFoundException {
		FileInputStream sourceFileInputStream = 
				new FileInputStream(sourceFile);

		return ChordChangeConsonanceModel.loadFromStream(sourceFileInputStream);
	}

	private Map<ChordSignature,Map<ChordSignature,IntervalRatingMap>> chordChangeConsonanceMap;

	/**
	 * Create a new ChordChangeConsonanceModel.
	 */
	public ChordChangeConsonanceModel() {
		this.chordChangeConsonanceMap = new HashMap<>();
	}

	/**
	 * Add a rating for the given start chord, end chord, and relation between roots.
	 * @param startChordSig the start chord signature
	 * @param endChordSig the end chord signature
	 * @param intervalBetweenRoots interval between the roots of the start chord and end chord.
	 * @param rating rating to be added
	 * @return the previous rating for the parameters,
	 * null if there was no previous rating.
	 */
	 ConsonanceRating addRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots, ConsonanceRating rating) {
		if(startChordSig == null) {
			throw new NullPointerException("start chord sig may not be null");
		}
		if(endChordSig == null) {
			throw new NullPointerException("end chord sig may not be null");
		}
		if(intervalBetweenRoots == null) {
			throw new NullPointerException("interval between roots may not be null.");
		}
		if( !intervalBetweenRoots.inFirstOctave()) {
			throw new IllegalArgumentException("Interval between roots must be between UNISON and MAJOR7 inclusive");
		}
		if(rating == null) {
			throw new NullPointerException("rating must not be null");
		}
		//if the start and end chords are the same and the interval between roots is unison,
		//then the two chords are exactly the same and should not be rated.
		//TODO: I might throw a created exception here so the calling function can tell
		//that the rating wasn't added and i don't have to confuse the return values of
		//the function
		if(startChordSig.equals(endChordSig) && intervalBetweenRoots.equals(Interval.UNISON)){
			throw new IllegalArgumentException("");
		}

		//I have to grab each map one at a time so that I don't
		//dereference a null pointer
		Map<ChordSignature,IntervalRatingMap> startChordToEndChordRatingMap =
				chordChangeConsonanceMap.get(startChordSig);

		if(startChordToEndChordRatingMap == null) {
			startChordToEndChordRatingMap = new HashMap<>();
			chordChangeConsonanceMap.put(startChordSig, startChordToEndChordRatingMap);
		}

		IntervalRatingMap endChordIntervalRatingMap = 
				startChordToEndChordRatingMap.get(endChordSig);

		if(endChordIntervalRatingMap == null) {
			endChordIntervalRatingMap = new IntervalRatingMap();
			startChordToEndChordRatingMap.put(endChordSig, endChordIntervalRatingMap);
		}

		return endChordIntervalRatingMap.put(intervalBetweenRoots, rating);
	}

	/**
	 * Remove a rating for the given parameters.
	 * @param startChordSig start chord signature
	 * @param endChordSig end chord signature
	 * @param intervalBetweenRoots interval between the roots of the two chords.
	 * @return the rating associated with the parameters, or null if no
	 * rating exists.
	 */
	ConsonanceRating removeRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots) {
		if(startChordSig == null) {
			throw new NullPointerException("start chord sig may not be null");
		}
		if(endChordSig == null) {
			throw new NullPointerException("end chord sig may not be null");
		}
		if(intervalBetweenRoots == null) {
			throw new NullPointerException("interval between roots may not be null.");
		}
		if( !intervalBetweenRoots.inFirstOctave()) {
			throw new IllegalArgumentException("Interval between roots must be between UNISON and MAJOR7 inclusive");
		}

		//Have to get each map one at a time so that we don't
		//dereference a null pointer.
		Map<ChordSignature,IntervalRatingMap> endChordSigToRatingMap =
				this.chordChangeConsonanceMap.get(startChordSig);
		if(endChordSigToRatingMap == null) {
			return null;
		}

		IntervalRatingMap chordChangeIntervalRatingMap = 
				endChordSigToRatingMap.get(endChordSig);
		if(chordChangeIntervalRatingMap == null) {
			return null;
		}

		return chordChangeIntervalRatingMap.remove(intervalBetweenRoots);
	}

	/**
	 * Get the rating for the given parameters.
	 * @param startChordSig the start chord signature
	 * @param endChordSig the end chord signature
	 * @param intervalBetweenRoots interval between the roots of the two chords
	 * @return the rating associated with the parameters,
	 * null if no rating exists
	 */
	ConsonanceRating getRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots) {
		if(startChordSig == null) {
			throw new NullPointerException("start chord sig may not be null");
		}
		if(endChordSig == null) {
			throw new NullPointerException("end chord sig may not be null");
		}
		if(intervalBetweenRoots == null) {
			throw new NullPointerException("interval between roots may not be null.");
		}
		if( !intervalBetweenRoots.inFirstOctave()) {
			throw new IllegalArgumentException("Interval between roots must be between UNISON and MAJOR7 inclusive");
		}

		//Have to get each map one at a time so that we don't
		//dereference a null pointer.
		Map<ChordSignature,IntervalRatingMap> endChordSigToRatingMap =
				this.chordChangeConsonanceMap.get(startChordSig);
		if(endChordSigToRatingMap == null) {
			return null;
		}

		IntervalRatingMap chordChangeIntervalRatingMap = 
				endChordSigToRatingMap.get(endChordSig);
		if(chordChangeIntervalRatingMap == null) {
			return null;
		}

		return chordChangeIntervalRatingMap.get(intervalBetweenRoots);
	}

	/**
	 * Get a set of all start chord signatures that have ratings. 
	 * @return set of all start chord signatures with ratings.
	 */
	private Set<ChordSignature> getStartChordSignatureSet(){
		return this.chordChangeConsonanceMap.keySet();
	}

	/**
	 * Get the set of all endchord ratings for the given start chord.
	 * 
	 * @param startChordSig chord for which we are retrieving end chord
	 * ratings. 
	 * @return a set of all end chords that ave been saved and are associated
	 * with the start chord. an empty set if no such ratings exist
	 */
	private Set<ChordSignature> getEndChordSignatureSetForStartChordSignature(
			ChordSignature startChordSig){
		if(startChordSig == null) {
			throw new NullPointerException("start chord signature may not be null");
		}

		Map<ChordSignature,IntervalRatingMap> endChordMapForStartChordSig = 
				this.chordChangeConsonanceMap.get(startChordSig);

		if(endChordMapForStartChordSig == null) {
			return Collections.emptySet();
		}

		return endChordMapForStartChordSig.keySet();
	}

	/**
	 * Remove any empty maps to simplify the equals and isEmpty methods.
	 */
	private void purgeUnusedMaps() {
		//Remove any end maps that have no ratings
		purgeUnusedEndChordMaps();

		//Remove any start chords that no longer have any end chords
		//because of the previous removal
		purgeEmptyStartChordMaps();
	}

	/**
	 * Remove any endChordMaps that no longer have any ratings.
	 */
	private void purgeUnusedEndChordMaps() {
		List<ChordChangeConsonanceRecord> recordsWithNoRatings = new LinkedList<>();

		for(ChordSignature startChord : getStartChordSignatureSet()) {
			for(ChordSignature endChord : getEndChordSignatureSetForStartChordSignature(startChord)) {
				IntervalRatingMap intRatingMap = 
						this.chordChangeConsonanceMap.get(startChord).get(endChord);
				if(intRatingMap.keySet().size() == 0) {
					//Save the startChordSignature and the endChordSignature
					//in a record for later....interval is ignored.
					recordsWithNoRatings.add(
							new ChordChangeConsonanceRecord(
									startChord, 
									endChord, 
									Interval.MAJOR3, 
									null));
				}
			}
		}

		//remove the endChords from the map
		for(ChordChangeConsonanceRecord record : recordsWithNoRatings) {
			this.chordChangeConsonanceMap.get(record.startChordSignature()).remove(record.endChordSignature());
		}
	}

	/**
	 * Remove any maps for start chords that no longer have any
	 * associated end chords because of a removal by purgeUnusedEndChordMaps
	 */
	private void purgeEmptyStartChordMaps() {
		List<ChordSignature> startChordsWithNoEndChordMaps = new LinkedList<>();

		for(ChordSignature startChord : getStartChordSignatureSet()) {
			Map<ChordSignature,IntervalRatingMap> endChordMap = 
					this.chordChangeConsonanceMap.get(startChord);

			if(endChordMap.keySet().size() == 0) {
				startChordsWithNoEndChordMaps.add(startChord);
			}
		}

		for(ChordSignature chordSig : startChordsWithNoEndChordMaps) {
			this.chordChangeConsonanceMap.remove(chordSig);
		}
	}


	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof ChordChangeConsonanceModel))
			return false;

		ChordChangeConsonanceModel other = (ChordChangeConsonanceModel)o;
		this.purgeUnusedMaps();
		other.purgeUnusedMaps();

		return this.chordChangeConsonanceMap.equals(other.chordChangeConsonanceMap);
	}

	@Override
	public ChordChangeConsonanceRecord addRating(ChordChangeConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}
		ConsonanceRating rating = addRating(
				record.startChordSignature(),
				record.endChordSignature(), 
				record.intervalBetweenRoots(), 
				record.rating());
		if(rating == null) {
			return null;
		}else {
			return new ChordChangeConsonanceRecord(
					record.startChordSignature(), 
					record.endChordSignature(), 
					record.intervalBetweenRoots(), 
					rating);
		}
	}

	@Override
	public ChordChangeConsonanceRecord removeRating(ChordChangeConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}
		ConsonanceRating rating = removeRating(
				record.startChordSignature(),
				record.endChordSignature(), 
				record.intervalBetweenRoots());
		if(rating == null) {
			return null;
		}else {
			return new ChordChangeConsonanceRecord(
					record.startChordSignature(), 
					record.endChordSignature(), 
					record.intervalBetweenRoots(), 
					rating);
		}
	}

	@Override
	public ChordChangeConsonanceRecord getRating(ChordChangeConsonanceRecord record) {
		if(record == null) {
			throw new NullPointerException("record may not be null");
		}
		ConsonanceRating rating = getRating(
				record.startChordSignature(),
				record.endChordSignature(), 
				record.intervalBetweenRoots());
		if(rating == null) {
			return null;
		}else {
			return new ChordChangeConsonanceRecord(
					record.startChordSignature(), 
					record.endChordSignature(), 
					record.intervalBetweenRoots(), 
					rating);
		}
	}

	@Override
	public ChordChangeConsonanceRecord getNextRecordToBeRated() {
		if(isFull()) {
			return null;
		}

		for(ChordSignature startChordSig : ChordSignature.values()) {
			for(ChordSignature endChordSig : ChordSignature.values()) {
				for(Interval interval : Interval.values()) {
					//Make sure we aren't duplicating ratings
					//PERFECT8 refers to the same note as UNISON
					if( !interval.inFirstOctave() ) {
						break;
					}
					//if the start and end chord are the same and
					//the interval is UNISON then the combination
					//represents the same chord
					if(startChordSig.equals(endChordSig) && interval.equals(Interval.UNISON)) {
						continue;
					}
					ConsonanceRating rating = getRating(startChordSig, endChordSig, interval);
					if(rating == null) {
						return new ChordChangeConsonanceRecord(startChordSig, endChordSig, interval, null);
					}
				}
			}
		}

		//this is redundant...I suppose that the 
		//check at the start is unnecessary
		//may have to change this in the future
		return null;
	}

	@Override
	public ChordChangeConsonanceRecord getLastRecordRated() {
		//This check isn't really necessary
		//it works for now...maybe remove it later
		if(isEmpty()) {
			return null;
		}

		ChordChangeConsonanceRecord lastRecordRated = null;

		OUTER_LOOP:
			for(ChordSignature startChord : ChordSignature.values()) {
				for(ChordSignature endChord : ChordSignature.values()) {
					for(Interval interval : Interval.values()) {
						//Make sure we aren't duplicating ratings
						//PERFECT8 refers to the same note as UNISON
						if( !interval.inFirstOctave() ) {
							break;
						}
						//if the start and end chord are the same and
						//the interval is UNISON then the combination
						//represents the same chord
						if(startChord.equals(endChord) && interval.equals(Interval.UNISON)) {
							continue;
						}

						ConsonanceRating rating = getRating(startChord, endChord, interval);
						if(rating == null) {
							break OUTER_LOOP;
						}
						lastRecordRated = new ChordChangeConsonanceRecord(startChord, endChord, interval, rating);
					}
				}
			}

		return lastRecordRated;
	}

	@Override
	public boolean isFull() {
		
		//TODO: These loops are pretty much the same
		//we might need to parameterize this later with
		//delegates or lambda expressions....works for now
		for(ChordSignature startChord : ChordSignature.values()) {
			for(ChordSignature endChord : ChordSignature.values()) {
				for(Interval interval : Interval.values()) {
					//Make sure we aren't duplicating ratings
					//PERFECT8 refers to the same note as UNISON
					if( !interval.inFirstOctave() ) {
						break;
					}
					//if the start and end chord are the same and
					//the interval is UNISON then the combination
					//represents the same chord
					if(startChord.equals(endChord) && interval.equals(Interval.UNISON)) {
						continue;
					}

					ConsonanceRating rating = getRating(startChord, endChord, interval);
					if(rating == null) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		purgeUnusedMaps();
		return this.chordChangeConsonanceMap.keySet().size() == 0;
	}

	@Override
	public Set<ChordChangeConsonanceRecord> getRecords(ChordChangeConsonanceRecordRequest request) {
		if(request == null) {
			throw new NullPointerException("request may not be null.");
		}
		if( !request.isInitialized() ) {
			throw new IllegalStateException("request is not initialized");
		}
		//purge unused maps to simplify the request process
		//by not checking for null values
		purgeUnusedMaps();
		
		Set<ChordChangeConsonanceRecord> recordsRequested = new HashSet<>();
		
		//reference chord loop
		for(ChordSignature referenceChordSig : this.chordChangeConsonanceMap.keySet()) {
			if( ! request.containsReferenceChord(referenceChordSig)) {
				continue;
			}
			
			//no need to check for nulls in these loops since we have already
			//purged all unused maps and ratings
			Map<ChordSignature,IntervalRatingMap> targetChordToIntervalMap =
					this.chordChangeConsonanceMap.get(referenceChordSig);

			for(ChordSignature targetChordSig : targetChordToIntervalMap.keySet()) {
				if( !request.containsTargetChord(targetChordSig)) {
					continue;
				}
				
				IntervalRatingMap intervalRatingMap = 
						targetChordToIntervalMap.get(targetChordSig);
				
				for(Interval interval : intervalRatingMap.keySet()) {
					if( !request.containsIntervalBetweenRoots(interval)) {
						continue;
					}
					
					ConsonanceRating rating = intervalRatingMap.get(interval);
					if( !request.contains(rating)) {
						continue;
					}
					
					recordsRequested.add(
							new ChordChangeConsonanceRecord(
									referenceChordSig, 
									targetChordSig, 
									interval, 
									rating));
				}
			}
		}
		return recordsRequested;
	}
}
