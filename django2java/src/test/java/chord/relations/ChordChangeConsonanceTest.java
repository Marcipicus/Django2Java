package chord.relations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.persist.P_ChordChangeConsonance;
import chord.relations.persist.P_EndChordRatingList;
import chord.relations.persist.P_IntervalRating;

class ChordChangeConsonanceTest {

	ChordChangeConsonance chordConsonance, filledChordConsonance;
	P_ChordChangeConsonance p_chordConsonance;

	@BeforeEach
	void initializeConsonanceVariables() {
		chordConsonance = new ChordChangeConsonance(ChordSignature.getLowestValue());

		initializeFilledChordConsonance();

		p_chordConsonance = 
				ChordChangeConsonance.convertErrorCheckedToPersistableChordChangeConsonance(
						filledChordConsonance);
	}

	void initializeFilledChordConsonance() {
		final ChordSignature startChordSignature = ChordSignature.getLowestValue();
		filledChordConsonance = new ChordChangeConsonance(startChordSignature);

		for(ChordSignature endChordSig : ChordSignature.values()) {
			EndChordRatingList endList = 
					new EndChordRatingList(filledChordConsonance.getStartSignature(), endChordSig);

			//iterate through all 12 intervals
			int ordinal;
			for(ordinal = 0 ; ordinal < 12 ; ordinal++) {
				//we don't want to add a rating for a chord change that
				//starts and ends on the same root note and has the same chord Signature
				//basically a chord change that starts and ends on the exact same chord.
				if(startChordSignature.equals(endChordSig)) {
					continue;
				}
				endList.addIntervalRating(
						new IntervalRating(Interval.values()[ordinal], ConsonanceRating.MEDIOCRE));
			}

			filledChordConsonance.addNewEntryToEndChordRatingList(endList);
		}
	}

	@Test
	void testConvertChordChangeConsonanceErrorCheckedToPersisted() {
		P_ChordChangeConsonance p_consonance = 
				ChordChangeConsonance.convertErrorCheckedToPersistableChordChangeConsonance(
						filledChordConsonance);

		assertEquals(
				filledChordConsonance.getStartSignature(), 
				p_consonance.getStartSignature());

		//make sure they have the same number of entries so we don't
		//have to check in the following for loops
		assertEquals(
				filledChordConsonance.getEndChordRatingList().size(),
				p_consonance.getEndChordList().size());

		for(int endRatingListIndex = 0 ; 
				endRatingListIndex < filledChordConsonance.getEndChordRatingList().size();
				endRatingListIndex++) {
			EndChordRatingList errorCheckedList = 
					filledChordConsonance.getEndChordRatingList().get(endRatingListIndex);

			P_EndChordRatingList persistedList = 
					p_consonance.getEndChordList().get(endRatingListIndex);

			//make sure they have the same number of entries so we don't
			//have to check in the following for loops
			assertEquals(
					errorCheckedList.getIntervalRatings().size(), 
					persistedList.getIntervalRatings().size());

			for(int ratingIndex = 0; 
					ratingIndex < errorCheckedList.getIntervalRatings().size(); 
					ratingIndex++) {
				IntervalRating errorCheckedRating = 
						errorCheckedList.getIntervalRatings().get(ratingIndex);
				P_IntervalRating persistedRating = 
						persistedList.getIntervalRatings().get(ratingIndex);

				assertEquals(
						errorCheckedRating.getIntervalBetweenRoots(),
						persistedRating.getIntervalBetweenRoots());

				assertEquals(
						errorCheckedRating.getRating(),
						persistedRating.getRating());
			}
		}
	}
	
	@Test
	void testConvertChordChangeConsonancePersistedToErrorChecked() {
		ChordChangeConsonance errorCheckedConsonance = 
				ChordChangeConsonance.convertPersistableConsonanceToErrorCheckedChordChangeConsonance(
						p_chordConsonance);

		assertEquals(
				p_chordConsonance.getStartSignature(),
				errorCheckedConsonance.getStartSignature());

		//make sure they have the same number of entries so we don't
		//have to check in the following for loops
		assertEquals(
				p_chordConsonance.getEndChordList().size(),
				errorCheckedConsonance.getEndChordRatingList().size());

		for(int endRatingListIndex = 0 ; 
				endRatingListIndex < p_chordConsonance.getEndChordList().size();
				endRatingListIndex++) {
			
			P_EndChordRatingList persistedList = 
					p_chordConsonance.getEndChordList().get(endRatingListIndex);
			
			EndChordRatingList errorCheckedList = 
					errorCheckedConsonance.getEndChordRatingList().get(endRatingListIndex);

			//make sure they have the same number of entries so we don't
			//have to check in the following for loops
			assertEquals(
					persistedList.getIntervalRatings().size(),
					errorCheckedList.getIntervalRatings().size());

			for(int ratingIndex = 0; 
					ratingIndex < persistedList.getIntervalRatings().size(); 
					ratingIndex++) {
				P_IntervalRating persistedRating = 
						persistedList.getIntervalRatings().get(ratingIndex);
				IntervalRating errorCheckedRating = 
						errorCheckedList.getIntervalRatings().get(ratingIndex);

				assertEquals(
						persistedRating.getIntervalBetweenRoots(),
						errorCheckedRating.getIntervalBetweenRoots());

				assertEquals(
						persistedRating.getRating(),
						errorCheckedRating.getRating());
			}
		}
	}
	
	@Test
	void testSaveAndLoadChordChangeConsonanceFile() throws JAXBException {
		File testFile = new File("test.tmp");
		
		//TODO: Gotta fix save and loading of files.
		ChordChangeConsonance.saveChordChangeConsonanceModelToFile(testFile, filledChordConsonance);
		ChordChangeConsonance.loadChordChangeConsonanceModelFromFile(testFile);
	}

}
