package chord.relations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import chord.ConsonanceRating;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;

/**
 * Main data structure for rating the consonance of chords to scales.
 * @author DAD
 *
 */
public class ScaleConsonanceModel implements RatingModel<ScaleConsonanceRecord>{

	/**
	 * String used to signify that a chord signature is on the line and
	 * the program should start to add note consonance ratings for that
	 * chord signature.
	 */
	private static final String chordSignatureSignal = "ChordSignature";
	/**
	 * String used to signify that a scale rating is on the line.
	 */
	private static final String scaleRatingSignal = "ScaleRating";
	/**
	 * String used to separate the ChordSignature and scale rating
	 * Signal from the data on the line.
	 */
	private static final String signalDataSeparator = ":";
	/**
	 * String used to separate the scale signature from the rating
	 */
	private static final String scaleRatingSeparator = ",";

	
	static String createChordSignatureString(ChordSignature chordSig) {
		return chordSignatureSignal+
				signalDataSeparator+
				chordSig;
	}

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

	static String createScaleRatingString(ScaleSignature scaleSig, ConsonanceRating rating) {
		return scaleRatingSignal + 
				signalDataSeparator + 
				scaleSig + 
				scaleRatingSeparator + 
				rating;
	}

	static ScaleSignature parseScaleSignatureFromScaleRatingLine(String line) {
		final int indexOfSignalDataSeparator = 
				line.indexOf(signalDataSeparator);
		final int indexOfFirstCharacterOfScaleSignature =
				indexOfSignalDataSeparator + 1;
		final int indexOfIntervalRatingSeparator =
				line.indexOf(scaleRatingSeparator);
		
		
		//Get the substring between the signalData separator and the interval rating separator
		return ScaleSignature.valueOf(
				line.substring(
						indexOfFirstCharacterOfScaleSignature ,
						indexOfIntervalRatingSeparator));
	}

	/**
	 * Take a line of text containing a scale rating and return
	 * the rating for the scale.
	 * @param line line of text containing an interval rating
	 * @return rating for the interval
	 */
	static  ConsonanceRating parseRatingFromScaleRatingLine(String line) {
		final int indexOfScaleRatingSeparator = 
				line.indexOf(scaleRatingSeparator);
		final int indexOfFirstCharacterOfRating =
				indexOfScaleRatingSeparator + 1;
		
		return ConsonanceRating.valueOf(
				line.substring(indexOfFirstCharacterOfRating));
	}

	/**
	 * Take the given data model and save it to the given stream.
	 * @param model model to save
	 * @param outputStream destnation stream
	 */
	static void saveToStream(ScaleConsonanceModel model, OutputStream outputStream) {
		if(model == null) {
			throw new NullPointerException("model may not be null");
		}
		if(outputStream == null) {
			throw new NullPointerException("outputStream may not be null.");
		}
		
		//make sure that there are no chords with empty scale ratings
		//this needs to be done in case there are scale ratings removed 
		//but the map still exists
		model.purgeUnratedScales();

		//create a writer that autoflushes lines.
		//TODO:may have to specify a charset as well.
		try(PrintWriter modelWriter = new PrintWriter(outputStream,true)){
			
			for(ChordSignature chordSig : model.getChordSignatureSet()) {
				modelWriter.println(createChordSignatureString(chordSig));
				
				Map<ScaleSignature,ConsonanceRating> scaleRatingMap = 
						model.chordToScaleRatingMap.get(chordSig);
				
				for(ScaleSignature scaleSig : scaleRatingMap.keySet()) {
					ConsonanceRating rating = scaleRatingMap.get(scaleSig);
					modelWriter.println(createScaleRatingString(
							scaleSig, 
							rating));
				}
			}
		}
	}

