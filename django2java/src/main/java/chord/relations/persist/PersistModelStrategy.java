package chord.relations.persist;

import chord.relations.RatingModel;

/**
 * Interface used to save and load RatingModels to/from
 * various data storage formats.
 * 
 * Basic usage...
 * 1.call configure to give the persister everything it needs to
 * know to save or load the data model.
 * 2.call save or load 
 * @author DAD
 *
 * @param <RECORD> type of record for the RatingModel
 * @param <MODEL> model being saved
 * @param <CONFIG_PARAM> object used to store configuration parameters
 */
public interface PersistModelStrategy<RECORD,CONFIG_PARAM, MODEL extends RatingModel<RECORD,CONFIG_PARAM>> {

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
