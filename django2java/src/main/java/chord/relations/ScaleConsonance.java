package chord.relations;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import chord.ident.ChordSignature;
import chord.ident.ScaleSignature;
import chord.relations.persist.P_NoteConsonance;
import chord.relations.persist.P_ScaleConsonance;
import chord.relations.persist.P_ScaleRating;

public class ScaleConsonance {

	//package scope for testing
	static P_ScaleConsonance convertErrorCheckedToPersistableScaleConsonance(
			ScaleConsonance originalConsonance) {

		if(originalConsonance == null) {
			throw new NullPointerException("Original Consonance Model may not be null");
		}
		P_ScaleConsonance persistableConsonance = new P_ScaleConsonance();

		persistableConsonance.setChordSig(originalConsonance.getChordSig());

		List<P_ScaleRating> newScaleRatings = new LinkedList<P_ScaleRating>();

		for(ScaleRating originalRating : originalConsonance.getScaleRatings()) {
			P_ScaleRating newRating = new P_ScaleRating();
			newRating.setScaleSig(originalRating.getScaleSig());
			newRating.setRating(originalRating.getRating());

			newScaleRatings.add(newRating);
		}

		persistableConsonance.setScaleRatings(newScaleRatings);
		return persistableConsonance;
	}

	//package scope for testing
	static ScaleConsonance convertPersistableConsonanceToErrorCheckedScaleConsonance(
			P_ScaleConsonance persistableConsonance) {
		if(persistableConsonance == null) {
			throw new NullPointerException("persistable consonance may not equal null.");
		}

		ScaleConsonance errorCheckedNoteConsonance = 
				new ScaleConsonance(persistableConsonance.getChordSig());

		for(P_ScaleRating rating : persistableConsonance.getScaleRatings()) {
			ScaleRating errorCheckedScaleRating = 
					new ScaleRating(
							rating.getScaleSig(), 
							rating.getRating());

			errorCheckedNoteConsonance.addScaleRating(errorCheckedScaleRating);
		}

		return errorCheckedNoteConsonance;
	}

	public static void saveScaleConsonanceModelToFile(File destinationFile, ScaleConsonance model) throws JAXBException {
		if( !model.allRatingsCompleted() ) {
			throw new IllegalStateException("Cannot persist Scale Consonance Model to file. Incomplpete Ratings");
		}
		
		P_ScaleConsonance persistableModel = 
				convertErrorCheckedToPersistableScaleConsonance(model);

		JAXBContext context = JAXBContext.newInstance(P_ScaleConsonance.class);

		Marshaller marshaller = context.createMarshaller();

		marshaller.marshal(persistableModel,destinationFile);
	}
	
	public static ScaleConsonance loadScaleConsonanceModelFromFile(File originFile) throws JAXBException {
	    JAXBContext jContext = JAXBContext.newInstance(P_ScaleConsonance.class);
	    
	    Unmarshaller unmarshallerObj = jContext.createUnmarshaller();
	    
	    P_ScaleConsonance p_scaleConsonanceModel =
	    		(P_ScaleConsonance) unmarshallerObj.unmarshal(originFile);
	    
	    return convertPersistableConsonanceToErrorCheckedScaleConsonance(p_scaleConsonanceModel);
	}


	private ChordSignature chordSig;
	private List<ScaleRating> scaleRatings;
	private ScaleSignature currentScaleSignature;

	public ScaleConsonance(ChordSignature chordSig) {
		if(chordSig == null) {
			throw new NullPointerException("The given chord signature may not be null");
		}
		this.chordSig = chordSig;
		this.currentScaleSignature = ScaleSignature.values()[0];
		scaleRatings = new LinkedList<ScaleRating>();
	}

	public ChordSignature getChordSig() {
		return chordSig;
	}

	/**
	 * Attempt to add the current rating to the list of ratings.
	 * @param newScaleRating new rating, must not be null
	 * @return true if the rating was added successfully,
	 * false if we have already added the last rating.
	 */
	public boolean addScaleRating(ScaleRating newScaleRating) {
		if(newScaleRating == null) {
			throw new NullPointerException("newScaleRating may not be null");
		}
		if(scaleRatings.contains(newScaleRating)) {
			//rating has already been added
			return false;
		}

		scaleRatings.add(newScaleRating);
		if(currentScaleSignature.isLastSignature()) {
			return true;
		}
		//TODO: REFACTOR THE FOLLOWING LINE AND TRANSFER THE LOGIC TO ScaleSignature enum
		currentScaleSignature = ScaleSignature.values()[currentScaleSignature.ordinal() + 1];
		return true;
	}

	/**
	 * Attempt to remove the last rating added. 
	 * @return true if at least one rating has been added and
	 * has been successfully removed, false if no ratings have
	 * been added. 
	 */
	public boolean removeLastScaleRating() {
		if(scaleRatings.size() == 0) {
			return false;
		}

		scaleRatings.remove(scaleRatings.size()-1);
		//TODO: REFACTOR THE FOLLOWING LINE AND TRANSFER THE LOGIC TO ScaleSignature enum
		currentScaleSignature = ScaleSignature.values()[currentScaleSignature.ordinal() - 1];
		return true;
	}


	public ScaleSignature getCurrentSignature() {
		return currentScaleSignature;
	}

	public List<ScaleRating> getScaleRatings() {
		return Collections.unmodifiableList(scaleRatings);
	}

	public boolean allRatingsCompleted() {
		return getScaleRatings().size() == ScaleSignature.values().length;
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
		if (!(o instanceof ScaleConsonance))
			return false;
		ScaleConsonance other = (ScaleConsonance) o;

		boolean signaturesEqual, scaleRatingsEqual;

		signaturesEqual = this.chordSig.equals(other.chordSig);
		scaleRatingsEqual = this.scaleRatings.equals(other.scaleRatings);

		return signaturesEqual && scaleRatingsEqual;
	}

	@Override
	public int hashCode() {
		final int prime = 23;

		int result = 1;

		result = prime * result + ((chordSig==null) ? 0 : chordSig.hashCode());
		result = prime * result + ((scaleRatings == null) ? 0 : scaleRatings.hashCode());

		return result;
	}
}