	/**
	 * Read a model from the given stream and return a model containing
	 * the data contained in it.
	 * 
	 * @param inputStream source data stream
	 * @return model containing the data from the stream
	 */
	static ScaleConsonanceModel loadFromStream(InputStream inputStream) {
		if(inputStream == null) {
			throw new NullPointerException("inputStream may not be null");
		}

		ScaleConsonanceModel model = new ScaleConsonanceModel();

		try (Scanner inputScanner = new Scanner(inputStream)) {
			//I have to assign a value to avoid compiler errors
			ChordSignature currentChordSignature = null;

			while(inputScanner.hasNextLine()) {

				String line = inputScanner.nextLine();
				if(line.contains(chordSignatureSignal)) {
					currentChordSignature = readChordSignatureFromLine(line);
				}else if( line.contains(scaleRatingSignal) ){
					ScaleSignature scaleSig = parseScaleSignatureFromScaleRatingLine(line);
					ConsonanceRating rating = parseRatingFromScaleRatingLine(line);
					model.addRating(currentChordSignature, scaleSig, rating);
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
	public static void saveToFile(ScaleConsonanceModel model, String destinationFileName) throws FileNotFoundException {
		if(model == null) {
			throw new NullPointerException("model may not be null");
		}
		if(destinationFileName == null) {
			throw new NullPointerException("destination file name may not be null");
		}
		
		FileOutputStream destinationFileOutputStream = 
				new FileOutputStream(destinationFileName);
		
		ScaleConsonanceModel.saveToStream(model, destinationFileOutputStream);
	}
	
	/**
	 * Convert a file that has been used to store the data from a NoteConsonanceModel
	 * into a NoteConsonanceModel
	 * 
	 * @param sourceFileName name of file containing the data
	 * @return NoteConsonanceModel containing the stored data
	 * @throws FileNotFoundException
	 */
	public static ScaleConsonanceModel loadFromFile(String sourceFileName) throws FileNotFoundException {
		if(sourceFileName == null) {
			throw new NullPointerException("sourceFileName may not be null.");
		}
		
		FileInputStream sourceFileInputStream = 
				new FileInputStream(sourceFileName);
		
		return ScaleConsonanceModel.loadFromStream(sourceFileInputStream);
	}
	
	private Map<ChordSignature, Map<ScaleSignature,ConsonanceRating>> chordToScaleRatingMap;
	
	/**
	 * Create an empty ScaleConsonanceModel.
	 */
	public ScaleConsonanceModel() {
		this.chordToScaleRatingMap = new HashMap<>();
	}
	
	/**
	 * Add the rating for the given chord signature and scale signature
	 * @param chordSig the chord that the scale signature is being compared to
	 * @param scaleSig the scale signature being compared to the chord
	 * @param rating the rating of how consonant the chord and scale signatures are
	 * when compared to each other.
	 * @return the previous rating mapped to the chord and scale signatures,
	 * null if no previous rating exists.
	 */
	public ConsonanceRating addRating(ChordSignature chordSig, ScaleSignature scaleSig, ConsonanceRating rating) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(scaleSig == null) {
			throw new NullPointerException("Scale Signature may not be null.");
		}
		if(rating == null) {
			throw new NullPointerException("Rating may not be null.");
		}
		Map<ScaleSignature,ConsonanceRating> ratingMapForChordSignature = 
				chordToScaleRatingMap.get(chordSig);

		if(ratingMapForChordSignature == null) {
			ratingMapForChordSignature = new HashMap<>();
			chordToScaleRatingMap.put(chordSig, ratingMapForChordSignature);
		}
		
		return ratingMapForChordSignature.put(scaleSig,rating);
	}
	
	/**
	 * Remove the ConsonanceRating mapped to the given chordSignature and scale signature.
	 * @param chordSig ChordSignature from which the rating is being removed.
	 * @param scaleSig the scaleSignature from which the rating is being removed.
	 * @return the rating that was removed if successful,
	 * null if the rating does not exist.
	 */
	public ConsonanceRating removeRating(ChordSignature chordSig, ScaleSignature scaleSig) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(scaleSig == null) {
			throw new NullPointerException("scale signature may not be null.");
		}

		Map<ScaleSignature,ConsonanceRating> ratingMapForChordSignature = 
				chordToScaleRatingMap.get(chordSig);

		return ratingMapForChordSignature == null? null : ratingMapForChordSignature.remove(scaleSig);
	}
	
	/**
	 * Retrieve the rating for the given chord signature and scaleSignature.
	 * @param chordSig Chord signature for which the rating is being retrieved
	 * @param scaleSig scaleSignature for which the rating is being retrieved
	 * @return the rating for the parameters if it exists, 
	 * null otherwise
	 */
	public ConsonanceRating getRating(ChordSignature chordSig, ScaleSignature scaleSig) {
		if(chordSig == null) {
			throw new NullPointerException("ChordSignature may not be null.");
		}
		if(scaleSig == null) {
			throw new NullPointerException("Scale signature may not be null.");
		}
		
		Map<ScaleSignature,ConsonanceRating> ratingMapForChordSignature = 
				chordToScaleRatingMap.get(chordSig);

		return ratingMapForChordSignature == null? null : ratingMapForChordSignature.get(scaleSig);
	}
	
	/**
	 * Get a set of all chord signatures that have ratings. 
	 * @return set of all chord signatures with ratings.
	 */
	private Set<ChordSignature> getChordSignatureSet(){
		return this.chordToScaleRatingMap.keySet();
	}

	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof ScaleConsonanceModel))
			return false;
		
		ScaleConsonanceModel other = (ScaleConsonanceModel)o;
		
		this.purgeUnratedScales();
		other.purgeUnratedScales();
		
		return this.chordToScaleRatingMap.equals(other.chordToScaleRatingMap);
	}
	
	/**
	 * Find any chords with no scale ratings and remove them to simplify
	 * equals comparison and save/load methods.
	 */
	private void purgeUnratedScales() {
		List<ChordSignature> chordsWithNoRatings = new LinkedList<>();
		
		for(ChordSignature chordSig : this.chordToScaleRatingMap.keySet()) {
			Map<ScaleSignature,ConsonanceRating> scaleRatingMap = 
					this.chordToScaleRatingMap.get(chordSig);
			
			if(scaleRatingMap.size() == 0) {
				chordsWithNoRatings.add(chordSig);
			}
		}
		
		for(ChordSignature chordSig : chordsWithNoRatings) {
			this.chordToScaleRatingMap.remove(chordSig);
		}
	}

	@Override
	public ScaleConsonanceRecord addRating(ScaleConsonanceRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScaleConsonanceRecord removeRating(ScaleConsonanceRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScaleConsonanceRecord getRating(ScaleConsonanceRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScaleConsonanceRecord getNextRecordToBeRated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScaleConsonanceRecord getLastRecordRated() {
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
