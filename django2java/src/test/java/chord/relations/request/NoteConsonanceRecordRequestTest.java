package chord.relations.request;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.request.NoteConsonanceRecordRequest;

public class NoteConsonanceRecordRequestTest {

	NoteConsonanceRecordRequest ncrRequest;

	@BeforeEach
	void init() {
		ncrRequest = new NoteConsonanceRecordRequest();
	}



}
