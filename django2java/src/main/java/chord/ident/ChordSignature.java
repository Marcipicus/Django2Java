package chord.ident;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import chord.Interval;
import chord.exceptions.DuplicateIntervalException;

public enum ChordSignature {
	//diads
	b2("b2",
			Interval.UNISON,
			Interval.MINOR2),
	_2("2",
			Interval.UNISON,
			Interval.MAJOR2),
	b3("b3",
			Interval.UNISON,
			Interval.MINOR3),
	_3("3",
			Interval.UNISON,
			Interval.MAJOR3),
	P4("4",
			Interval.UNISON,
			Interval.PERFECT4),
	dim5("dim5",
			Interval.UNISON,
			Interval.DIMINISHED5),
	P5("5",
			Interval.UNISON,
			Interval.PERFECT5),
	b6("b6",
			Interval.UNISON,
			Interval.MINOR6),
	_6("6",
			Interval.UNISON,
			Interval.MAJOR6),
	b7("b7",
			Interval.UNISON,
			Interval.MINOR7),
	_7("7",
			Interval.UNISON,
			Interval.MAJOR7),
	P8("8",
			Interval.UNISON,
			Interval.PERFECT8),
	b9("b9",
			Interval.UNISON,
			Interval.MINOR9),
	_9("9",
			Interval.UNISON,
			Interval.MAJOR9),
	b10("b10",
			Interval.UNISON,
			Interval.MINOR10),
	_10("10",
			Interval.UNISON,
			Interval.MAJOR10),
	_11("11",
			Interval.UNISON,
			Interval.PERFECT11),
	b12("b12",
			Interval.UNISON,
			Interval.DIMINISHED12),
	_12("12",
			Interval.UNISON,
			Interval.PERFECT12),
	b13("b13",
			Interval.UNISON,
			Interval.MINOR13),
	_13("13",
			Interval.UNISON,
			Interval.MAJOR13),
	b14("b14",
			Interval.UNISON,
			Interval.MINOR14),
	_14("14",
			Interval.UNISON,
			Interval.MAJOR14),
	P15("15",
			Interval.UNISON,
			Interval.PERFECT15),

	//triads
	MINOR("m", 
			Interval.UNISON,
			Interval.MINOR3, 
			Interval.PERFECT5),
	MAJOR("M", 
			Interval.UNISON,
			Interval.MAJOR3, 
			Interval.PERFECT5),
	SUS2 ("sus2", 
			Interval.UNISON,
			Interval.MAJOR2, 
			Interval.PERFECT5),
	SUS4 ("sus4", 
			Interval.UNISON,
			Interval.PERFECT4, 
			Interval.PERFECT5),
	DIMINISHED ("dim", 
			Interval.UNISON,
			Interval.MINOR3, 
			Interval.DIMINISHED5),
	AUGMENTED ("aug", 
			Interval.UNISON,
			Interval.MAJOR3, 
			Interval.MINOR6),
	DIMSUS2 ("dim sus2", 
			Interval.UNISON,
			Interval.MAJOR2, 
			Interval.DIMINISHED5),
	DIMSUS4 ("dim sus4", 
			Interval.UNISON,
			Interval.PERFECT4, 
			Interval.DIMINISHED5),
	AUGSUS2 ("aug sus2", 
			Interval.UNISON,
			Interval.MAJOR2, 
			Interval.MINOR6),
	AUGSUS4 ("aug sus4", 
			Interval.UNISON,
			Interval.PERFECT4, 
			Interval.MINOR6),

	//Quadads
	MINOR_7("m7",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.PERFECT5,
			Interval.MINOR7),
	MAJOR_7("M7",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5,
			Interval.MAJOR7),
	DOMINANT_7("dom7",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5,
			Interval.MINOR7),
	MINOR_7_b5("m7b5",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.DIMINISHED5,
			Interval.MINOR7),
	DIMINISHED_7("dim7",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.DIMINISHED5,
			Interval.MAJOR6),
	MAJOR_6("M6",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5,
			Interval.MAJOR6),
	MINOR_6("m6",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.PERFECT5,
			Interval.MAJOR6),

