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
import chord.relations.persist.P_ChordChangeConsonance;
import chord.relations.persist.P_EndChordRatingList;
import chord.relations.persist.P_IntervalRating;

public class ChordChangeConsonance {
	
	static P_ChordChangeConsonance convertErrorCheckedToPersistableChordChangeConsonance(
			ChordChangeConsonance originalConsonance) {
		if(originalConsonance == null) {
			throw new NullPointerException("originalConsonanceModel may not be null");
		}
		
		P_ChordChangeConsonance persistentModel = new P_ChordChangeConsonance();
		persistentModel.setStartSignature(originalConsonance.getStartSignature());
		persistentModel.setEndChordList(new LinkedList<P_EndChordRatingList>());
		
		for(EndChordRatingList errorCheckedEndRatingList : originalConsonance.getEndChordRatingList()) {
			P_EndChordRatingList p_endChordRatingList = new P_EndChordRatingList();
			p_endChordRatingList.setEndsignature(errorCheckedEndRatingList.getEndsignature());
			
			List<P_IntervalRating> p_intervalRatings = new LinkedList<P_IntervalRating>();
			for(IntervalRating intervalRating : errorCheckedEndRatingList.getIntervalRatings()) {
				P_IntervalRating p_intervalRating = new P_IntervalRating();
				p_intervalRating.setIntervalBetweenRoots(intervalRating.getIntervalBetweenRoots());
				p_intervalRating.setRating(intervalRating.getRating());
				
				p_intervalRatings.add(p_intervalRating);
			}
			
			p_endChordRatingList.setIntervalRatings(p_intervalRatings);
			persistentModel.getEndChordList().add(p_endChordRatingList);
		}
		
		return persistentModel;
	}
	
	static ChordChangeConsonance convertPersistableConsonanceToErrorCheckedChordChangeConsonance(
			P_ChordChangeConsonance persistableConsonance) {
		if(persistableConsonance == null) {
			throw new NullPointerException("persistable consonance may not be null");
		}
		ChordChangeConsonance errorChecked = 
				new ChordChangeConsonance(persistableConsonance.getStartSignature());
		
		for(P_EndChordRatingList p_endRatingList : persistableConsonance.getEndChordList()) {
			
			EndChordRatingList errorCheckedEndChordRatingList = 
					new EndChordRatingList(
							persistableConsonance.getStartSignature(), 
							p_endRatingList.getEndsignature());
			
			for(P_IntervalRating p_rating : p_endRatingList.getIntervalRatings()) {
				IntervalRating errorCheckedRating = 
						new IntervalRating(
								p_rating.getIntervalBetweenRoots(), 
								p_rating.getRating());
				errorCheckedEndChordRatingList.addIntervalRating(errorCheckedRating);
			}
			errorChecked.addNewEntryToEndChordRatingList(errorCheckedEndChordRatingList);
		}
		
		return errorChecked;
	}
	
	public static void saveChordChangeConsonanceModelToFile(File destinationFile, ChordChangeConsonance model) throws JAXBException {
		P_ChordChangeConsonance persistableModel = 
				convertErrorCheckedToPersistableChordChangeConsonance(model);
		
		JAXBContext context = JAXBContext.newInstance(P_ChordChangeConsonance.class);

		Marshaller marshaller = context.createMarshaller();

		marshaller.marshal(persistableModel,destinationFile);
	}
	
	public static ChordChangeConsonance loadChordChangeConsonanceModelFromFile(File originFile) throws JAXBException {
	    JAXBContext jContext = JAXBContext.newInstance(P_ChordChangeConsonance.class);
	    
	    Unmarshaller unmarshallerObj = jContext.createUnmarshaller();
	    
	    P_ChordChangeConsonance p_ChordChangeConsonanceModel =
	    		(P_ChordChangeConsonance) unmarshallerObj.unmarshal(originFile);
	    
	    return convertPersistableConsonanceToErrorCheckedChordChangeConsonance(p_ChordChangeConsonanceModel);
	}
	
	private ChordSignature startSignature,currentEndSignature;
	private List<EndChordRatingList> endChordList;

	public ChordChangeConsonance(ChordSignature startSignature) {
		if(startSignature == null) {
			throw new NullPointerException("Start Chord Signature may not be null");
		}
		this.startSignature = startSignature;
		this.currentEndSignature = ChordSignature.values()[0];
		
		this.endChordList = new LinkedList<EndChordRatingList>();
	}

	public ChordSignature getStartSignature() {
		return startSignature;
	}
	
	public ChordSignature getCurrentEndSignature() {
		return currentEndSignature;
	}
	
	public boolean addNewEntryToEndChordRatingList(EndChordRatingList endList) {
		//if the current End signature is null then we have already filled
		//all of the possible end chords.
		if( currentEndSignature == null) {
			return false;
		}
		for(EndChordRatingList endChordListItem : this.endChordList) {
			//if the list of endChordRatings already contains a rating list
			//with the same start and end chord Signatures then the data structure
			//is corrupt and we need to throw an exception
			if(endChordListItem.getStartSignature().equals(endList.getStartSignature())
					&& endChordListItem.getEndsignature().equals(endList.getEndsignature())) {
				throw new IllegalStateException("Given endList has already been added.");
			}
		}
		this.endChordList.add(endList);
		currentEndSignature = currentEndSignature.getNextChordSignature();
		return true;
	}
	
	/**
	 * Attempt to remove the last EndChordRatingList in the data structure
	 * and return the success of the operation
	 * @return true if the last endChordRatingList was successfully removed,
	 * false if there are no values in the endChordRatingList and therefore
	 * the operation failed
	 */
	public boolean removeLastEntryFromEndChordRatingList() {
		if(endChordList.size() == 0) {
			return false;
		}
		endChordList.remove(endChordList.size() -1);
		if(currentEndSignature == null) {
			currentEndSignature = ChordSignature.getHighestValue();
		}
		
		return true;
	}
	
	/**
	 * Get an unmodifiable copy of all of the endChordRatingLists that
	 * have been added.
	 * 
	 * @return unmodifiable copy of all of the end chord ratings.
	 */
	public List<EndChordRatingList> getEndChordRatingList() {
		return Collections.unmodifiableList(endChordList);
	}
	
	//TODO: Might have to change this method to look at displayText
	@Override
	public String toString() {
		return("StartChord:"+startSignature.toString());
	}

	@Override
	public boolean equals(Object o) {
	    if (o == this)
	        return true;
	    if (!(o instanceof ChordChangeConsonance))
	        return false;
	    ChordChangeConsonance other = (ChordChangeConsonance) o;
	    
	    boolean signaturesEqual, endChordListsEqual;
	    
	    signaturesEqual = this.startSignature.equals(other.startSignature);
	    endChordListsEqual = this.endChordList.equals(other.endChordList);

	    return signaturesEqual && endChordListsEqual;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + ((startSignature==null) ? 0 : startSignature.hashCode());
		result = prime * result + ((endChordList == null) ? 0 : endChordList.hashCode());
		
		return result;
	}

}
