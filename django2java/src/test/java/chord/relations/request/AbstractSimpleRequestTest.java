package chord.relations.request;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Abstract class used to have a consistent interfaces
 * for simple request tests.
 * 
 * @Test and @BeforeEach annotations are not
 * inherited so we have to add them to subclasses.
 * 
 * Leaving the annotations on the class as a
 * guide for subclasses
 * @author DAD
 *
 * @param <REQUEST_TYPE>
 * @param <CONCRETE_SIMPLE_REQUEST>
 */
public abstract class AbstractSimpleRequestTest<
REQUEST_TYPE, 
CONCRETE_SIMPLE_REQUEST extends SimpleRequest<REQUEST_TYPE>> {
	
	protected CONCRETE_SIMPLE_REQUEST request;
	

	@BeforeEach
	abstract void init();
	
	@Test
	abstract void testAddNullArray();
	
	@Test
	abstract void testAddArrayWithNull();
	
	@Test
	abstract void testAddArrayWithDuplicates();
	
	@Test
	abstract void testAddArrayWithValidValues()  throws RequestInitializationException;
	
	@Test
	abstract void testAddAll();
	
	@Test
	abstract void testContains() throws RequestInitializationException ;
	
	@Test
	void testIsInitialized() {
		assertFalse(request.isInitialized());
		
		request.addAll();
		assertTrue(request.isInitialized());
	}
}