	//AddedChords
	//Minor added chords
	MIN_ADD9("madd9",
			Interval.UNISON,
			Interval.MINOR3, 
			Interval.PERFECT5,
			Interval.MAJOR9),
	MIN_ADD11("madd11",
			Interval.UNISON,
			Interval.MINOR3, 
			Interval.PERFECT5,
			Interval.PERFECT11),
	MIN_ADD13("madd13",
			Interval.UNISON,
			Interval.MINOR3, 
			Interval.PERFECT5,
			Interval.MAJOR13),

	//Major added chords
	MAJ_ADD9("Madd9",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5,
			Interval.MAJOR9),
	MAJ_ADD11("Madd11",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5,
			Interval.PERFECT11),
	MAJ_ADD13("Madd13",
			Interval.UNISON,
			Interval.MAJOR3, 
			Interval.PERFECT5,
			Interval.MAJOR13),

	//Diminished added chords
	DIM_ADD9("dimadd9",
			Interval.UNISON,
			Interval.MINOR3, 
			Interval.DIMINISHED5,
			Interval.MAJOR9),
	DIM_ADD11("dimadd11",
			Interval.UNISON,
			Interval.MINOR3, 
			Interval.DIMINISHED5,
			Interval.PERFECT11),
	DIM_ADD13("dimadd13",
			Interval.UNISON,
			Interval.MINOR3, 
			Interval.DIMINISHED5,
			Interval.MAJOR13),

	//Augmented added chords
	AUG_ADD9("augadd9",
			Interval.UNISON,
			Interval.MAJOR3, 
			Interval.MINOR6,
			Interval.MAJOR9),
	AUG_ADD11("augadd11",
			Interval.UNISON,
			Interval.MAJOR3, 
			Interval.MINOR6,
			Interval.PERFECT11),
	AUG_ADD13("augadd13",
			Interval.UNISON,
			Interval.MAJOR3, 
			Interval.MINOR6,
			Interval.MAJOR13),

	//Extended Chords

	//Extended chords based on MINOR 7
	MINOR_7_EXT_9("m7ext9" ,
			Interval.UNISON,
			Interval.MINOR3,
			Interval.PERFECT5,
			Interval.MINOR7,
			Interval.MAJOR9),
	MINOR_7_EXT_11("m7ext11",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.PERFECT5,
			Interval.MINOR7,
			Interval.MAJOR9,
			Interval.PERFECT11),
	MINOR_7_EXT_13("m7ext13",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.PERFECT5,
			Interval.MINOR7,
			Interval.MAJOR9,
			Interval.PERFECT11,
			Interval.MAJOR13),

	//Extended chords based on MAJOR_7
	MAJOR_7_EXT_9("M7ext9",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5,
			Interval.MAJOR7,
			Interval.MAJOR9),
	MAJOR_7_EXT_11("M7ext11",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5,
			Interval.MAJOR7,
			Interval.MAJOR9,
			Interval.PERFECT11),
	MAJOR_7_EXT_13("M7ext13",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5,
			Interval.MAJOR7,
			Interval.MAJOR9,
			Interval.PERFECT11, 
			Interval.MAJOR13),

	//Extended chords based on DOMINANT 7
	DOMINANT_7_EXT_9("dom7ext9",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5,
			Interval.MINOR7,
			Interval.MAJOR9),
	DOMINANT_7_EXT_11("dom7ext11",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5,
			Interval.MINOR7,
			Interval.MAJOR9,Interval.PERFECT11),
	DOMINANT_7_EXT_13("dom7ext13",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5,
			Interval.MINOR7,
			Interval.MAJOR9,
			Interval.PERFECT11, 
			Interval.MAJOR13),

