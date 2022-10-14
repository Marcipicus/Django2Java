package chord.relations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.persist.P_NoteConsonance;
import chord.relations.persist.P_NoteRating;

class NoteConsonanceTest {
	
	NoteConsonance nc,filledNC;
	P_NoteConsonance p_filledNC;
	
	@BeforeEach
	void initializeNC() {
		nc = new NoteConsonance(ChordSignature.b2);
		
		filledNC = createFilledErrorCheckedNoteConsonance();
		
		p_filledNC = copyErrorCheckedNoteConsonanceToPersistent(filledNC);
	}
	
	NoteConsonance createFilledErrorCheckedNoteConsonance() {
		NoteConsonance testNC = new NoteConsonance(ChordSignature.b2);
		for(Interval intvl : Interval.values()) {
			if(intvl.equals(Interval.PERFECT8)) {
				break;
			}
			testNC.addNoteRating(
					new NoteRating(intvl,ConsonanceRating.MEDIOCRE));
		}
		
		return testNC;
	}
	
	P_NoteConsonance copyErrorCheckedNoteConsonanceToPersistent(NoteConsonance nc_to_copy) {
		p_filledNC = new P_NoteConsonance();
		p_filledNC.setChordSig(nc_to_copy.getChordSig());
		
		List<P_NoteRating> copiedRatingList = new LinkedList<P_NoteRating>();
		
		for(NoteRating noteRating : nc_to_copy.getNoteRatings()) {
			P_NoteRating ratingToAdd = new P_NoteRating();
			ratingToAdd.setNoteInterval(noteRating.getNoteInterval());
			ratingToAdd.setRating(noteRating.getRating());
			
			copiedRatingList.add(ratingToAdd);
		}
		
		p_filledNC.setNoteRatings(copiedRatingList);
		
		return p_filledNC;
	}
	
	@Test
	void testRemoveNoteRatingWhenEmpty() {
		assertFalse(nc.removeLastNoteRating());
		assertTrue(nc.getNoteRatings().size() == 0);
		assertTrue(nc.getChordSig().equals(ChordSignature.b2));
		assertTrue(nc.getCurrentInterval().equals(Interval.UNISON));
	}
	
	@Test
	void testAddNoteRatingWhenFull() {
		
		assertTrue(nc.getCurrentInterval().equals(Interval.UNISON));
		for(Interval ival : Interval.values()) {
			if(ival.equals(Interval.PERFECT8)) {
				Exception exception = 
						assertThrows(IllegalArgumentException.class, 
								() -> nc.addNoteRating(new NoteRating(ival,ConsonanceRating.GOOD)));
				break;
			}
			
			assertTrue(nc.addNoteRating(new NoteRating(ival,ConsonanceRating.GOOD)));
			assertTrue(nc.getCurrentInterval().equals(ival.getNextInterval()));
		}
	}
	
	@Test
	void testAddDuplicateNoteRating() {
		final Interval duplicateInterval = Interval.UNISON;
		nc.addNoteRating(new NoteRating(duplicateInterval,ConsonanceRating.BAD));
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> nc.addNoteRating(new NoteRating(duplicateInterval,ConsonanceRating.GOOD)));	
	}

	@Test
	void testconvertErrorCheckedToPersistableNoteConsonance() {
		P_NoteConsonance p_consonance = 
				NoteConsonance.convertErrorCheckedToPersistableNoteConsonance(filledNC);
		
		assertEquals(filledNC.getChordSig(), p_consonance.getChordSig());
		
		for(NoteRating n_rating : filledNC.getNoteRatings()) {
			boolean equalRatingFound = false;
			
			for(P_NoteRating p_note_rating : p_consonance.getNoteRatings()) {
				
				if(n_rating.getNoteInterval().equals(p_note_rating.getNoteInterval()) &&
						n_rating.getNoteInterval().equals(p_note_rating.getNoteInterval())) {
					
					equalRatingFound = true;
					break;
				}
			}
			
			if(!equalRatingFound) {
				fail();
			}
			
		}
	}
	
	@Test
	void testconvertPersistableConsonanceToErrorCheckedNoteConsonance() {
		NoteConsonance copied_nc = 
				NoteConsonance.convertPersistableConsonanceToErrorCheckedNoteConsonance(p_filledNC);
		
		for(P_NoteRating p_rating : p_filledNC.getNoteRatings()) {
			boolean equalRatingFound = false;
			
			for(NoteRating n_rating : copied_nc.getNoteRatings()) {
				if(p_rating.getNoteInterval().equals(n_rating.getNoteInterval()) &&
						p_rating.getRating().equals(n_rating.getRating())) {
					equalRatingFound = true;
				}
			}
			
			if( !equalRatingFound ) {
				fail();
			}
		}
	}
	
	@Test
	void testSaveToAndLoadFromFile() throws JAXBException {
		File testFile = new File("test.xml");
		
		NoteConsonance.saveNoteConsonanceModelToFile(testFile,filledNC);
		
		NoteConsonance retrievedFromFile = NoteConsonance.loadNoteConsonanceModelFromFile(testFile);
		
		assertEquals(filledNC, retrievedFromFile);
	}
	
	
}
