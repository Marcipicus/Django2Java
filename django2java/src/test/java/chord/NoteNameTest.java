package chord;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class NoteNameTest {

	
	/**
	 * Left side expected value, right side value to increment
	 * @return
	 */
	private static Stream<Arguments> provideNoteNamesForIncrementOneSemitone(){
		return Stream.of(
				Arguments.of(NoteName.A_SHARP, NoteName.A),
				Arguments.of(NoteName.B,NoteName.A_SHARP),
				Arguments.of(NoteName.C, NoteName.B),
				Arguments.of(NoteName.C_SHARP, NoteName.C),
				Arguments.of(NoteName.D, NoteName.C_SHARP ),
				Arguments.of(NoteName.D_SHARP, NoteName.D),
				Arguments.of(NoteName.E, NoteName.D_SHARP),
				Arguments.of(NoteName.F, NoteName.E),
				Arguments.of(NoteName.F_SHARP, NoteName.F),
				Arguments.of(NoteName.G, NoteName.F_SHARP),
				Arguments.of(NoteName.G_SHARP, NoteName.G),
				Arguments.of(NoteName.A, NoteName.G_SHARP)
				);
	}
	
	/**
	 * Left side expected value, right side value to increment
	 * @return
	 */
	private static Stream<Arguments> provideNoteNamesForDecrementOneSemitone(){
		return Stream.of(
				Arguments.of(NoteName.G_SHARP,NoteName.A),
				Arguments.of(NoteName.G,NoteName.G_SHARP),
				Arguments.of(NoteName.F_SHARP,NoteName.G),
				Arguments.of(NoteName.F,NoteName.F_SHARP),
				Arguments.of(NoteName.E,NoteName.F),
				Arguments.of(NoteName.D_SHARP,NoteName.E),
				Arguments.of(NoteName.D,NoteName.D_SHARP),
				Arguments.of(NoteName.C_SHARP,NoteName.D),
				Arguments.of(NoteName.C,NoteName.C_SHARP),
				Arguments.of(NoteName.B, NoteName.C),
				Arguments.of(NoteName.A_SHARP, NoteName.B),
				Arguments.of(NoteName.A, NoteName.A_SHARP)
				);
	}
	
	@ParameterizedTest
	@MethodSource("provideNoteNamesForIncrementOneSemitone")
	void testIncrementOneSemitone(NoteName expectedNoteAfterIncrementByOne, NoteName startingNote ) {
		assertEquals(expectedNoteAfterIncrementByOne,startingNote.incrementOneSemitone());
	}
	
	@ParameterizedTest
	@MethodSource("provideNoteNamesForDecrementOneSemitone")
	void testDecrementOneSemitone(NoteName expectedNoteAfterDecrementByOne, NoteName startingNote ) {
		assertEquals(expectedNoteAfterDecrementByOne,startingNote.decrementOneSemitone());
	}
}