	//Extended chords based on MINOR 7 b5
	MINOR_7_b5_EXT_9("m7b5ext9",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.DIMINISHED5,
			Interval.MINOR7,
			Interval.MAJOR9),
	MINOR_7_b5_EXT_11("m7b5ext11",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.DIMINISHED5,
			Interval.MINOR7,
			Interval.MAJOR9,
			Interval.PERFECT11),
	MINOR_7_b5_EXT_13("m7b5ext13",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.DIMINISHED5,
			Interval.MINOR7,
			Interval.MAJOR9,
			Interval.PERFECT11, 
			Interval.MAJOR13),

	//Extended chords based on DIMINISHED 7
	DIMINISHED_7_EXT_9("dim7ext9",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.DIMINISHED5,
			Interval.MAJOR6,
			Interval.MAJOR9),
	DIMINISHED_7_EXT_11("dim7ext11",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.DIMINISHED5,
			Interval.MAJOR6,
			Interval.MAJOR9,
			Interval.PERFECT11),
	DIMINISHED_7_EXT_13("dim7ext13",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.DIMINISHED5,
			Interval.MAJOR6,
			Interval.MAJOR9,
			Interval.PERFECT11, 
			Interval.MAJOR13),


	//Extended chords based on MAJOR 6
	MAJOR_6_EXT_9("M6ext9",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5
			,Interval.MAJOR6,
			Interval.MAJOR9),
	MAJOR_6_EXT_11("M6ext11",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5,
			Interval.MAJOR6,
			Interval.MAJOR9,
			Interval.PERFECT11),
	MAJOR_6_EXT_13("M6ext13",
			Interval.UNISON,
			Interval.MAJOR3,
			Interval.PERFECT5,
			Interval.MAJOR6,
			Interval.MAJOR9,
			Interval.PERFECT11, 
			Interval.MAJOR13),

	//Extended chords based on MINOR 6
	MINOR_6_EXT_9("m6ext9",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.PERFECT5,
			Interval.MAJOR6,
			Interval.MAJOR9),
	MINOR_6_EXT_11("m6ext11",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.PERFECT5,
			Interval.MAJOR6,
			Interval.MAJOR9,
			Interval.PERFECT11),
	MINOR_6_EXT_13("m6ext13",
			Interval.UNISON,
			Interval.MINOR3,
			Interval.PERFECT5,
			Interval.MAJOR6,
			Interval.MAJOR9,
			Interval.PERFECT11, 
			Interval.MAJOR13);



	public static final String MSG_DUPLICATE_INTERVALS_EXCEPTION = "The signature contains duplicate intervals....check your code";
	public static final String MSG_SIGNATURE_STRING_EXCEPTION = "The signatureText argument may not be null"; 
	public static final String MSG__CONSTRUCTOR_MUST_HAVE_AT_LEAST_ONE_INTERVAL = "The constructor must recieve at least one interval";

	//storage variable for convenience when creating large nbumbers of chords
	private static List<ChordSignature> allDiads;

	/**
	 * Get an unmodifiable list containing all of the possible diad combinations.
	 * @return list of all diads
	 */
	public static List<ChordSignature> getAllDiads(){
		if(allDiads == null) {
			allDiads = new LinkedList<ChordSignature>();

			allDiads.add(b2);
			allDiads.add(_2);
			allDiads.add(b3);
			allDiads.add(_3);
			allDiads.add(P4);
			allDiads.add(dim5);
			allDiads.add(P5);
			allDiads.add(b6);
			allDiads.add(_6);
			allDiads.add(b7);
			allDiads.add(_7);
			allDiads.add(P8);
			allDiads.add(b9);
			allDiads.add(_9);
			allDiads.add(b10);
			allDiads.add(_10);
			allDiads.add(_11);
			allDiads.add(b12);
			allDiads.add(_12);
			allDiads.add(b13);
			allDiads.add(_13);
			allDiads.add(b14);
			allDiads.add(_14);
			allDiads.add(P15);

			allDiads = Collections.unmodifiableList(allDiads);
		}

		return allDiads;
	}

