package chord.relations;

import java.util.Set;

/**
 * RatingModel interface declares the common methods for
 * all ConsonanceRatingDataModels(ChordChangeConsonanceModel,
 * NoteConsonanceModel, and ScaleConsonanceModel.
 * 
 * The interface exists to declare the common functions of
 * all rating data models.
 * @author DAD
 *
 * @param <RECORD> ConsonanceRecord Type for the data model.
 * (NoteConsonanceRecord for NoteConsonanceModel...)
 * @param <REQUEST> Object used to specify the records that
 * the user wants to retrieve.
 */
public interface RatingModel<RECORD,REQUEST> {

	/**
	 * Add the rating represented by the record .
	 * 
	 * @param record the record containing the fields which define
	 * the rating to add.
	 * @return the previous rating that existed for the record,
	 * null if no previous rating exists.
	 */
	RECORD addRating(RECORD record);
	
	/**
	 * Remove the rating represented by the record .
	 * 
	 * @param record the record containing the fields which define
	 * the rating to remove
	 * @return the rating for the record which was removed if it exists,
	 * null if no rating exists.
	 */
	RECORD removeRating(RECORD record);
	
	/**
	 * Get the record containing the rating represented by the
	 * record param.
	 * 
	 * @param record the record containing the fields which
	 * define the rating to retrieve.
	 * @return the record if it exists,
	 * null otherwise
	 */
	RECORD getRating(RECORD record);

	/**
	 * Get the next unrated record. The rating value of the record 
	 * should be ignored.
	 * 
	 * @return record representing the next record
	 * which needs to be rated, null if all ratings
	 * have already been added
	 */
	RECORD getNextRecordToBeRated();
	
	/**
	 * Get the last record which was added to the RatingModel.
	 * 
	 * @return the last record saved,
	 * null if no interactions have been saved.
	 */
	RECORD getLastRecordRated();
	
	/**
	 * Test to see if the model is full.
	 * @return true if all possible ratings are filled.
	 */
	boolean isFull();
	
	/**
	 * Test to see if the model is empty
	 * @return true if there are not ratings in the model
	 */
	boolean isEmpty();
	
	/**
	 * Get the records specified by the request parameter.
	 * @param request object used to specify the records
	 * desired by the caller.
	 * @return a set of records matching the request.
	 */
	Set<RECORD> getRecords(REQUEST request);
	
}
