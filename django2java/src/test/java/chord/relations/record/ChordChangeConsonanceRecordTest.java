package chord.relations.record;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.ConsonanceRating;
import chord.Interval;
import chord.ident.ChordSignature;

/**
 * Class to test persistence in chord change consonance record.
 * 
 * Other functionality created by the jdk so no need to worry about it.
 * @author DAD
 *
 */
public class ChordChangeConsonanceRecordTest {

	ChordChangeConsonanceRecord nullRecord,nonNullRecord,spareRecord;
	
	@BeforeEach
	void init() {
		nullRecord =  
				new ChordChangeConsonanceRecord(
						ChordSignature._10, 
						ChordSignature._13, 
						Interval.MINOR2, 
						null);
		
		nonNullRecord =  
				new ChordChangeConsonanceRecord(
						ChordSignature._10, 
						ChordSignature._13, 
						Interval.MINOR2, 
						ConsonanceRating.BAD);
	}
	
	
	@Test
	void testCreatePersistenceStringNonNullRating() {
		String persistedString = nonNullRecord.createPersistenceString();
		
		spareRecord = ChordChangeConsonanceRecord.createRecordFromString(persistedString);
		
		assertEquals(nonNullRecord,spareRecord);
	}
	
	@Test
	void testCreatePersistenceStringNullRating() {
		String persistedString = nullRecord.createPersistenceString();
		
		spareRecord = ChordChangeConsonanceRecord.createRecordFromString(persistedString);
		
		assertEquals(nullRecord,spareRecord);
	}
}