	private static List<ChordSignature> allTriads;

	/**
	 * Get an unmodifiable list of all triad signatures.
	 * @return
	 */
	public static List<ChordSignature> getAllTriads(){
		if(allTriads == null) {
			allTriads = new LinkedList<ChordSignature>();

			allTriads.add(MINOR);
			allTriads.add(MAJOR);
			allTriads.add(SUS2);
			allTriads.add(SUS4);
			allTriads.add(DIMINISHED);
			allTriads.add(AUGMENTED);
			allTriads.add(DIMSUS2);
			allTriads.add(DIMSUS4);
			allTriads.add(AUGSUS2);
			allTriads.add(AUGSUS4);

			allTriads = Collections.unmodifiableList(allTriads);
		}

		return allTriads;
	}
	
	private static List<ChordSignature> allQuadads;

	/**
	 * Get an unmodifiable list of all quadad signatures.
	 * @return
	 */
	public static List<ChordSignature> getAllQuadads(){
		if(allQuadads == null) {
			allQuadads = new LinkedList<ChordSignature>();

			allQuadads.add(MINOR_7);
			allQuadads.add(MAJOR_7);
			allQuadads.add(DOMINANT_7);
			allQuadads.add(MINOR_7_b5);
			allQuadads.add(DIMINISHED_7);
			allQuadads.add(MAJOR_6);
			allQuadads.add(MINOR_6);

			allQuadads = Collections.unmodifiableList(allQuadads);
		}

		return allQuadads;
	}

	private static List<ChordSignature> minorAddedChords;
	
	/**
	 * Get an unmodifiable list of all added chords based on a minor triad
	 * @return
	 */
	public static List<ChordSignature> getMinorAddedChords(){
		if(minorAddedChords == null) {
			minorAddedChords = new LinkedList<ChordSignature>();

			minorAddedChords.add(MIN_ADD9);
			minorAddedChords.add(MIN_ADD11);
			minorAddedChords.add(MIN_ADD13);

			minorAddedChords = Collections.unmodifiableList(minorAddedChords);
		}

		return minorAddedChords;
	}
	
	private static List<ChordSignature> majorAddedChords;

	/**
	 * Get an unmodifiable list of all added chords based on a major triad
	 * @return
	 */
	public static List<ChordSignature> getMajorAddedChords(){
		if(majorAddedChords == null) {
			majorAddedChords = new LinkedList<ChordSignature>();

			majorAddedChords.add(MAJ_ADD9);
			majorAddedChords.add(MAJ_ADD11);
			majorAddedChords.add(MAJ_ADD13);

			majorAddedChords = Collections.unmodifiableList(majorAddedChords);
		}

		return majorAddedChords;
	}
	
	private static List<ChordSignature> diminishedAddedChords;

	/**
	 * Get an unmodifiable list of all added chords based on a diminished triad
	 * @return
	 */
	public static List<ChordSignature> getDiminishedAddedChords(){
		if(diminishedAddedChords == null) {
			diminishedAddedChords = new LinkedList<ChordSignature>();

			diminishedAddedChords.add(DIM_ADD9);
			diminishedAddedChords.add(DIM_ADD11);
			diminishedAddedChords.add(DIM_ADD13);

			diminishedAddedChords = Collections.unmodifiableList(diminishedAddedChords);
		}

		return diminishedAddedChords;
	}
	
	private static List<ChordSignature> augmentedAddedChords;

	/**
	 * Get an unmodifiable list of all added chords based on an augmented triad
	 * @return
	 */
	public static List<ChordSignature> getAugmentedAddedChords(){
		if(augmentedAddedChords == null) {
			augmentedAddedChords = new LinkedList<ChordSignature>();

			augmentedAddedChords.add(AUG_ADD9);
			augmentedAddedChords.add(AUG_ADD11);
			augmentedAddedChords.add(AUG_ADD13);

			augmentedAddedChords = Collections.unmodifiableList(augmentedAddedChords);
		}

		return augmentedAddedChords;
	}

	
	private static List<ChordSignature> allAddedChords;

