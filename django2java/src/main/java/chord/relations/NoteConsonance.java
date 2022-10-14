package chord.relations;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import chord.Interval;
import chord.ident.ChordSignature;
import chord.relations.persist.P_NoteConsonance;
import chord.relations.persist.P_NoteRating;

public class NoteConsonance {
	
	static P_NoteConsonance convertErrorCheckedToPersistableNoteConsonance(
			NoteConsonance originalConsonance) {
		
		if(originalConsonance == null) {
			throw new NullPointerException("Original Consonance Model may not be null");
		}
		P_NoteConsonance persistableConsonance = new P_NoteConsonance();
		
		persistableConsonance.setChordSig(originalConsonance.getChordSig());
		
		List<P_NoteRating> newNoteRatings = new LinkedList<P_NoteRating>();
		
		for(NoteRating originalRating : originalConsonance.getNoteRatings()) {
			P_NoteRating newRating = new P_NoteRating();
			newRating.setNoteInterval(originalRating.getNoteInterval());
			newRating.setRating(originalRating.getRating());
			newNoteRatings.add(newRating);
		}
		
		persistableConsonance.setNoteRatings(newNoteRatings);
		
		return persistableConsonance;
	}
	
	static NoteConsonance convertPersistableConsonanceToErrorCheckedNoteConsonance(
			P_NoteConsonance persistableConsonance) {
		if(persistableConsonance == null) {
			throw new NullPointerException("persistable consonance may not equal null.");
		}
		
		NoteConsonance errorCheckedNoteConsonance = new NoteConsonance(persistableConsonance.getChordSig());
		
		for(P_NoteRating rating : persistableConsonance.getNoteRatings()) {
			NoteRating errorCheckedNoteRating = 
					new NoteRating(
							rating.getNoteInterval(), 
							rating.getRating());
			
			errorCheckedNoteConsonance.addNoteRating(errorCheckedNoteRating);
		}
		
		return errorCheckedNoteConsonance;
	}
	
	public static void saveNoteConsonanceModelToFile(File destinationFile, NoteConsonance model) throws JAXBException {
		if( !model.isComplete()) {
			throw new IllegalStateException("The NoteConsonanceFile is not complete.");
		}
		
		P_NoteConsonance persistableModel = 
				convertErrorCheckedToPersistableNoteConsonance(model);
		
		JAXBContext context = JAXBContext.newInstance(P_NoteConsonance.class);

		Marshaller marshaller = context.createMarshaller();

		marshaller.marshal(persistableModel,destinationFile);
	}
	
	public static NoteConsonance loadNoteConsonanceModelFromFile(File originFile) throws JAXBException {
	    JAXBContext jContext = JAXBContext.newInstance(P_NoteConsonance.class);
	    
	    Unmarshaller unmarshallerObj = jContext.createUnmarshaller();
	    
	    P_NoteConsonance p_scaleConsonanceModel =
	    		(P_NoteConsonance) unmarshallerObj.unmarshal(originFile);
	    
	    return convertPersistableConsonanceToErrorCheckedNoteConsonance(p_scaleConsonanceModel);
	}
	
	private Interval currentInterval;
	private ChordSignature chordSig;
	private List<NoteRating> noteRatings;
	
	public NoteConsonance(ChordSignature chordSig) {
		if(chordSig == null) {
			throw new NullPointerException("Chord Signature may not be null");
		}
		this.chordSig = chordSig;
		
		noteRatings = new LinkedList<NoteRating>();
		currentInterval = Interval.UNISON;
	}

	public ChordSignature getChordSig() {
		return chordSig;
	}
	
	/**
	 * Get the current interval.
	 * @return current interval, null if ratings for 
	 * all intervals have been added
	 */
	public Interval getCurrentInterval() {
		return currentInterval;
	}
	
	public boolean addNoteRating(NoteRating newRating) {
		if(newRating == null) {
			throw new NullPointerException("newRating may not be null");
		}
		for(NoteRating rating : noteRatings) {
			if(rating.intervalsEqual(newRating)) {
				throw new IllegalArgumentException("Interval Rating already added");
			}
		}
		noteRatings.add(newRating);
		//TODO:Refactor the next line to the Interval enum
		currentInterval = Interval.values()[currentInterval.ordinal() + 1];
		return true;
	}
	

	/**
	 * Attempt to remove the last note rating added.
	 * @return true if there are ratings and the last one
	 * was removed, false if there have not yet been any ratings
	 * added.
	 */
	public boolean removeLastNoteRating() {
		if(noteRatings.size() == 0) {
			return false;
		}
		noteRatings.remove(noteRatings.size()-1);
		//TODO:Refactor the next line to the Interval enum
		currentInterval  = currentInterval.values()[currentInterval.ordinal() -1];
		return true;
	}

	public List<NoteRating> getNoteRatings() {
		return Collections.unmodifiableList(this.noteRatings);
	}
	
	public boolean isComplete() {
		//12 notes in an octave so we stop at number 12
		return noteRatings.size() == 12;
	}
	
	public String getSaveFileName() {
		return "Note-"+this.getChordSig().toString()+".xml";
	}

	
	//TODO: Might have to change this method to look at displayText
	@Override
	public String toString() {
		return("Chord:"+chordSig.toString());
	}

	@Override
	public boolean equals(Object o) {
	    if (o == this)
	        return true;
	    if (!(o instanceof NoteConsonance))
	        return false;
	    NoteConsonance other = (NoteConsonance) o;
	    
	    boolean signaturesEqual, noteRatingsEqual;
	    
	    signaturesEqual = this.chordSig.equals(other.chordSig);
	    noteRatingsEqual = this.noteRatings.equals(other.noteRatings);

	    return signaturesEqual && noteRatingsEqual;
	}
	
	@Override
	public int hashCode() {
		final int prime = 19;
		
		int result = 1;
		
		result = prime * result + ((chordSig==null) ? 0 : chordSig.hashCode());
		result = prime * result + ((noteRatings == null) ? 0 : noteRatings.hashCode());
		
		return result;
	}
}
