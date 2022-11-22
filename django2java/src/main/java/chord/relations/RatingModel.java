package chord.relations;

/**
 * RatingModel interface declares the common methods for
 * all ConsonanceRatingDataModels(ChordChangeConsonanceModel,
 * NoteConsonanceModel, and ScaleConsonanceModel.
 * 
 * The interface exists to declare the common functions of
 * all rating data models.
 * @author DAD
 *
 * @param <T> ConsonanceRecord Type for the data model.
 * (NoteConsonanceRecord for NoteConsonanceModel...)
 */
public interface RatingModel<T> {

	/**
	 * Add the rating represented by the record .
	 * 
	 * @param record the record containing the fields which define
	 * the rating to add.
	 * @return the previous rating that existed for the record,
	 * null if no previous rating exists.
	 */
	T addRating(T record);
	
	/**
	 * Remove the rating represented by the record .
	 * 
	 * @param record the record containing the fields which define
	 * the rating to remove
	 * @return the rating for the record which was removed if it exists,
	 * null if no rating exists.
	 */
	T removeRating(T record);
	
	/**
	 * Get the record containing the rating represented by the
	 * record param.
	 * 
	 * @param record the record containing the fields which
	 * define the rating to retrieve.
	 * @return the record if it exists,
	 * null otherwise
	 */
	T getRating(T record);

	/**
	 * Get the next unrated record. The rating value of the record 
	 * should be ignored.
	 * 
	 * @return record representing the next record
	 * which needs to be rated, null if all ratings
	 * have already been added
	 */
	T getNextRecordToBeRated();
	
	/**
	 * Get the last record which was added to the RatingModel.
	 * 
	 * @return the last record saved,
	 * null if no interactions have been saved.
	 */
	T getLastRecordRated();
}
