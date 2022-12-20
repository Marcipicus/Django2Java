package chord.relations.record;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;

/**
 * Mostly used to test the persistence methods 
 * @author DAD
 *
 */
public class ScaleConsonanceRecordTest {

	ScaleConsonanceRecord nullRecord,nonNullRecord,spareRecord;
	
	
	@BeforeEach
	void init() {
		nullRecord = 
				new ScaleConsonanceRecord(
						ChordSignature.MAJOR, 
						ScaleSignature.AEOLIAN,
						null);
		
		nonNullRecord = 
				new ScaleConsonanceRecord(
						ChordSignature.MAJOR, 
						ScaleSignature.AEOLIAN,
						ConsonanceRating.GOOD);
	}
	
	@Test
	void testCreatePersistenceStringNonNullRating() {
		String persistedString = nonNullRecord.createPersistenceString();
		
		spareRecord = ScaleConsonanceRecord.createRecordFromString(persistedString);
		
		assertEquals(nonNullRecord,spareRecord);
	}
	
	@Test
	void testCreatePersistenceStringNullRating() {
		String persistedString = nullRecord.createPersistenceString();
		
		spareRecord = ScaleConsonanceRecord.createRecordFromString(persistedString);
		
		assertEquals(nullRecord,spareRecord);
	}
	
	
}
