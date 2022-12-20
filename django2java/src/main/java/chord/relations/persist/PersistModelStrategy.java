package chord.relations.persist;

import chord.relations.RatingModel;
import chord.relations.request.AbstractRecordRequest;

/**
 * Interface used to save and load RatingModels to/from
 * various data storage formats.
 * 
 * Basic usage...
 * 1.call setRequest to specify the records to persist
 * 2.call configure to give the persister everything it needs to
 * know to save or load the data model.
 * 3.call save or load 
 * @author DAD
 *
 * @param <RECORD> type of record for the RatingModel
 * @param <REQUEST> type of request to be used
 * @param <CONFIG_PARAM> object used to store configuration parameters
 * @param <MODEL> model being saved
 */
public interface PersistModelStrategy<RECORD,REQUEST extends AbstractRecordRequest,CONFIG_PARAM, MODEL extends RatingModel<RECORD,REQUEST>> {

	/**
	 * Set the request that will be used to persist only the records
	 * that we want.
	 * @param request request specifying records to persist,
	 * may not be null
	 */
	void setRequest(REQUEST request);
	
	/**
	 * Give the persister any data that it needs to save to/load from
	 * a source/destination.
	 * @param parameters an object containing all of the data required
	 * to save a model or to load a model
	 */
	void configure(CONFIG_PARAM parameters);
	
	/**
	 * Save the model to the destination described by
	 * the parameters passed to  configure.
	 * @param model model to be saved
	 * 
	 * @throws PersistenceException if there is an error saving
	 * the model.
	 */
	void save(MODEL model) throws PersistenceException;
	
	/**
	 * Load the model from the source described by
	 * the parameters passed to configure.
	 * @return an initialized model containing the data
	 * stored in the location described by parameters passed
	 * to configure.
	 * 
	 * @throws PersistenceException if there is an error loading the data
	 */
	MODEL load() throws PersistenceException;
}