	/**
	 * Get an unmodifiable list of all added chords. 
	 * @return
	 */
	public static List<ChordSignature> getAllAddedChords(){
		if(allAddedChords == null) {
			allAddedChords = new LinkedList<ChordSignature>();

			allAddedChords.addAll(getMinorAddedChords());
			allAddedChords.addAll(getMajorAddedChords());
			allAddedChords.addAll(getDiminishedAddedChords());
			allAddedChords.addAll(getAugmentedAddedChords());

			allAddedChords = Collections.unmodifiableList(allAddedChords);
		}

		return allAddedChords;
	}
	
	private static List<ChordSignature> allMinor7ExtendedChords;

	/**
	 * Get an unmodifiable list of all extended chords based on a Minor 7 quadad.
	 * @return
	 */
	public static List<ChordSignature> getAllMinor7ExtendedChords(){
		if(allMinor7ExtendedChords == null) {
			allMinor7ExtendedChords = new LinkedList<ChordSignature>();

			allMinor7ExtendedChords.add(MINOR_7_EXT_9);
			allMinor7ExtendedChords.add(MINOR_7_EXT_11);
			allMinor7ExtendedChords.add(MINOR_7_EXT_13);

			allMinor7ExtendedChords = Collections.unmodifiableList(allMinor7ExtendedChords);
		}

		return allMinor7ExtendedChords;
	}
	
	private static List<ChordSignature> allMajor7ExtendedChords;

	/**
	 * Get an unmodifiable list of all extended chords based on a Minor 7 quadad.
	 * @return
	 */
	public static List<ChordSignature> getallMajor7ExtendedChords(){
		if(allMajor7ExtendedChords == null) {
			allMajor7ExtendedChords = new LinkedList<ChordSignature>();

			allMajor7ExtendedChords.add(MAJOR_7_EXT_9);
			allMajor7ExtendedChords.add(MAJOR_7_EXT_11);
			allMajor7ExtendedChords.add(MAJOR_7_EXT_13);

			allMajor7ExtendedChords = Collections.unmodifiableList(allMajor7ExtendedChords);
		}

		return allMajor7ExtendedChords;
	}
	
	private static List<ChordSignature> allDominant7ExtendedChords;

	/**
	 * Get an unmodifiable list of all extended chords based on a dominant 7 quadad.
	 * @return
	 */
	public static List<ChordSignature> getallDominant7ExtendedChords(){
		if(allDominant7ExtendedChords == null) {
			allDominant7ExtendedChords = new LinkedList<ChordSignature>();

			allDominant7ExtendedChords.add(DOMINANT_7_EXT_9);
			allDominant7ExtendedChords.add(DOMINANT_7_EXT_11);
			allDominant7ExtendedChords.add(DOMINANT_7_EXT_13);

			allDominant7ExtendedChords = Collections.unmodifiableList(allDominant7ExtendedChords);
		}

		return allDominant7ExtendedChords;
	}
	
	private static List<ChordSignature> allMinor7b5ExtendedChords;

	/**
	 * Get an unmodifiable list of all extended chords based on a minor 7 b5 quadad.
	 * @return
	 */
	public static List<ChordSignature> getallMinor7b5ExtendedChords(){
		if(allMinor7b5ExtendedChords == null) {
			allMinor7b5ExtendedChords = new LinkedList<ChordSignature>();

			allMinor7b5ExtendedChords.add(MINOR_7_b5_EXT_9);
			allMinor7b5ExtendedChords.add(MINOR_7_b5_EXT_11);
			allMinor7b5ExtendedChords.add(MINOR_7_b5_EXT_13);

			allMinor7b5ExtendedChords = Collections.unmodifiableList(allMinor7b5ExtendedChords);
		}

		return allMinor7b5ExtendedChords;
	}
	
