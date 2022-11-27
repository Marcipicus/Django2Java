package chord.relations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;

/**
 * Main data structure for rating chord changes.
 * @author DAD
 *
 */
public class ChordChangeConsonanceModel implements RatingModel<ChordChangeConsonanceRecord>{

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

		FileOutputStream destinationFileOutputStream = 
				new FileOutputStream(destinationFileName);

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

		FileInputStream sourceFileInputStream = 
				new FileInputStream(sourceFileName);

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
	public ConsonanceRating addRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots, ConsonanceRating rating) {
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
	public ConsonanceRating removeRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots) {
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
	public ConsonanceRating getRating(ChordSignature startChordSig, ChordSignature endChordSig, Interval intervalBetweenRoots) {
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
	public Set<ChordSignature> getStartChordSignatureSet(){
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
	
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof ChordChangeConsonanceModel))
			return false;
		
		ChordChangeConsonanceModel other = (ChordChangeConsonanceModel)o;
		
		return this.chordChangeConsonanceMap.equals(other.chordChangeConsonanceMap);
	}

	@Override
	public ChordChangeConsonanceRecord addRating(ChordChangeConsonanceRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChordChangeConsonanceRecord removeRating(ChordChangeConsonanceRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChordChangeConsonanceRecord getRating(ChordChangeConsonanceRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChordChangeConsonanceRecord getNextRecordToBeRated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChordChangeConsonanceRecord getLastRecordRated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFull() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
}
