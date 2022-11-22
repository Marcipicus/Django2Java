package chord.relations;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import chord.Interval;
import chord.ident.ChordSignature;

public class NoteConsonanceRecordTest {
	
	/**
	 * All illegal intervals.
	 * @return
	 */
	static Stream<Arguments> allIllegalIntervals(){
		List<Interval> illegalIntervals = new LinkedList<>();
		for(Interval interval : Interval.values()) {
			if( !interval.inFirstOctave()) {
				illegalIntervals.add(interval);
			}
		}
		
		Arguments[] args = new Arguments[illegalIntervals.size()];
		for(int i=0 ; i<illegalIntervals.size();i++) {
			Interval illegalInterval = illegalIntervals.get(i);
			args[i] = Arguments.of(illegalInterval);
			
		}
		
		return Stream.of(args);
	}
	
	/**
	 * All legal intervals.
	 * @return
	 */
	static Stream<Arguments> allLegalIntervals(){
		List<Interval> legalIntervals = new LinkedList<>();
		for(Interval interval : Interval.values()) {
			if( interval.inFirstOctave()) {
				legalIntervals.add(interval);
			}
		}
		
		Arguments[] args = new Arguments[legalIntervals.size()];
		for(int i=0 ; i<legalIntervals.size();i++) {
			Interval illegalInterval = legalIntervals.get(i);
			args[i] = Arguments.of(illegalInterval);
			
		}
		
		return Stream.of(args);
	}

	NoteConsonanceRecord record;
	
	/**
	 * Make sure that no illegal intervals are allowed.
	 * @param interval
	 */
	@ParameterizedTest
	@MethodSource("allIllegalIntervals")
	void testCreateRecordWithInvalidInterval(Interval interval) {
		Exception exception = 
				assertThrows(IllegalArgumentException.class, 
						() -> new NoteConsonanceRecord(
								ChordSignature._10,
								interval,
								null));
	}
	
	/**
	 * Make sure that all legal intervals are allowed.
	 * @param interval
	 */
	@ParameterizedTest
	@MethodSource("allLegalIntervals")
	void testCreateRecordWithValidInterval(Interval interval) {
		//make sure there are no exceptions thrown
		new NoteConsonanceRecord(
				ChordSignature._10,
				interval,
				null);
	}
	
	@ParameterizedTest
	@EnumSource(ChordSignature.class)
	void testAllChordSignatures(ChordSignature chordSig) {
		new NoteConsonanceRecord(
				chordSig,
				Interval.UNISON,
				null);
	}
}