	private static List<ChordSignature> allDiminished7ExtendedChords;

	/**
	 * Get an unmodifiable list of all extended chords based on a diminished 7 quadad.
	 * @return
	 */
	public static List<ChordSignature> getallDiminished7ExtendedChords(){
		if(allDiminished7ExtendedChords == null) {
			allDiminished7ExtendedChords = new LinkedList<ChordSignature>();

			allDiminished7ExtendedChords.add(DIMINISHED_7_EXT_9);
			allDiminished7ExtendedChords.add(DIMINISHED_7_EXT_11);
			allDiminished7ExtendedChords.add(DIMINISHED_7_EXT_13);

			allDiminished7ExtendedChords = Collections.unmodifiableList(allDiminished7ExtendedChords);
		}

		return allDiminished7ExtendedChords;
	}
	
	
	private static List<ChordSignature> allMajor6ExtendedChords;

	/**
	 * Get an unmodifiable list of all extended chords based on a major 6 quadad.
	 * @return
	 */
	public static List<ChordSignature> getallMajor6ExtendedChords(){
		if(allMajor6ExtendedChords == null) {
			allMajor6ExtendedChords = new LinkedList<ChordSignature>();

			allMajor6ExtendedChords.add(MAJOR_6_EXT_9);
			allMajor6ExtendedChords.add(MAJOR_6_EXT_11);
			allMajor6ExtendedChords.add(MAJOR_6_EXT_13);

			allMajor6ExtendedChords = Collections.unmodifiableList(allMajor6ExtendedChords);
		}

		return allMajor6ExtendedChords;
	}
	
	private static List<ChordSignature> allMinor6ExtendedChords;

	/**
	 * Get an unmodifiable list of all extended chords based on a major 6 quadad.
	 * @return
	 */
	public static List<ChordSignature> getallMinor6ExtendedChords(){
		if(allMinor6ExtendedChords == null) {
			allMinor6ExtendedChords = new LinkedList<ChordSignature>();

			allMinor6ExtendedChords.add(MINOR_6_EXT_9);
			allMinor6ExtendedChords.add(MINOR_6_EXT_11);
			allMinor6ExtendedChords.add(MINOR_6_EXT_13);

			allMinor6ExtendedChords = Collections.unmodifiableList(allMinor6ExtendedChords);
		}

		return allMinor6ExtendedChords;
	}
	
	private static List<ChordSignature> allExtendedChords;

	/**
	 * Get an unmodifiable list of all extended chords. 
	 * @return
	 */
	public static List<ChordSignature> getAllExtendedChords(){
		if(allExtendedChords == null) {
			allExtendedChords = new LinkedList<ChordSignature>();

			allExtendedChords.addAll(getAllMinor7ExtendedChords());
			allExtendedChords.addAll(getallMajor7ExtendedChords());
			allExtendedChords.addAll(getallDominant7ExtendedChords());
			allExtendedChords.addAll(getallMinor7b5ExtendedChords());
			allExtendedChords.addAll(getallDiminished7ExtendedChords());
			allExtendedChords.addAll(getallMajor6ExtendedChords());
			allExtendedChords.addAll(getallMinor6ExtendedChords());

			allExtendedChords = Collections.unmodifiableList(allExtendedChords);
		}

		return allExtendedChords;
	}
	
	private static List<ChordSignature> allChordSignatures;
	
	/**
	 * Get an unmodifiable list of all chord signatures.
	 * @return
	 */
	public static List<ChordSignature> getAllChordSignatures(){
		if(allChordSignatures == null) {
			allChordSignatures = new LinkedList<ChordSignature>();
			allChordSignatures.addAll(getAllDiads());
			allChordSignatures.addAll(getAllTriads());
			allChordSignatures.addAll(getAllQuadads());
			allChordSignatures.addAll(getAllAddedChords());
			allChordSignatures.addAll(getAllExtendedChords());
			
			allChordSignatures = Collections.unmodifiableList(allChordSignatures);
		}
		
		return allChordSignatures;
	}
	
	private static List<ChordSignature> valuesAsList;
	
	/**
	 * Get a list of all chord signatures sorted by ordinal.
	 * Completely equivalent to ChordSingature.values
	 * 
	 * USE THIS INSTEAD OF ChordSignature.values().
	 * 
	 * enum.values() creates a new array every time it is called
	 * so there is a massive increase in performance in nested
	 * for loops.
	 * 
	 * @return an unmodifiable list that contains all
	 * ChordSignatures ordered by ordinal.
	 */
	public static List<ChordSignature> valuesAsList(){
		if(valuesAsList == null) {
			valuesAsList = new ArrayList<>(Arrays.asList(ChordSignature.values()));

			Collections.sort(valuesAsList);
			
			valuesAsList = Collections.unmodifiableList(valuesAsList);
		}
		return valuesAsList;
	}
	
	public static ChordSignature firstSignature() {
		return valuesAsList().get(0);
	}
	
	public static ChordSignature lastSignature() {
		return valuesAsList().get(getLargestOrdinal());
	}
	
	/**
	 * Get the largest possible ordinal value for a note.
	 * @return
	 */
	private static int getLargestOrdinal() {
		return valuesAsList.size() - 1;
	}
	
	private final String signatureText;
	private final List<Interval> intervals;

	/**
	 * Create a chord signature using the given list of intervals.
	 * THE UNISON INTERVAL MUST BE PASSED ALONG WITH THE OTHER INTERVALS
	 * THAT DEFINE THE CHORD
	 * @param signatureText text used to define signature in displayText()must not be null
	 * @param intervals list of intervals
	 * @throws DuplicateIntervalException when an interval is added twice
	 */
	private ChordSignature(String signatureText, Interval... intervals) {
		if(signatureText == null) {
			throw new IllegalArgumentException(MSG_SIGNATURE_STRING_EXCEPTION);
		}

		this.signatureText = signatureText;

		int numberOfIntervals = intervals.length;
		if(numberOfIntervals < 1) {
			throw new IllegalArgumentException(MSG__CONSTRUCTOR_MUST_HAVE_AT_LEAST_ONE_INTERVAL);
		}

		Arrays.sort(intervals);

		Set<Interval> intervalsSet = new HashSet<Interval>(Arrays.asList(intervals));

		if(numberOfIntervals != intervalsSet.size()) {
			throw new DuplicateIntervalException(MSG_DUPLICATE_INTERVALS_EXCEPTION);
		}

		//Make sure that the signature cannot be modified after creation
		this.intervals = Collections.unmodifiableList(Arrays.asList(intervals));
	}
	
	/**
	 * Get the previous chord by its order of declaration.
	 * 
	 * @return the previous chord signature by its order of declaration
	 * the last chord signature if this instance is the first signature.
	 */
	public ChordSignature getPreviousChordSignature() {
		if(this.ordinal() == 0) {
			return values()[ChordSignature.getLargestOrdinal()];
		}else {
			return values()[this.ordinal() -1];
		}
	}
	
	/**
	 * Get the next chord signature by its order of declaration.
	 * 
	 * @return the next chord signature by its order of declaration,
	 * the first chord signature if this instance is the last signature
	 */
	public ChordSignature getNextChordSignature() {
			if(this.ordinal()+1 > ChordSignature.getLargestOrdinal()) {
				return values()[0];
			}else {
				return values()[this.ordinal()+1];
			}
	}
	
	/**
	 * Get all of the intervals for this signature.
	 * @return
	 */
	public List<Interval> getIntervals(){
		return intervals;
	}

	public String displayText() {
		return signatureText;
	}

	public boolean isLastSignature() {
		return ordinal() == (ChordSignature.values().length - 1);
	}
	
	public boolean isFirstSignature() {
		return ordinal() == 0;
	}
}
